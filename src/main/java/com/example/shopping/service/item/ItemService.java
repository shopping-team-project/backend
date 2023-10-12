package com.example.shopping.service.item;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ModifyItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    // 상품 등록
    ResponseEntity<?> saveItem(ItemDTO itemDTO,
                               List<MultipartFile> itemFiles,
                               String memberEmail) throws Exception;
    // 상품 상세 정보
    ResponseEntity<ItemDTO> getItem(Long itemId);

    // 상품 수정
    ResponseEntity<?> updateItem(Long itemId,
                                 ModifyItemDTO itemDTO,
                                 List<MultipartFile> itemFiles,
                                 String memberEmail) throws Exception;

    // 상품 삭제
    String removeItem(Long itemId, String memberEmail);
    // 전체 상품 보기
    Page<ItemDTO> getItems(Pageable pageable);
    // 검색
    Page<ItemDTO> getSearchItems(Pageable pageable,
                                 String searchKeyword);
}
