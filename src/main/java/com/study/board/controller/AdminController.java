package com.study.board.controller;

import com.study.board.dto.BoardResponseDto;
import com.study.board.dto.CommentResponseDto;
import com.study.board.dto.MemberResponseDto;
import com.study.board.enums.LoginStatus;
import com.study.board.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {

        this.adminService = adminService;
    }

    //회원 리스트
    @GetMapping
    public String memberList(Model model,
                             @PageableDefault(page = 0, size = 10, sort = "memberId", direction = Sort.Direction.DESC) Pageable pageable,
                             Integer searchOption,
                             String searchKeyword,
                             HttpSession session) {

        Page<MemberResponseDto> list = null;

        Object LoginId = session.getAttribute("LoginId");

        //검색어가 비어있을 경우에는 모든 회원 리스트를 보여준다.
        if (searchOption == null) {
            list = adminService.memberList(pageable);
        } else {
            list = adminService.memberSearchList(searchOption, searchKeyword, pageable);
        }

        if (LoginId != null) {
            model.addAttribute("login", LoginStatus.SUCCESS.getStatus());
        } else {
            model.addAttribute("login", LoginStatus.NOT_AVAILABLE.getStatus());
        }

        model.addAttribute("list", list);

        return "/admin/memberList";
    }


    //회원 계정 삭제
    @ResponseBody
    @GetMapping("/deleteMember/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable String id) {

        try {
            Integer memberId = Integer.parseInt(id);

            adminService.deleteMember(memberId);

            return ResponseEntity.status(HttpStatus.OK).body(null);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //게시글 관리
    @GetMapping("/board")                                                     //어떤 기준으로 정렬?  정렬을 어떻게? 역순으로
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            Integer searchOption,
                            String searchKeyword,
                            HttpSession session
    ) {

        Page<BoardResponseDto> list = null;

        Object LoginId = session.getAttribute("LoginId");

        if (searchOption == null) {
            list = adminService.boardList(pageable);
        } else {
            list = adminService.boardSearchList(searchOption, searchKeyword, pageable);
        }

        if (LoginId != null) {
            model.addAttribute("login", LoginStatus.SUCCESS.getStatus());
        } else {
            model.addAttribute("login", LoginStatus.NOT_AVAILABLE.getStatus());
        }

        model.addAttribute("list", list);

        return "/admin/board";
    }

    //게시글 삭제
    @ResponseBody
    @GetMapping("/deletePost/{id}")
    public ResponseEntity<String> deletePost(@PathVariable String id) {

        try {
            Integer boardId = Integer.parseInt(id);

            adminService.deletePost(boardId);

            return ResponseEntity.status(HttpStatus.OK).body(null);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //댓글 관리
    @GetMapping("/comments")
    public String commentList(Model model,
                              @PageableDefault(page = 0, size = 10, sort = "commentId", direction = Sort.Direction.DESC) Pageable pageable,
                              Integer searchOption,
                              String searchKeyword,
                              HttpSession session
    ) {

        Page<CommentResponseDto> list = null;

        Object LoginId = session.getAttribute("LoginId");

        if (searchOption == null) {
            list = adminService.commentList(pageable);
        } else {
            list = adminService.commentSearchList(searchOption, searchKeyword, pageable);
        }

        if (LoginId != null) {
            model.addAttribute("login", LoginStatus.SUCCESS.getStatus());
        } else {
            model.addAttribute("login", LoginStatus.NOT_AVAILABLE.getStatus());
        }

        model.addAttribute("list", list);

        return "/admin/comments";
    }

    //댓글 삭제
    @ResponseBody
    @GetMapping("/deleteComment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable String id) {

        try {
            Integer commentId = Integer.parseInt(id);

            adminService.deleteComment(commentId);

            return ResponseEntity.status(HttpStatus.OK).body(null);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}
