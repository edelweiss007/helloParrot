package com.study.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//JPA가 @Entity 애노테이션을 가진 클래스를 board 테이블에 대한 설정이란 것을 인지하고 읽는다.

@NoArgsConstructor //파라미터 없는 생성자를 생성
@AllArgsConstructor //모든 필드값을 파라미터로 받는 생성자를 생성
@Getter
@Builder
@Entity //자바의 영속성 JPA에서 이 클래스가 DB에 있는 table을 의미
@Table(name = "board")
public class Board {

    @Id //PK를 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mariaDB에서는 IDENTITY. 오라클에서 쓰는 시퀀스
    private Integer id;

    private String title;

    private String content;

    private String filename;

    private String filepath;

    private LocalDateTime created;

    private String writer;
             //어떤 것과 매칭이 되느냐  /  부모가 삭제되면 자식도 삭제
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();



}
