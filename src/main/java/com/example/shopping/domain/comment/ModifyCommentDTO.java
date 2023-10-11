package com.example.shopping.domain.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ModifyCommentDTO {
    @Schema(description = "댓글")
    private String comment;

    @Builder
    public ModifyCommentDTO(String comment) {
        this.comment = comment;
    }
}
