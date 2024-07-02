package com.study.board.dto;

import com.study.board.entity.Board;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BoardResponseDto {

    private int id;

    private String title;

    private String content;

    private String filename;

    private String filepath;

    private LocalDateTime created;

    private String writer;

    public Board toEntity(BoardResponseDto boardResponseDto) {

        return Board.builder()
                .id(boardResponseDto.getId())
                .title(boardResponseDto.getTitle())
                .content(boardResponseDto.getContent())
                .filename(boardResponseDto.getFilename())
                .filepath(boardResponseDto.getFilepath())
                .created(boardResponseDto.getCreated())
                .writer(boardResponseDto.getWriter())
                .build();
    }


    //Page<Entity> -> Page<Dto> 변환처리
    public Page<BoardResponseDto> toDtoList(Page<Board> boardList){

        Page<BoardResponseDto> boardDtoList = boardList.map(board -> BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .filename(board.getFilename())
                .filepath(board.getFilepath())
                .created(board.getCreated())
                .writer(board.getWriter())
                .build());

        return boardDtoList;
    }
}


