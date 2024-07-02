package com.study.board.dto;

import com.study.board.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberRequestDto {

    private Integer memberId;

    //Null, 빈 문자열, 스페이스만 있는 문자열 불가
    @NotBlank(message = "ID를 입력해 주세요.")
    //영문 소문자 1개 이상 포함한 영문, 숫자로만 이루어진 5 ~ 12자 이하
    @Pattern(regexp = "^(?=.*[a-z])[a-z\\d]{5,12}$", message = "영문, 숫자 조합의 5-12자로 입력해 주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    //영문(대소문자 구분), 숫자, 특수문자 조합 + 8~20자리 사이 문자
    @Pattern(regexp = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{8,20}$",
             message = "비밀번호는 영문 대문자, 소문자, 특수문자가 1개씩 포함된 8 ~ 20자 사이로 입력해 주세요.")
    private String password;

    @NotBlank(message = "비밀번호를 한번 더 입력해 주세요.")
    private String password_confirm;

    @NotBlank(message = "Email 주소를 입력해 주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private LocalDateTime reg_day;

    private LocalDateTime update_day;

    private String authNumber;

    private String storedEmailAddress;

    private int level;

    public Member toEntity(MemberRequestDto memberRequestDto) {

        return Member.builder()
                .memberId(memberRequestDto.getMemberId())
                .loginId(memberRequestDto.getLoginId())
                .password(memberRequestDto.getPassword())
                .email(memberRequestDto.getEmail())
                .reg_day(memberRequestDto.getReg_day())
                .update_day(memberRequestDto.getUpdate_day())
                .level(memberRequestDto.getLevel())
                .build();
    }

}
