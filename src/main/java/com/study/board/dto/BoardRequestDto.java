package com.study.board.dto;

import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardRequestDto {

    @NotNull
    private int id;

    //" "(공백)는 허용하되 null과 ""(빈 문자열)는 허용하지 않음
    @NotEmpty(message = "제목을 입력해 주세요.")
    @Size(max = 45, message = "45자 이하로 입력해 주세요.")
    private String title;

    @NotEmpty(message = "내용을 입력해 주세요.")
    @Size(max = 300, message = "300자 이하로 입력해 주세요.")
    private String content;

    private String filename;

    private String filepath;

    private LocalDateTime created;

    private String writer;

    private String isFileDeleted;

    public Board toEntity(BoardRequestDto boardRequestDto, List<Comment> commentList) {

        return Board.builder()
                .id(boardRequestDto.getId())
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .filename(boardRequestDto.getFilename())
                .filepath(boardRequestDto.getFilepath())
                .created(boardRequestDto.getCreated())
                .writer(boardRequestDto.getWriter())
                .commentList(commentList)
                .build();

    }

}
