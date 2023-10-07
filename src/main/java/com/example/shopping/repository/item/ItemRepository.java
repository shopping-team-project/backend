package com.example.shopping.repository.item;

import com.example.shopping.entity.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    ItemEntity deleteByItemId(Long itemId);

    // 페이지 처리를 위해서
    Page<ItemEntity> findAll(Pageable pageable);
    // 검색
    Page<ItemEntity> findByItemNameContaining(Pageable pageable, String searchKeyword);
}
