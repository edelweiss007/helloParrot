package com.study.board.repository;

import com.study.board.entity.Board;
import com.study.board.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

    List<Member> findByLoginId(String id);

    List<Member> findByEmail(String email);
    //id 중복체크
    boolean existsByLoginId(String loginId);
    //email 중복체크
    boolean existsByEmail(String email);
    //회원 ID로 검색
    Page<Member> findByLoginIdContaining(String searchKeyword, Pageable pageable);
    //회원 Email로 검색
    Page<Member> findByEmailContaining(String searchKeyword, Pageable pageable);

}
