package com.example.shopping.service.comment;

import com.example.shopping.domain.comment.CommentDTO;
import com.example.shopping.domain.comment.ModifyCommentDTO;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    // 댓글 작성
    ResponseEntity<?> save(Long boardId,
                           CommentDTO commentDTO,
                           String memberEmail);

    // 댓글 삭제
    String remove(Long boardId,
                  Long commentId,
                  String memberEmail);

    // 댓글 수정
    ResponseEntity<?> update(Long boardId,
                             Long commentId,
                             ModifyCommentDTO commentDTO,
                             String memberEmail);
}
