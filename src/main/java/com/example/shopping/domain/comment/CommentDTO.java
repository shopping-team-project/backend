package com.example.shopping.domain.comment;

import com.example.shopping.entity.comment.CommentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class CommentDTO {
    @Schema(description = "댓글 번호")
    private Long commentId;
    @Schema(description = "댓글")
    private String comment;
    @Schema(description = "댓글 등록 시간")
    private LocalDateTime writeTime;
    @Schema(description = "닉네임")
    private String nickName;

    @Builder
    public CommentDTO(Long commentId, String comment, LocalDateTime writeTime, String nickName) {
        this.commentId = commentId;
        this.comment = comment;
        this.writeTime = writeTime;
        this.nickName = nickName;
    }

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        return CommentDTO.builder()
                .commentId(commentEntity.getCommentId())
                .comment(commentEntity.getComment())
                .writeTime(LocalDateTime.now())
                .nickName(commentEntity.getMember().getNickName())
                .build();
    }
}
