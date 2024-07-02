package com.study.board.controller;

import com.study.board.dto.CommentRequestDto;
import com.study.board.dto.CommentResponseDto;
import com.study.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {

        this.commentService = commentService;
    }

    //댓글 조회
    //ResponseEntity는 응답코드를 같이 보내려고 쓴다.
    @GetMapping("/board/{boardId}/comments")
    public ResponseEntity<List<CommentResponseDto>> comments(@PathVariable Integer boardId) {
        //서비스에 위임
        List<CommentResponseDto> dto = commentService.comments(boardId);
        //결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    //댓글 생성
    @PostMapping("/board/{boardId}/comments")
    public ResponseEntity<CommentResponseDto> create(@PathVariable Integer boardId,
                                                     @RequestBody @Valid CommentRequestDto commentRequestDto,
                                                     BindingResult result) {

        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }

        //서비스에 위임
        CommentResponseDto createdDto = commentService.create(boardId, commentRequestDto);
        //결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(createdDto);
    }

    //댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable Integer commentId,
                                                     @RequestBody CommentRequestDto commentRequestDto,
                                                     HttpSession session) throws IllegalAccessException {

        //로그인한 사용자와 댓글 작성자가 같을 경우만 수정 가능
        if (session.getAttribute("LoginId").equals(commentRequestDto.getCommentWriter())) {
            //서비스에 위임
            CommentResponseDto updatedDto = commentService.update(commentId, commentRequestDto);
            //결과 응답
            return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
        } else {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }

    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> delete(@PathVariable Integer commentId,
                                                     HttpSession session,
                                                     @RequestBody CommentRequestDto commentRequestDto) throws IllegalAccessException {
        //로그인한 사용자와 댓글 작성자가 같을 경우 + 관리자만 삭제 가능
        if (session.getAttribute("LoginId").equals(commentRequestDto.getCommentWriter()) || session.getAttribute("LoginId").equals("admin")) {
            //서비스에 위임
            CommentResponseDto deletedDto = commentService.delete(commentId);
            //결과 응답
            return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
        } else {
            throw new IllegalAccessException("잘못된 접근입니다.");
        }
    }
}
