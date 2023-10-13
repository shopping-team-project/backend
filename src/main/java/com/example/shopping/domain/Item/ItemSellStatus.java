package com.example.shopping.domain.Item;

import io.swagger.v3.oas.annotations.media.Schema;

public enum ItemSellStatus {
    @Schema(description = "판매 중")
    SELL,
    @Schema(description = "예약")
    RESERVED,
    @Schema(description = "품절")
    SOLD_OUT
}
