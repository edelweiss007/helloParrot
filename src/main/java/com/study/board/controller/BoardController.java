package com.study.board.controller;

import com.study.board.dto.BoardRequestDto;
import com.study.board.dto.BoardResponseDto;
import com.study.board.enums.LoginStatus;
import com.study.board.service.BoardService;
import com.study.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;

    private CommentService commentService;

    @Autowired
    public BoardController(BoardService boardService, CommentService commentService) {

        this.boardService = boardService;
        this.commentService = commentService;
    }

    //게시글 작성 폼
    @GetMapping("/write")
    public String boardWriteForm(Model model) {

        model.addAttribute("login", LoginStatus.SUCCESS.getStatus());

        model.addAttribute("boardRequestDto", new BoardRequestDto());

        return "/board/boardWrite";
    }

    //게시글 등록 처리
    @PostMapping("/write")
    public String boardWriteProcess(@Valid BoardRequestDto boardRequestDto,
                                    BindingResult result,
                                    Model model,
                                    MultipartFile file,
                                    HttpSession session) throws IOException {

        if(result.hasErrors()) {

            return "/board/boardWrite";
        }

        boardService.write(boardRequestDto, file, session);

        model.addAttribute("write", "글 작성이 완료되었습니다! :)");
        model.addAttribute("url", "/board");

        return "/board/messages";
    }

    //게시글 리스트
    @GetMapping                                                    //어떤 기준으로 정렬?  정렬을 어떻게? 역순으로
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            Integer searchOption,
                            String searchKeyword,
                            HttpSession session
                            ) {

        Page<BoardResponseDto> list = null;

        Object LoginId = session.getAttribute("LoginId");

        if(searchOption == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchOption, searchKeyword, pageable);
        }

        if(LoginId != null) {
            model.addAttribute("login", LoginStatus.SUCCESS.getStatus());
        } else {
            model.addAttribute("login", LoginStatus.NOT_AVAILABLE.getStatus());
        }

        model.addAttribute("list", list);

        return "/board/boardList";
    }

    //게시글 상세보기
    @GetMapping("/{id}")
    public String boardView(Model model, @PathVariable Integer id) {

        model.addAttribute("login", LoginStatus.SUCCESS.getStatus());

        model.addAttribute("boardView", boardService.boardView(id));
        model.addAttribute("commentResponseDto", commentService.comments(id));

        return "/board/boardView";
    }

    //게시글 삭제
    @GetMapping("/delete/{id}")
    public String boardDelete(@PathVariable Integer id, HttpSession session, Model model) {

        String loginId = (String)session.getAttribute("LoginId");

        String writer = boardService.boardView(id).getWriter();

        //글 작성자나 관리자만 삭제 가능
        if(loginId.equals(writer) || loginId.equals("admin")) {

            boardService.boardDelete(id);

            return "redirect:/board";

        } else {

            model.addAttribute("errorMessage", "잘못된 접근입니다.");

            return "/board/errorPage";
        }
    }

    //게시글 수정 폼
    @GetMapping("/{id}/updateForm")
    public String boardModify(@PathVariable Integer id, Model model, HttpSession session) {

        //로그인한 사용자의 아이디
        String LoginId = (String)session.getAttribute("LoginId");
        //글쓴이 아이디
        String writer = boardService.boardView(id).getWriter();

        if(LoginId.equals(writer)) {

            model.addAttribute("login", LoginStatus.SUCCESS.getStatus());

            model.addAttribute("boardRequestDto", boardService.boardView(id));

            return "/board/boardModify";

        } else {

            model.addAttribute("errorMessage", "잘못된 접근입니다.");

            return "/board/errorPage";
        }


    }

    //게시글 수정 처리
    @PostMapping("/{id}")               //수정할 내용 받아옴
    public String boardUpdate(@PathVariable Integer id,
                              @Valid BoardRequestDto boardRequestDto, //새 내용을 가지고옴
                              BindingResult result,
                              Model model,
                              MultipartFile file,
                              HttpSession session) throws IOException {

        if(result.hasErrors()) {

            model.addAttribute("boardRequestDto", boardRequestDto);

            return "/board/boardModify";
        }

        BoardResponseDto boardTemp = boardService.boardView(id); //기존의 정보를 가져와서

        //게시물 id를 알아야 저장 가능하다.
        boardRequestDto.setId(boardTemp.getId());

        if(boardRequestDto.getIsFileDeleted().equals("no")) {

            //파일을 수정하지 않았을 경우 예전 파일정보를 그대로 다시 저장해야한다.
            boardRequestDto.setFilename(boardTemp.getFilename());
            boardRequestDto.setFilepath(boardTemp.getFilepath());
        } else {
            //사진을 지웠을 경우
            boardRequestDto.setFilename(null);
            boardRequestDto.setFilepath(null);
        }

        boardService.write(boardRequestDto, file, session);


        model.addAttribute("modify", "수정이 완료되었습니다! :)");
        model.addAttribute("url", "/board/" + id);

        return "/board/messages";
    }
}

















