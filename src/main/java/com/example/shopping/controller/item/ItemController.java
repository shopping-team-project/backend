package com.example.shopping.controller.item;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    private final ItemService itemService;

    // 상품 등록
    @PostMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
            ResponseEntity<?> responseEntity = itemService.saveItem(itemDTO, itemFiles, email);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 상품 상세 정보
    @GetMapping("/{itemId}")
    public ResponseEntity<?> itemDetail(@PathVariable Long itemId) {
        try {
            ResponseEntity<ItemDTO> item = itemService.getItem(itemId);
            log.info("item : " + item);
            return ResponseEntity.ok().body(item);
        } catch (EntityNotFoundException e) {
            log.error("존재하지 않는 상품입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 상품입니다.");
        }
    }

    // 상품 수정
    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateItem(@PathVariable Long itemId,
                                        @RequestBody ItemDTO itemDTO,
                                        @RequestPart(value = "files") List<MultipartFile> itemFiles,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            ResponseEntity<?> responseEntity = itemService.updateItem(itemId, itemDTO, itemFiles, email);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 상품 삭제
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            String result = itemService.removeItem(itemId, email);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
    }

}
