package com.shopshop.firstshop.config;

import com.shopshop.firstshop.security.CustomAuthenticationSuccessHandler;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 시큐리티 필터가 스프링 필터체인에 Bean으로 등록이 된다.
@EnableMethodSecurity(securedEnabled = true) // @Secured 애노테이션 활성화
@AllArgsConstructor //왜?
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/user").authenticated() // /user로 접근하려면 인가가 필요하고 인가받지 않았다면 자동으로 로그인 페이지로 이동함
                    .requestMatchers("/admin").hasRole("ADMIN") // 마찬가지이며 Role 조건도 필요함
                    .anyRequest().permitAll() // 위에 설정한 url을 제외한 모든 url은 접근 허용
            )
            .formLogin(form -> form
                    // 원래라면 시큐리티가 제공하는 기본 로그인 페이지 대신 커스텀 로그인 페이지를 사용하도록 설정.
                    // 사용자가 로그인되지 않은 상태로 보호된 URL에 접근하면 /members/login 페이지로 리다이렉트.
                    .loginPage("/members/login")
                    //로그인 폼은 POST 여야 하고, action="/members/login" 이어야 한다. 해당 요청이 들어오면 시큐리티가
                    //요청을 낚아채서 인증 처리를 대신 함.
                    .loginProcessingUrl("/members/login")
                    .successHandler(new CustomAuthenticationSuccessHandler()) //로그인이 성공했을 때 이를 처리하는 커스텀 핸들러
                    .permitAll() // 로그인 페이지는 누구나 접속 가능
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // 패스워드 인코딩을 위한 PasswordEncoder를 Bean으로 등록한다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
