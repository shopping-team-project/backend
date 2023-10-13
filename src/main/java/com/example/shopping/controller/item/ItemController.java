package com.example.shopping.controller.item;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ModifyItemDTO;
import com.example.shopping.service.item.ItemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@Tag(name = "item", description = "상품 API")
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;

    // 상품 등록
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "item")
    @Operation(summary = "상품 등록", description = "상품을 등록하는 API입니다.")
    public ResponseEntity<?> createItem(@Validated @RequestBody ItemDTO itemDTO,
                                        @RequestPart(value = "files")List<MultipartFile>itemFiles,
                                        BindingResult result,
                                        @AuthenticationPrincipal UserDetails userDetails){
        try {
            if(result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            ResponseEntity<?> responseEntity = itemServiceImpl.saveItem(itemDTO, itemFiles, email);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 상품 상세 정보
    @GetMapping("/{itemId}")
    @Tag(name = "item")
    @Operation(summary = "상품 상세 정보 보기", description = "상품의 상세정보를 볼 수 있습니다.")
    public ResponseEntity<?> itemDetail(@PathVariable Long itemId) {
        try {
            ResponseEntity<ItemDTO> item = itemServiceImpl.getItem(itemId);
            log.info("item : " + item);
            return ResponseEntity.ok().body(item);
        } catch (EntityNotFoundException e) {
            log.error("존재하지 않는 상품입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 상품입니다.");
        }
    }

    // 상품 수정
    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "item")
    @Operation(summary = "상품 수정", description = "상품을 수정하는 API입니다.")
    public ResponseEntity<?> updateItem(@PathVariable Long itemId,
                                        @RequestBody ModifyItemDTO itemDTO,
                                        @RequestPart(value = "files") List<MultipartFile> itemFiles,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            ResponseEntity<?> responseEntity = itemServiceImpl.updateItem(itemId, itemDTO, itemFiles, email);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 상품 삭제
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "item")
    @Operation(summary = "상품 삭제", description = "상품을 삭제하는 API입니다.")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            String result = itemServiceImpl.removeItem(itemId, email);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
    }

    // 전체 상품 보여주기
    // 파라미터로 받는다.
    // 예) localhost:8080/api/v1/items?page=1&searchKeyword=내용
    @GetMapping("")
    @Tag(name = "item")
    @Operation(summary = "상품 전체", description = "모든 상품을 보여주는 API입니다.")
    public ResponseEntity<?> getItems(
            // SecuritConfig에 Page 설정을 한 페이지에 10개 보여주도록
            // 설정을 해서 여기서는 할 필요가 없다.
            @PageableDefault(sort = "itemId", direction = Sort.Direction.DESC)
            Pageable pageable,
            String searchKeyword) {
        try {
            // 검색하지 않을 때는 모든 글을 보여준다.
            Page<ItemDTO> items = null;
            if(searchKeyword == null) {
                items = itemServiceImpl.getItems(pageable);
            } else {
                // 검색할 때는 검색한 것을 보여줌
                items = itemServiceImpl.getSearchItems(pageable, searchKeyword);
            }
            Map<String, Object> response = new HashMap<>();
            // 현재 페이지의 아이템 목록
            response.put("items", items.getContent());
            // 현재 페이지 번호
            response.put("nowPageNumber", items.getNumber());
            // 전체 페이지 수
            response.put("totalPage", items.getTotalPages());
            // 한 페이지에 출력되는 데이터 개수
            response.put("pageSize", items.getSize());
            // 다음 페이지 존재 여부
            response.put("hasNextPage", items.hasNext());
            // 이전 페이지 존재 여부
            response.put("hasPreviousPage", items.hasPrevious());
            // 첫 번째 페이지 여부
            response.put("isFirstPage", items.isFirst());
            // 마지막 페이지 여부
            response.put("isLastPage", items.isLast());

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
