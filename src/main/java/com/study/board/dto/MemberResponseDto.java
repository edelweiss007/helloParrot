package com.study.board.dto;

import com.study.board.entity.Board;
import com.study.board.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MemberResponseDto {

    private Integer memberId;

    private String loginId;

    private String password;

    private String email;

    private LocalDateTime reg_day;

    private LocalDateTime update_day;

    private int level;

    //Entity로 바꿔주는 메서드
    public Member toEntity(MemberResponseDto memberResponseDto) {

        return Member.builder()
                .memberId(memberResponseDto.getMemberId())
                .loginId(memberResponseDto.getLoginId())
                .password(memberResponseDto.getPassword())
                .email(memberResponseDto.getEmail())
                .reg_day(memberResponseDto.getReg_day())
                .update_day(memberResponseDto.getUpdate_day())
                .level(memberResponseDto.getLevel())
                .build();
    }

    //Page<Entity> -> Page<Dto> 변환처리
    public Page<MemberResponseDto> toDtoList(Page<Member> memberList){

        Page<MemberResponseDto> memberDtoList = memberList.map(member -> MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .email(member.getEmail())
                .reg_day(member.getReg_day())
                .update_day(member.getUpdate_day())
                .level(member.getLevel())
                .build());

        return memberDtoList;
    }

}
