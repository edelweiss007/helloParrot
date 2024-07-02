package com.study.board.configuration;

import com.study.board.interceptor.LogInterceptor;
import com.study.board.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LogInterceptor()) // LogInterceptor 등록
                .order(1)	// 적용할 필터 순서 설정
                .addPathPatterns("/**")
                .excludePathPatterns("/templates/error"); // 인터셉터에서 제외할 패턴

        registry.addInterceptor(new LoginCheckInterceptor()) //LoginCheckInterceptor 등록
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns
                        ("/board", "/member/join", "/member/login", "/member/logout", "/assets/**",
                         "/member/findPassword", "/member/emailAuth", "/member/setNewPassword",
                        "/member/errorPage", "/errorPage", "/error");
    }
}
