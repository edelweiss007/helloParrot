package com.study.board.validator;

import com.study.board.dto.MemberRequestDto;
import com.study.board.entity.Member;
import com.study.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CheckEmailDuplicationValidator extends AbstractValidator<MemberRequestDto> {

    private final MemberRepository memberRepository;

    @Autowired
    public CheckEmailDuplicationValidator(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }

    @Override
    protected void doValidate(MemberRequestDto memberRequestDto, Errors errors) {
        //이메일이 중복이라면
        if(memberRepository.existsByEmail(memberRequestDto.toEntity(memberRequestDto).getEmail())) {

            errors.rejectValue("email", "이메일 중복 오류", "이미 사용 중인 이메일입니다.");
        }
    }
}
