package com.example.shopping.service.item;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemImgDTO;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.service.s3.S3ItemImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Log4j2
public class ItemServiceImpl implements ItemService{
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3ItemImgUploaderService s3ItemImgUploaderService;

    // 상품 등록 메소드
    @Override
    public ResponseEntity<?> saveItem(ItemDTO itemDTO,
                                      List<MultipartFile> itemFiles,
                                      String memberEmail) throws Exception {
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);

        if(findUser != null) {
            // 상품 등록
            ItemEntity item = ItemEntity.builder()
                    .itemName(itemDTO.getItemName())
                    .itemDetail(itemDTO.getItemDetail())
                    .itemSellStatus(itemDTO.getItemSellStatus())
                    .stockNumber(itemDTO.getStockNumber())
                    .price(itemDTO.getPrice())
                    .itemPlace(itemDTO.getSellPlace())
                    .build();

            // S3에 업로드
            List<ItemImgDTO> productImg = s3ItemImgUploaderService.upload("product", itemFiles);
            List<ItemImgEntity> itemImgEntities = new ArrayList<>();

            for (int i = 0; i < productImg.size(); i++) {
                ItemImgDTO itemImgDTO = productImg.get(i);
                ItemImgEntity imgEntity = ItemImgEntity.builder()
                        .oriImgName(itemImgDTO.getOriImgName())
                        .uploadImgPath(itemImgDTO.getUploadImgPath())
                        .uploadImgUrl(itemImgDTO.getUploadImgUrl())
                        .uploadImgName(itemImgDTO.getUploadImgName())
                        .item(item)
                        .repImgYn(i == 0 ? "Y" : "N")
                        .build();
                ItemImgEntity saveImg = itemImgRepository.save(imgEntity);
                log.info("img : " + saveImg);
                itemImgEntities.add(saveImg);
            }
            item = ItemEntity.builder()
                    .itemName(itemDTO.getItemName())
                    .itemDetail(itemDTO.getItemDetail())
                    .itemSellStatus(itemDTO.getItemSellStatus())
                    .stockNumber(itemDTO.getStockNumber())
                    .price(itemDTO.getPrice())
                    .itemPlace(itemDTO.getSellPlace())
                    .itemImgList(itemImgEntities)
                    .build();

            ItemEntity saveItem = itemRepository.save(item);
            ItemDTO toItemDTO = ItemDTO.toItemDTO(saveItem);
            return ResponseEntity.ok().body(toItemDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
        }
    }

