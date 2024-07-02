package com.study.board.entity;

import com.study.board.dto.CommentRequestDto;
import com.study.board.dto.CommentResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity //자바의 영속성 JPA에서 이 클래스가 DB에 있는 table을 의미
@Getter
@ToString //모든 필드를 출력할 수 있는 toString 메서드 자동생성
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    private String commentWriter;

    private String commentContent;

    /* Board:Comment = 1:N */
    @ManyToOne(fetch = FetchType.LAZY) //댓글이 기준
    @JoinColumn(name = "boardId")
    private Board board;

    private LocalDateTime commentCreated;

    private LocalDateTime commentUpdated;

    public static Comment createComment(CommentRequestDto commentRequestDto, Board board) {
        //예외 발생
        if (commentRequestDto.getCommentId() != null) {
            throw new IllegalArgumentException("댓글 생성에 실패하였습니다. 댓글의 id가 없어야 합니다.");
        } else if (!commentRequestDto.getBoardId().equals(board.getId())) {
            throw new IllegalArgumentException("댓글 생성에 실패하였습니다. 게시글의 id가 잘못됐습니다.");
        } else {         //예외가 발생하지 않을 시 엔티티 생성 및 반환

            commentRequestDto.setCommentCreated(LocalDateTime.now());
            commentRequestDto.setCommentUpdated(LocalDateTime.now());

            Comment comment = new Comment(
                    commentRequestDto.getCommentId(),
                    commentRequestDto.getCommentWriter(),
                    commentRequestDto.getCommentContent(),
                    board,
                    commentRequestDto.getCommentCreated(),
                    commentRequestDto.getCommentUpdated()
            );
            return comment;
        }

    }

    public void patch(CommentRequestDto commentRequestDto) {
        //url의 commentId와 json 데이터의 commentId가 다른 경우 예외 발생
        if(this.commentId != commentRequestDto.getCommentId()) {
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 commentId가 입력되었습니다.");
        }
        //예외가 발생하지 않았다면 객체 수정
        if(commentRequestDto.getCommentContent() != null) {
            this.commentContent = commentRequestDto.getCommentContent();
        }

    }
}
