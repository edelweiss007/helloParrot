package com.study.board.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**@EnableWebSecurity :
 * WebSecurityConfigurer에 SpringSecurity 구성을 정의하거나 SecurityFilterChain빈을
 * 노출하여 정의할 때 쓴다.
 */


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean  //SpringSecurity 설정하는 클래스   //스프링 시큐리티의 각종 설정은 HttpSecurity로 대부분 이루어진다.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf().disable();

/** authorizeHttpRequests(): HTTP 요청에 대한 인가 설정을 구성하는데 사용한다.
 * anyRequest(): 설정한 경로 외에 모든 경로를 뜻한다.
 * permitAll(): 어떤 사용자든지 접근 가능
 */
//        http.authorizeHttpRequests().anyRequest().permitAll();

        return http.build();
    }

}
