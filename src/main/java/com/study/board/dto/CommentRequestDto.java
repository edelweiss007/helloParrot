package com.study.board.dto;

import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentRequestDto {

    //댓글 번호
    private Integer commentId;
    //게시글 번호
    @NotNull
    private Integer boardId;

    @NotNull
    private String commentWriter;

    @NotBlank(message = "내용을 입력해 주세요.")
    @Size(max = 300, message = "300자 이하로 입력해 주세요.")
    private String commentContent;

    private LocalDateTime commentCreated;

    private LocalDateTime commentUpdated;


    public Board toEntity(BoardRequestDto boardRequestDto) {

        return Board.builder()
                .id(boardRequestDto.getId())
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .filename(boardRequestDto.getFilename())
                .filepath(boardRequestDto.getFilepath())
                .created(boardRequestDto.getCreated())
                .writer(boardRequestDto.getWriter())
                .build();

    }
}
