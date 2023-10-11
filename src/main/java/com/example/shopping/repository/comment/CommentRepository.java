package com.example.shopping.repository.comment;

import com.example.shopping.entity.comment.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    void deleteByBoardBoardId(Long boardId);
}
