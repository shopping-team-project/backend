package com.example.shopping.domain.Item;

import com.example.shopping.entity.item.ItemImgEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ItemImgDTO {
    @Schema(description = "상품 이미지 번호")
    private Long itemImgId;

    @Schema(description = "상품 업로드 이름")
    private String uploadImgName;

    @Schema(description = "원본 상품 이름")
    private String oriImgName;

    @Schema(description = "업로드 이미지 URL")
    private String uploadImgUrl;

    @Schema(description = "업로드 이미지 Path")
    private String uploadImgPath;

    @Schema(description = "대표 이미지 여부")
    private String repImgYn;

    @Schema(description = "상품 정보")
    private ItemDTO item;

    @Builder
    public ItemImgDTO(Long itemImgId,
                      String uploadImgName,
                      String oriImgName,
                      String uploadImgUrl,
                      String uploadImgPath,
                      String repImgYn,
                      ItemDTO item) {
        this.itemImgId = itemImgId;
        this.uploadImgName = uploadImgName;
        this.oriImgName = oriImgName;
        this.uploadImgUrl = uploadImgUrl;
        this.uploadImgPath = uploadImgPath;
        this.repImgYn = repImgYn;
        this.item = item;
    }

    public static ItemImgDTO toItemImgDTO(ItemImgEntity itemImgEntity) {
        return ItemImgDTO.builder()
                .itemImgId(itemImgEntity.getItemImgId())
                .oriImgName(itemImgEntity.getOriImgName())
                .uploadImgName(itemImgEntity.getUploadImgName())
                .uploadImgUrl(itemImgEntity.getUploadImgUrl())
                .uploadImgPath(itemImgEntity.getUploadImgPath())
                .repImgYn(itemImgEntity.getRepImgYn())
                .build();
    }
}
