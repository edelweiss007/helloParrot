package com.study.board.repository;

import com.study.board.dto.BoardResponseDto;
import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository                              //T는 Entity의 타입클래스이고 ID는 PK 값의 Type
public interface BoardRepository extends JpaRepository<Board, Integer> {

    //findBy컬럼이름: 컬럼에서 키워드를 넣어서 찾겠다. ex)마우스
    //findBy컬럼이름Containing: 컬럼에서 키워드가 포함된 것을 찾겠다 ex)마우스의 '마'
    Page<Board> findByWriterContaining(String searchKeyword, Pageable pageable);

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);

}
