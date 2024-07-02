package com.study.board.service;

import com.study.board.dto.MemberRequestDto;
import com.study.board.dto.MemberResponseDto;
import com.study.board.enums.EmailStatus;
import com.study.board.enums.LoginStatus;
import com.study.board.entity.Member;
import com.study.board.enums.PasswordStatus;
import com.study.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class MemberService {

    private MemberRepository memberRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository,  PasswordEncoder passwordEncoder) {

        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //비밀번호 찾기 - 이메일 존재 여부
   public EmailStatus checkExistenceEmail(String email) {

       boolean existsByEmail = memberRepository.existsByEmail(email);

       if(existsByEmail) {
           return EmailStatus.SUCCESS;
       } else {
           return EmailStatus.DOESNT_EXIST;
       }
   }

    //패스워드 중복확인
    public PasswordStatus passwordConfirm(String password, String passwordConfirm) {

        if(password.equals(passwordConfirm)) {
            return PasswordStatus.SUCCESS;
        } else {
            return PasswordStatus.PASSWORD_MISMATCH;
        }
    }

    //회원가입 처리
    public void join(MemberRequestDto memberRequestDto) throws Exception {

        //비밀번호 암호화
        String newPassword = passwordEncoder.encode(memberRequestDto.getPassword());
        //암호화한 비밀번호를 DB에 저장
        memberRequestDto.setPassword(newPassword);

        memberRequestDto.setReg_day(LocalDateTime.now());

        memberRequestDto.setUpdate_day(LocalDateTime.now());

        memberRequestDto.setLevel(1);

        memberRepository.save(memberRequestDto.toEntity(memberRequestDto));
    }

    //회원 정보 가져오기
    public List<MemberResponseDto> memberInfo(String id) {

        List<Member> list = memberRepository.findByLoginId(id);

        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();

        for(Member member : list) {

            MemberResponseDto dto = MemberResponseDto.builder()
                    .memberId(member.getMemberId())
                    .loginId(member.getLoginId())
                    .password(member.getPassword())
                    .email(member.getEmail())
                    .reg_day(member.getReg_day())
                    .update_day(member.getUpdate_day())
                    .level(member.getLevel())
                    .build();

            memberResponseDtoList.add(dto);

        }

        return memberResponseDtoList;
    }

    //로그인 처리
    public LoginStatus login(String id, String password) {

        LoginStatus result = LoginStatus.SUCCESS;

        List<Member> list = memberRepository.findByLoginId(id);

        if(!list.isEmpty()) {

            boolean matches = passwordEncoder.matches(password, list.get(0).getPassword());

            if(matches) {
                result = LoginStatus.SUCCESS;
            } else {
                result = LoginStatus.NOT_AVAILABLE;
            }

        } else {
            result = LoginStatus.NOT_AVAILABLE;
        }

        return result;
    }

    //비밀번호 재설정
    public void setNewPassword(MemberRequestDto memberRequestDto, String email) {

        List<Member> list = memberRepository.findByEmail(email);
        //비밀번호 암호화
        String newPassword = passwordEncoder.encode(memberRequestDto.getPassword());

        memberRequestDto.setMemberId(list.get(0).getMemberId());

        memberRequestDto.setLoginId(list.get(0).getLoginId());

        memberRequestDto.setEmail(email);

        memberRequestDto.setPassword(newPassword);

        memberRequestDto.setReg_day(list.get(0).getReg_day());

        memberRequestDto.setUpdate_day(LocalDateTime.now());

        memberRequestDto.setLevel(1);

        memberRepository.save(memberRequestDto.toEntity(memberRequestDto));
    }

    //회원탈퇴
    public void memberDelete(Integer id) {

            memberRepository.deleteById(id);

    }


}










