package com.example.shopping.entity.board;

import com.example.shopping.entity.Base.BaseEntity;
import com.example.shopping.entity.comment.CommentEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "board")
@Table
@ToString
@Getter
@NoArgsConstructor
public class BoardEntity extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long boardId;

    @Column(length = 300, nullable = false)
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    // 게시판 이미지
    // 게시글을 수정하면 게시글 속 이미지들도 함께 수정되어야 하기 때문에
    // 여기에 적용해야 합니다. 보통 게시물을 삭제해야 이미지가 삭제되므로
    // 게시물이 주축이기 때문에 여기에 cascade = CascadeType.ALL을 추가
    // orphanRemoval = true도 게시글을 삭제하면
    // 이미지 엔티티를 고아객체가 되므로 삭제되게 여기에 추가한다.
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("boardImgId asc")
    private List<BoardImgEntity> boardImgDTOList = new ArrayList<>();

    // 댓글
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("commentId asc ")
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @Builder
    public BoardEntity(Long boardId,
                       String title,
                       String content,
                       MemberEntity member,
                       List<BoardImgEntity> boardImgDTOList,
                       List<CommentEntity> commentEntityList) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.member = member;
        this.boardImgDTOList = boardImgDTOList;
        this.commentEntityList = commentEntityList;
    }
}
