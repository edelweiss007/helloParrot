package com.study.board.validator;

import com.study.board.dto.MemberRequestDto;
import com.study.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CheckIdDuplicationValidator extends AbstractValidator<MemberRequestDto> {

    private final MemberRepository memberRepository;

    @Autowired
    public CheckIdDuplicationValidator(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }

    @Override
    protected void doValidate(MemberRequestDto memberRequestDto, Errors errors) {
        //아이디가 중복인 경우
        if(memberRepository.existsByLoginId(memberRequestDto.toEntity(memberRequestDto).getLoginId())) {

            errors.rejectValue("loginId", "아이디 중복 오류", "이미 사용중인 아이디입니다.");
        }
    }
}
