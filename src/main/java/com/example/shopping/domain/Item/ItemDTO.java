package com.example.shopping.domain.Item;

import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
public class ItemDTO {
    @Schema(description = "상품 번호")
    private Long itemId;

    @Schema(description = "상품 이름")
    @NotBlank(message = "상품명은 필수 입력입니다.")
    private String itemName;     // 상품 명

    @Schema(description = "상품 가격")
    @NotNull(message = "가격은 필수 입력입니다.")
    private int price;          // 가격

    @Schema(description = "상품 설명")
    @NotNull(message = "설명은 필수 입력입니다.")
    private String itemDetail;  // 상품 상세 설명

    @Schema(description = "상품 상태")
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    @Schema(description = "상품 등록 시간")
    private LocalDateTime regTime;

    @Schema(description = "회원 닉네임")
    @NotNull(message = "닉네임은 필수로 입력해야합니다.")
    private String memberNickName;

    @Schema(description = "상품 재고 수량")
    @NotNull(message = "재고 수량은 필수 입력입니다.")
    private int stockNumber;    // 재고수량

    @Schema(description = "판매지역")
    @NotNull(message = "판매지역을 입력해야 합니다.")
    private String sellPlace;

    @Schema(description = "상품 이미지")
    // 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트
    private List<ItemImgDTO> itemImgList = new ArrayList<>();

    @Builder
    public ItemDTO(Long itemId,
                   String itemName,
                   int price,
                   String itemDetail,
                   ItemSellStatus itemSellStatus,
                   LocalDateTime regTime,
                   String memberNickName,
                   int stockNumber,
                   String sellPlace,
                   List<ItemImgDTO> itemImgList) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.regTime = regTime;
        this.memberNickName = memberNickName;
        this.stockNumber = stockNumber;
        this.sellPlace = sellPlace;
        this.itemImgList = itemImgList;
    }

    public static ItemDTO toItemDTO(ItemEntity item) {
        List<ItemImgEntity> itemImgEntities = item.getItemImgList();
        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

        for(ItemImgEntity itemImg : itemImgEntities) {
            ItemImgDTO itemImgDTO = ItemImgDTO.toItemImgDTO(itemImg);
            itemImgDTOList.add(itemImgDTO);
        }
        return ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .itemDetail(item.getItemDetail())
                .itemSellStatus(item.getItemSellStatus())
                .regTime(LocalDateTime.now())
                .memberNickName(item.getMember().getNickName())
                .sellPlace(item.getItemPlace())
                .stockNumber(item.getStockNumber())
                .itemImgList(itemImgDTOList)
                .build();
    }
}
