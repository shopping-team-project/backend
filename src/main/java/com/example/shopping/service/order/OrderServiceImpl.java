package com.example.shopping.service.order;

import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.member.Role;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.entity.order.OrderItemEntity;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.repository.order.OrderItemRepository;
import com.example.shopping.repository.order.OrderRepository;
import com.example.shopping.service.s3.S3ItemImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements  OrderService{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final MemberRepository memberRepository;

    private final S3ItemImgUploaderService s3ItemImgUploaderService;

    @Override
    @Transactional
    public OrderDTO orderItem(OrderMainDTO order, String adminEmail){

        Long memberId = memberRepository.findByEmail(order.getMbrEmail()).getMemberId();
        Long adminId = memberRepository.findByEmail(adminEmail).getMemberId();

        ItemEntity item = itemRepository.findByItemId(order.getItemId());

        if(item.getItemSellStatus() != ItemSellStatus.RESERVED){
            //throw 예약된 물품이 아니라 판매 못함
            throw new RuntimeException();
        }

        if(item.getStockNumber() < order.getCount() || item.getStockNumber() == 0){
            //throw 물품 수량이 부족할 때
            throw new RuntimeException();
        }

        List<OrderItemDTO> itemList = new ArrayList<>();

        // 구매처리 하려는 아이템 셋팅
        OrderItemEntity orderItem = OrderItemEntity.setOrderItem(item, memberId, item.getMember().getMemberId(), order.getCount());
        itemList.add(orderItem.toOrderItemDTO());

        OrderDTO orderInfo = OrderDTO.createOrder(adminId, memberId, itemList);

        // 주문처리
        OrderDTO savedOrder = orderRepository.save(orderInfo);
        OrderItemDTO savedOrderItem = orderItemRepository.save(savedOrder.getOrderItem().get(0), savedOrder);

        // Member-point 추가
        MemberEntity member = memberRepository.findByEmail(order.getMbrEmail());
        member.addPoint(savedOrderItem.getItemPrice() * savedOrderItem.getItemAmount());
        memberRepository.save(member);

        // Item-status 변경
        ItemEntity updateItem = itemRepository.findById(order.getItemId()).orElseThrow();
        updateItem.itemSell(order.getCount(), ItemSellStatus.SOLD_OUT);
        itemRepository.save(updateItem);

        //아이템 이미지 삭제처리
        List<ItemImgEntity> findImg = itemImgRepository.findByItemItemId(updateItem.getItemId());
        for(ItemImgEntity img : findImg) {
            String uploadFilePath = img.getUploadImgPath();
            String uuidFileName = img.getUploadImgName();

            // DB에서 이미지 삭제
            itemImgRepository.deleteById(img.getItemImgId());
            // S3에서 삭제
            String result = s3ItemImgUploaderService.deleteFile(uploadFilePath, uuidFileName);
            log.info(result);
        }
        return savedOrder;
    }


    //회원 주문조회
    @Override
    public List<OrderItemDTO> getOrders(String email) {
        //email로 order_id조회하여 판매된 내역 보여줌
        MemberEntity member = memberRepository.findByEmail(email);

        List<OrderDTO> orderList;
        List<OrderItemDTO> orderItemList = new ArrayList<>();

        if(member.getMemberRole() == Role.ADMIN) {
            orderList = orderRepository.findByOrderAdmin(member.getMemberId());
            for(OrderDTO order : orderList){
                orderItemList.add(orderItemRepository.findByOrderOrderId(order.getOrderId()));
            }
        }
        else{
            //구매자 기준 조회
            orderList = orderRepository.findByOrderMember(member.getMemberId());
            for(OrderDTO order : orderList) {
                orderItemList.add(orderItemRepository.findByOrderOrderId(order.getOrderId()));
            }
            //판매자 기준 조회
            orderItemList.add(orderItemRepository.findByItemSeller(member.getMemberId()));
        }
        return orderItemList;
    }

}
