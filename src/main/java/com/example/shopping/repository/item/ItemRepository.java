package com.example.shopping.repository.item;

import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.entity.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    ItemEntity deleteByItemId(Long itemId);

    // 페이지 처리를 위해서
    Page<ItemEntity> findAll(Pageable pageable);
    // 검색
    Page<ItemEntity> findByItemNameContaining(Pageable pageable, String searchKeyword);

    ItemEntity findByItemId(Long itemId);
    Page<ItemEntity> findByItemSellStatus(Pageable pageable, ItemSellStatus itemSellStatus);

}
