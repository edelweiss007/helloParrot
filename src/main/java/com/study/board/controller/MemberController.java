package com.study.board.controller;

import com.study.board.dto.MemberRequestDto;
import com.study.board.dto.MemberResponseDto;
import com.study.board.enums.EmailStatus;
import com.study.board.enums.LoginStatus;
import com.study.board.enums.PasswordStatus;
import com.study.board.service.EmailService;
import com.study.board.service.MemberService;
import com.study.board.service.RedisUtil;
import com.study.board.validator.CheckEmailDuplicationValidator;
import com.study.board.validator.CheckIdDuplicationValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;
    private EmailService emailService;
    private RedisUtil redisUtil;

    @Autowired
    public MemberController(MemberService memberService,
                            EmailService emailService,
                            RedisUtil redisUtil,
                            CheckIdDuplicationValidator checkIdDuplicationValidator,
                            CheckEmailDuplicationValidator checkEmailDuplicationValidator) {

        this.memberService = memberService;
        this.emailService = emailService;
        this.redisUtil = redisUtil;
        this.checkIdDuplicationValidator = checkIdDuplicationValidator;
        this.checkEmailDuplicationValidator = checkEmailDuplicationValidator;
    }

    //중복 체크 유효성 검사
    private final CheckIdDuplicationValidator checkIdDuplicationValidator;
    private final CheckEmailDuplicationValidator checkEmailDuplicationValidator;

    //커스텀 유효성 검증
    @InitBinder //컨트롤러의 메서드에 바인딩 될 때 마다 호출되는 초기화 메서드를 정의할 때 사용한다.
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkIdDuplicationValidator); //검증기 추가
        binder.addValidators(checkEmailDuplicationValidator);
    }


    //회원가입 폼
    @GetMapping("/join")
    public String memberJoinForm(Model model) {

        model.addAttribute("memberRequestDto", new MemberRequestDto());

        return "/member/memberJoin";
    }

    //회원가입 처리
    @PostMapping("/join")
    public String memberJoinProcess(@Valid MemberRequestDto memberRequestDto,
                                    BindingResult result,
                                    Model model) throws Exception {

        PasswordStatus passwordConfirm = memberService.passwordConfirm(memberRequestDto.getPassword(), memberRequestDto.getPassword_confirm());

        if (result.hasErrors() || passwordConfirm == PasswordStatus.PASSWORD_MISMATCH) {

            model.addAttribute("memberRequestDto", memberRequestDto);
            model.addAttribute("passwordConfirm", passwordConfirm.getStatus());

            return "/member/memberJoin";

        } else {

            memberService.join(memberRequestDto);
        }
        return "redirect:/member/login";

    }


    //로그인 폼
    @GetMapping("/login")
    public String loginForm(HttpSession session) {

        Object loginId = session.getAttribute("LoginId");

        if (loginId != null) {

            return "error/4xx";

        } else {

            return "/member/login";
        }

    }

    //로그인 처리
    @PostMapping("/login")
    public String loginProcess(MemberRequestDto memberRequestDto,
                               Model model,
                               HttpSession session) {

        LoginStatus loginStatus = memberService.login(memberRequestDto.getLoginId(), memberRequestDto.getPassword());

        if (loginStatus == LoginStatus.SUCCESS) {

            List<MemberResponseDto> list = memberService.memberInfo(memberRequestDto.getLoginId());

            String loginIdForSession = list.get(0).getLoginId();

            String emailForSession = list.get(0).getEmail();

            Integer idForSession = list.get(0).getMemberId();

            //세션의 key           value
            session.setAttribute("LoginId", loginIdForSession);

            session.setAttribute("Email", emailForSession);

            session.setAttribute("Id", idForSession);

            session.setMaxInactiveInterval(60 * 60); //60분

            int level = list.get(0).getLevel();

            System.out.println(level);

            //일반 회원이면 게시판 리스트를 리턴
            if (level == 1) {
                return "redirect:/board";
            } else { //관리자면 회원관리 페이지를 리턴
                return "redirect:/admin";
            }


        } else {

            model.addAttribute("loginStatus", loginStatus.getStatus());

        }
        return "/member/login";
    }

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/board";
    }

    //비밀번호 찾기 폼
    @GetMapping("/findPassword")
    public String findPassword(Model model) {

        model.addAttribute("memberRequestDto", new MemberRequestDto());

        return "/member/findPassword";
    }

    //비밀번호 찾기 처리(이메일 인증)
    @PostMapping("/findPassword")
    public String findPasswordProcess(String email, Model model) throws Exception {
        //입력한 이메일이 존재하는지 확인
        EmailStatus emailConfirm = memberService.checkExistenceEmail(email);

        if (emailConfirm == EmailStatus.DOESNT_EXIST) {

            model.addAttribute("emailConfirm", emailConfirm.getStatus());

            return "/member/findPassword";

        } else {

            emailService.sendSimpleMessage(email);

            long startTime = System.currentTimeMillis();

            model.addAttribute("email", email);
            
            model.addAttribute("startTime", startTime);

            return "/member/emailAuth";
        }
    }

    //이메일 인증번호 입력 폼
    @GetMapping("/emailAuth")
    public String emailAuth(Model model) {

        model.addAttribute("memberRequestDto", new MemberRequestDto());

        return "/member/emailAuth";
    }


    //이메일 인증번호 처리
    @PostMapping("/emailAuth")
    public String emailAuthProcess(MemberRequestDto memberRequestDto, 
                                   Model model,
                                   @RequestParam long startTime) {

        long endTime = System.currentTimeMillis();

        long durationTimeSec = endTime - startTime;

        String storedEmailAddress = redisUtil.getData(memberRequestDto.getAuthNumber());

        //인증번호 이메일 전송 후 3분이 지났을 시 에러페이지로 이동
        if(durationTimeSec > 180000L) {

            model.addAttribute("timeOver", "입력시간이 초과했습니다! :(");

            return "/member/errorPage";

        //인증번호가 맞으면
        } else if((memberRequestDto.getEmail()).equals(storedEmailAddress)) {

            memberRequestDto.setStoredEmailAddress(storedEmailAddress);

            return "/member/setNewPassword";

        //인증번호가 틀렸을 경우
        } else {

            model.addAttribute("startTime", startTime);
            model.addAttribute("email", memberRequestDto.getEmail());
            model.addAttribute("authNumStatus", EmailStatus.DOESNT_EXIST.getStatus());

            return "/member/emailAuth";
        }
    }

    //비밀번호 재설정 폼
    @GetMapping("/setNewPassword")
    public String setNewPassword(Model model) {

        model.addAttribute("memberRequestDto", new MemberRequestDto());

        return "/member/setNewPassword";
    }

    //비밀번호 재설정 처리
    @PostMapping("/setNewPassword")
    public String setNewPasswordProcess(@Valid MemberRequestDto memberRequestDto,
                                        BindingResult result,
                                        Model model,
                                        HttpSession session) throws Exception {

        PasswordStatus passwordConfirm = memberService.passwordConfirm(memberRequestDto.getPassword(), memberRequestDto.getPassword_confirm());

        if (result.hasFieldErrors("password") || passwordConfirm == PasswordStatus.PASSWORD_MISMATCH) {

            model.addAttribute("passwordConfirm", passwordConfirm.getStatus());
            memberRequestDto.setEmail(memberRequestDto.getStoredEmailAddress());
            model.addAttribute("memberRequestDto", memberRequestDto);

            return "/member/setNewPassword";

        } else {

            Object loginId = session.getAttribute("LoginId");

            //회원정보 수정에서 비밀번호를 바꿀 경우
            if (loginId != null) {

                String email = (String) session.getAttribute("Email");

                memberService.setNewPassword(memberRequestDto, email);

                return "redirect:/member/mypage";
                //비밀번호 찾기로 새로운 비밀번호를 설정할 경우
            } else {

                memberService.setNewPassword(memberRequestDto, memberRequestDto.getStoredEmailAddress());

                return "redirect:/member/login";
            }

        }
    }


    //마이페이지
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {

        model.addAttribute("login", LoginStatus.SUCCESS.getStatus());

        model.addAttribute("id", session.getAttribute("LoginId"));

        model.addAttribute("email", session.getAttribute("Email"));

        return "/member/mypage";
    }

    //회원탈퇴
    @ResponseBody
    @GetMapping("/delete")
    public ResponseEntity<String> memberDelete(HttpSession session) {

        Integer id = (Integer) session.getAttribute("Id");

        try {
            memberService.memberDelete(id);

            session.invalidate();

            return ResponseEntity.status(HttpStatus.OK).body(null);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }

    //5xx 에러페이지
    @GetMapping("/errorPage")
    public String getErrorPage() {

        return "/error/5xx";
    }

}
