package com.study.board.service;

import com.study.board.dto.BoardResponseDto;
import com.study.board.dto.CommentResponseDto;
import com.study.board.dto.MemberResponseDto;
import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.entity.Member;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import com.study.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private MemberRepository memberRepository;
    private BoardRepository boardRepository;
    private CommentRepository commentRepository;

    @Autowired
    public AdminService(MemberRepository memberRepository, BoardRepository boardRepository, CommentRepository commentRepository) {

        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    //전체회원 리스트
    public Page<MemberResponseDto> memberList(Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        Page<Member> all = memberRepository.findAll(pageable);

        Page<MemberResponseDto> dto = new MemberResponseDto().toDtoList(all);

        return dto;
    }

    //회원 검색기능
    public Page<MemberResponseDto> memberSearchList(Integer searchOption, String searchKeyword, Pageable pageable) {

        Page<Member> searchContaining = null;

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        //ID로 검색
        if(searchOption == 1) {
            searchContaining = memberRepository.findByLoginIdContaining(searchKeyword, pageable);
        } else { //이메일로 검색
            searchContaining = memberRepository.findByEmailContaining(searchKeyword, pageable);
        }

        Page<MemberResponseDto> dto = new MemberResponseDto().toDtoList(searchContaining);

        return dto;
    }

    //회원 계정 삭제
    public void deleteMember(Integer memberId) {

        memberRepository.deleteById(memberId);
    }

    //게시글 관리
    public Page<BoardResponseDto> boardList(Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        Page<Board> all = boardRepository.findAll(pageable); //List<Board>의 Board라는 클래스를 반환

        Page<BoardResponseDto> dto = new BoardResponseDto().toDtoList(all);

        return dto;
    }

    //게시글 관리 + 검색기능
    public Page<BoardResponseDto> boardSearchList(Integer searchOption, String searchKeyword, Pageable pageable) {

        Page<Board> searchContaining = null;

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        //작성자로 검색
        if(searchOption == 1) {
            searchContaining = boardRepository.findByWriterContaining(searchKeyword, pageable);
        } else { //제목으로 검색
            searchContaining = boardRepository.findByTitleContaining(searchKeyword, pageable);
        }

        Page<BoardResponseDto> dto = new BoardResponseDto().toDtoList(searchContaining);

        return dto;
    }

    //게시물 삭제
    public void deletePost(Integer boardId) {

        boardRepository.deleteById(boardId);
    }

    //댓글 관리
    public Page<CommentResponseDto> commentList(Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        Page<Comment> all = commentRepository.findAll(pageable);

        Page<CommentResponseDto> dto = new CommentResponseDto().toDtoList(all);

        return dto;
    }

    //댓글 관리 + 검색기능
    public Page<CommentResponseDto> commentSearchList(Integer searchOption, String searchKeyword, Pageable pageable) {

        Page<Comment> searchContaining = null;

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        //작성자로 검색
        if(searchOption == 1) {
            searchContaining = commentRepository.findByCommentWriterContaining(searchKeyword, pageable);
        } else { //제목으로 검색
            searchContaining = commentRepository.findByCommentContentContaining(searchKeyword, pageable);
        }

        Page<CommentResponseDto> dto = new CommentResponseDto().toDtoList(searchContaining);

        return dto;
    }


    //댓글 삭제
    public void deleteComment(Integer commentId) {

        commentRepository.deleteById(commentId);
    }
}
