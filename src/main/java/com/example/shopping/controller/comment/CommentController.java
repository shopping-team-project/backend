package com.example.shopping.controller.comment;

import com.example.shopping.domain.comment.CommentDTO;
import com.example.shopping.domain.comment.ModifyCommentDTO;
import com.example.shopping.service.comment.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@Log4j2
@Tag(name = "comment", description = "댓글 API")
public class CommentController {
    private final CommentServiceImpl commentService;

    // 댓글 작성
    @PostMapping("/{boardId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "comment")
    @Operation(summary = "댓글 등록", description = "댓글을 등록하는 API입니다.")
    public ResponseEntity<?> saveComment(@PathVariable Long boardId,
                                         @RequestBody CommentDTO commentDTO,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        log.info("email : " + email);
        ResponseEntity<?> save = commentService.save(boardId, commentDTO, email);
        return ResponseEntity.ok().body(save);
    }

    // 댓글 삭제
    @DeleteMapping("/{boardId}/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "comment")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제하는 API입니다.")
    public String removeComment(@PathVariable Long boardId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        log.info("email : " + email);
        return commentService.remove(boardId, commentId, email);
    }

    // 댓글 수정
    @PutMapping("/{boardId}/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Tag(name = "comment")
    @Operation(summary = "댓글 수정", description = "댓글을 수정하는 API입니다.")
    public ResponseEntity<?> updateComment(@PathVariable Long boardId,
                                           @PathVariable Long commentId,
                                           @RequestBody ModifyCommentDTO commentDTO,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        log.info("email : " + email);
        ResponseEntity<?> update = commentService.update(boardId, commentId, commentDTO, email);
        return update;
    }
}
