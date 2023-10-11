package com.example.shopping.domain.cart;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.entity.cart.CartItemEntity;
import com.example.shopping.repository.item.ItemRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;

@ToString
@Getter
@NoArgsConstructor
public class CartItemDTO {

    private  ItemRepository itemRepository;

    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상 담아주세요")
    private int count;

    @Builder
    public CartItemDTO(Long itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

    public CartItemEntity toEntity(){
        return CartItemEntity.builder()
                .item(itemRepository.findById(this.itemId).orElseThrow())
                .count(this.count)
                .build();
    }

    public void addCount(int cnt){
        this.count += cnt;
    }
}