    // 상품 상세정보
    // 상품의 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정합니다.
    // 이럴 경우 JPA가 더티체킹(변경감지)를 수행하지 않아서 성능을 향상 시킬 수 있다.
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<ItemDTO> getItem(Long itemId) {
        try {
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);

            ItemDTO itemDTO = ItemDTO.toItemDTO(findItem);
            return ResponseEntity.ok().body(itemDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 상품 수정
    @Override
    public ResponseEntity<?> updateItem(Long itemId,
                                        ItemDTO itemDTO,
                                        List<MultipartFile> itemFiles,
                                        String memberEmail) throws Exception {
        try {
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("item : " + findItem);
            MemberEntity findMember = memberRepository.findByEmail(memberEmail);
            log.info("member : " + findMember);

            // 이메일을 userDetails에서 가져와서 조회한 다음
            // 회원 이메일과 상품에 담긴 member 엔티티의 이메일과 비교
            if(findMember.getEmail().equals(findItem.getMember().getEmail())) {
                // 상품 정보 수정
                findItem = ItemEntity.builder()
                        .itemId(findItem.getItemId())
                        .itemName(itemDTO.getItemName())
                        .itemDetail(itemDTO.getItemDetail())
                        .itemPlace(itemDTO.getSellPlace())
                        .stockNumber(findItem.getStockNumber())
                        .price(itemDTO.getPrice())
                        .build();

                // 기존의 이미지를 가져오기
                // Item 엔티티에 List로 담긴 이미지들을 가지고 옵니다.
                List<ItemImgEntity> itemImgList = findItem.getItemImgList();
                // 새로운 이미지 업로드
                List<ItemImgDTO> products = s3ItemImgUploaderService.upload("product", itemFiles);

                // 가지고 온 이미지가 비어있을 경우
                if(itemImgList.isEmpty()) {
                    for (int i = 0; i < products.size(); i++) {
                        ItemImgDTO itemImgDTO = products.get(i);
                        ItemImgEntity imgEntity = ItemImgEntity.builder()
                                .oriImgName(itemImgDTO.getOriImgName())
                                .uploadImgName(itemImgDTO.getUploadImgName())
                                .uploadImgPath(itemImgDTO.getUploadImgPath())
                                .uploadImgUrl(itemImgDTO.getUploadImgUrl())
                                .repImgYn(i == 0 ? "Y" : "N")
                                .item(findItem)
                                .build();

                        ItemImgEntity saveImg = itemImgRepository.save(imgEntity);
                        itemImgList.add(saveImg);
                    }
                } else {
                    // 가지고 온 이미지가 있는 경우
                    for (ItemImgEntity imgEntity : itemImgList) {
                        for (int i = 0; i < products.size(); i++) {
                            ItemImgDTO itemImgDTO = products.get(i);
                            ItemImgEntity itemImgEntity = ItemImgEntity.builder()
                                    .itemImgId(imgEntity.getItemImgId())
                                    .oriImgName(itemImgDTO.getOriImgName())
                                    .uploadImgPath(itemImgDTO.getUploadImgPath())
                                    .uploadImgUrl(itemImgDTO.getUploadImgUrl())
                                    .uploadImgName(itemImgDTO.getUploadImgName())
                                    .repImgYn(i == 0 ? "Y" : "N")
                                    .item(findItem)
                                    .build();

                            ItemImgEntity saveImg = itemImgRepository.save(itemImgEntity);
                            itemImgList.add(saveImg);
                        }
                    }
                }
                // 위에서 진행한 것은 ItemImg를 처리하고 저장하는 것이다.
                // 이제 상품에 List에 포함시켜서 저장해야한다.
                findItem = ItemEntity.builder()
                        .itemId(findItem.getItemId())
                        .itemName(itemDTO.getItemName())
                        .itemDetail(itemDTO.getItemDetail())
                        .itemPlace(itemDTO.getSellPlace())
                        .stockNumber(findItem.getStockNumber())
                        .price(itemDTO.getPrice())
                        .itemImgList(itemImgList)
                        .build();

                ItemEntity saveItem = itemRepository.save(findItem);
                ItemDTO toItemDTO = ItemDTO.toItemDTO(saveItem);
                return ResponseEntity.ok().body(toItemDTO);
            } else {
                return ResponseEntity.badRequest().body("이메일이 일치하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 상품 삭제
    @Override
    public String removeItem(Long itemId, String memberEmail) {
        // 상품 조회
        ItemEntity findItem = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        // 이미지 조회
        List<ItemImgEntity> findImg = itemImgRepository.findByItemItemId(itemId);
        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(memberEmail);

        if(findUser.getEmail().equals(findItem.getMember().getEmail())) {
            for(ItemImgEntity img : findImg) {
                String uploadFilePath = img.getUploadImgPath();
                String uuidFileName = img.getUploadImgName();

                // 상품 정보 삭제
                itemRepository.deleteByItemId(itemId);
                // DB에서 이미지 삭제
                itemImgRepository.deleteById(img.getItemImgId());
                // S3에서 삭제
                String result = s3ItemImgUploaderService.deleteFile(uploadFilePath, uuidFileName);
                log.info(result);
            }
        }else  {
            return "해당 유저의 게시글이 아닙니다.";
        }
        return "상품과 이미지를 삭제했습니다.";
    }

    // 전체 상품 보여주기
    @Transactional(readOnly = true)
    @Override
    public Page<ItemDTO> getItems(Pageable pageable) {
        Page<ItemEntity> allItem = itemRepository.findAll(pageable);
        // 각 아이템 엔티티를 ItemDTO로 변환합니다.
        // 이 변환은 ItemDTO::toItemDTO 메서드를 사용하여 수행됩니다.
        return allItem.map(ItemDTO::toItemDTO);
    }

    // 검색
    @Transactional(readOnly = true)
    @Override
    public Page<ItemDTO> getSearchItems(Pageable pageable,
                                        String searchKeyword) {
        Page<ItemEntity> searchItems = itemRepository.findByItemNameContaining(pageable, searchKeyword);
        return searchItems.map(ItemDTO::toItemDTO);
    }

}
