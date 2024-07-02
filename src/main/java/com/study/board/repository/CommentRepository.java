package com.study.board.repository;

import com.study.board.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByBoardId(Integer boardId);

    Page<Comment> findByCommentWriterContaining(String searchKeyword, Pageable pageable);

    Page<Comment> findByCommentContentContaining(String searchKeyword, Pageable pageable);

}
