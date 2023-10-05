package com.example.shopping.repository.item;

import com.example.shopping.entity.item.ItemImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemImgRepository extends JpaRepository<ItemImgEntity, Long> {
    //  ItemImgRepository의 findByItemId 메서드에서 ItemEntity의 ID 필드(itemId)를 참조
    // ItemEntity의 ID를 어떻게 찾아야 하는지 명확하게 지정하려고 Item다음에 ItemId를 진행한것이다.
    List<ItemImgEntity> findByItemItemId(Long itemId);
}
