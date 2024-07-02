package com.study.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "member")
//클래스 이름과 테이블 이름이 같으면 인식한다.
public class Member {

    @Id //PK를 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mariaDB에서는 IDENTITY. 오라클에서 쓰는 시퀀스
    @Column(name = "member_id")  //레포지토리에서 findByMemberId라고 써야 JPA에서 인식하고 언더바를 쓰면 인식하지 못함
    private Integer memberId;

    @Column(name = "login_id")
    private String loginId;

    private String password;

    private String email;

    private LocalDateTime reg_day;

    private LocalDateTime update_day;

    private int level;

}
