package com.study.board.service;

import com.study.board.dto.BoardRequestDto;
import com.study.board.dto.BoardResponseDto;
import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BoardService {

    private BoardRepository boardRepository;
    private CommentRepository commentRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, CommentRepository commentRepository) {

        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    //글 작성 처리
    public void write(BoardRequestDto boardRequestDto,
                      MultipartFile file,
                      HttpSession session) throws IOException {
        //저장할 경로 지정함
        String projectPath = System.getProperty("user.dir") //user.dir: 현재 사용자의 작업 디렉토리
                + "/src/main/resources/static/files";

        String filename = null;

        List<Comment> commentList = commentRepository.findByBoardId(boardRequestDto.getId());

        if (file != null && !file.isEmpty()) {
            UUID uuid = UUID.randomUUID();

            filename = uuid + "_" + file.getOriginalFilename();

            //파라미터로 들어온 file을 넣어줄 껍데기를 생성한다.
            File saveFile = new File(projectPath, filename);

            file.transferTo(saveFile); //업로드한 파일을 지정된 위치로 이동

            boardRequestDto.setFilename(filename);

            boardRequestDto.setFilepath("/files/" + filename);


        }

        boardRequestDto.setCreated(LocalDateTime.now());
        boardRequestDto.setWriter(session.getAttribute("LoginId").toString());

        boardRepository.save(boardRequestDto.toEntity(boardRequestDto, commentList));

    }

    //게시글 리스트
    public Page<BoardResponseDto> boardList(Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        Page<Board> all = boardRepository.findAll(pageable); //List<Board>의 Board라는 클래스를 반환

        Page<BoardResponseDto> dto = new BoardResponseDto().toDtoList(all);

        return dto;
    }

    //게시글 리스트 + 검색기능
    public Page<BoardResponseDto> boardSearchList(Integer searchOption, String searchKeyword, Pageable pageable) {

        Page<Board> searchContaining = null;

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        if(searchOption == 1) {
            searchContaining = boardRepository.findByWriterContaining(searchKeyword, pageable);
        } else {
            searchContaining = boardRepository.findByTitleContaining(searchKeyword, pageable);
        }

        Page<BoardResponseDto> dto = new BoardResponseDto().toDtoList(searchContaining);

        return dto;
    }

    //게시글 상세보기
    public BoardResponseDto boardView(Integer id) {

        Board board = boardRepository.findById(id).get(); //get()을 통해 Optional에서 실제 객체를 가져온다.

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .filename(board.getFilename())
                .filepath(board.getFilepath())
                .created(board.getCreated())
                .writer(board.getWriter())
                .build();

        return boardResponseDto;
    }

    //게시글 삭제
    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }

}
