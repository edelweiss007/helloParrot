package com.study.board.service;

import com.study.board.dto.CommentRequestDto;
import com.study.board.dto.CommentResponseDto;
import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private BoardRepository boardRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    public List<CommentResponseDto> comments(Integer boardId) {
        //댓글조회
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        //엔티티 -> DTO 변환
        List<CommentResponseDto> commentResponseDto = comments.stream() //댓글 엔티티 목록을 스트림으로 변환
                .map(comment -> CommentResponseDto.toDto (comment)) //엔티티를 DTO로 매핑
                .collect(Collectors.toList()); //스트림을 리스트로 변환
        //결과 반환
        return commentResponseDto;

    }

    @Transactional
    //create() 메서드는 DB내용을 바꾸기 때문에 실패할 경우를 대비해 @Transactional을 추가해서 문제가 발생했을 때 롤백하게 한다.
    public CommentResponseDto create(Integer boardId, CommentRequestDto commentRequestDto) {

        //부모 게시글을 조회해서 가져오고 없을 경우 예외 발생시키기
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! " +
                        "대상 게시글이 존재하지 않습니다."));
        //댓글 엔티티 생성
        Comment comment = Comment.createComment(commentRequestDto, board);
        //댓글 엔티티를 DB에 저장
        Comment created = commentRepository.save(comment);
        //DTO로 변환해 반환
        CommentResponseDto commentResponseDto = CommentResponseDto.toDto(created);

        return commentResponseDto;
    }

    @Transactional
    public CommentResponseDto update(Integer commentId, CommentRequestDto commentRequestDto) {

        //해당 댓글을 조회해서 없을 경우 예외 발생
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패!" +
                        "대상 댓글이 없습니다."));
        //댓글이 존재할 경우 수정
        comment.patch(commentRequestDto);
        //수정한 댓글을 DB에 덮어쓰기
        Comment updated = commentRepository.save(comment);
        //DB에 덮어쓰기된 Entity를 DTO로 변환해서
        CommentResponseDto commentResponseDto = CommentResponseDto.toDto(updated);
        //반환하기
        return commentResponseDto;
    }

    @Transactional
    public CommentResponseDto delete(Integer commentId) {

        //해당 댓글을 조회해 가져오고 없을 경우 예외 발생
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패! " +
                        "대상이 없습니다."));
        //댓글이 존재할 경우 댓글 삭제
        commentRepository.delete(comment);
        //삭제한 댓글 Entity를 DTO로 변환해서
        CommentResponseDto commentResponseDto = CommentResponseDto.toDto(comment);
        //반환하기
        return commentResponseDto;
    }
}
