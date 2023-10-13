package com.example.shopping.entity.board;

import com.example.shopping.entity.Base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "board_img")
@Table
@Getter
@NoArgsConstructor
@ToString
public class BoardImgEntity extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "board_img_id")
    private Long boardImgId;
    private String uploadImgPath;
    private String uploadImgName;               // 이미지 파일명
    private String oriImgName;                  // 원본 이미지 파일명
    private String uploadImgUrl;                // 이미지 조회 경로
    private String repImgYn;                    // 대표 이미지 여부 Y면 대표이미지를 보여줌

    // 여기에서 cascade = CascadeType.ALL 이거를 적용하면
    // BoardEntity의 변경사항이 연관된 엔티티에 모두 전파됩니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @Builder
    public BoardImgEntity(Long boardImgId,
                          String uploadImgPath,
                          String uploadImgName,
                          String oriImgName,
                          String uploadImgUrl,
                          String repImgYn,
                          BoardEntity board) {
        this.boardImgId = boardImgId;
        this.uploadImgPath = uploadImgPath;
        this.uploadImgName = uploadImgName;
        this.oriImgName = oriImgName;
        this.uploadImgUrl = uploadImgUrl;
        this.repImgYn = repImgYn;
        this.board = board;
    }
}
