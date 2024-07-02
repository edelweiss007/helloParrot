package com.study.board.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        System.out.println("[interceptor] : " + requestURI);
        //request.getSession(), getSession(true): 세션이 존재하면 현재 세션을 반환하고 존재하지 않으면 새로운 세션을 생성한다.
        //request,getSession(false): 세션이 존재하면 세션을 반환하고 존재하지 않으면 새로 생성하지 않고 null을 반환한다.
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("LoginId") == null) {
            // 로그인 되지 않음
            System.out.println("[미인증 사용자 요청]");

            //로그인으로 redirect
            response.sendRedirect("/member/login?redirectURL=" + requestURI);
            return false;
        }
        // 로그인 되어있을 떄
        return true;
    }
}
