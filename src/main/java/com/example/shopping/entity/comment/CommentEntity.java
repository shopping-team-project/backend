package com.example.shopping.entity.comment;

import com.example.shopping.entity.Base.BaseEntity;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "comment")
@Table
@ToString
@Getter
@NoArgsConstructor
public class CommentEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @Builder
    public CommentEntity(Long commentId, String comment, MemberEntity member, BoardEntity board) {
        this.commentId = commentId;
        this.comment = comment;
        this.member = member;
        this.board = board;
    }
}
