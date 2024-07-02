package com.study.board.dto;

import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
//@ToString //모든 필드를 출력할 수 있는 toString 메서드를 자동생성
public class CommentResponseDto {

    //댓글 번호
    private Integer commentId;
    //게시글 번호
    private Integer boardId;

    private String commentWriter;

    private String commentContent;

    private LocalDateTime commentCreated;

    private LocalDateTime commentUpdated;

    //Entity를 DTO로 변환
    public static CommentResponseDto toDto(Comment comment) {

        return new CommentResponseDto(
                comment.getCommentId(),
                comment.getBoard().getId(),
                comment.getCommentWriter(),
                comment.getCommentContent(),
                comment.getCommentCreated(),
                comment.getCommentUpdated()
        );
    }

    //Page<Entity> -> Page<Dto> 변환처리
    public Page<CommentResponseDto> toDtoList(Page<Comment> commentList){

        Page<CommentResponseDto> commentDtoList = commentList.map(comment -> CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .boardId(comment.getBoard().getId())
                .commentWriter(comment.getCommentWriter())
                .commentContent(comment.getCommentContent())
                .commentCreated(comment.getCommentCreated())
                .commentUpdated(comment.getCommentUpdated())
                .build());

        return commentDtoList;
    }
}
