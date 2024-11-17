package com.shopshop.firstshop.config;

import com.shopshop.firstshop.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // 시큐리티 필터가 스프링 필터체인에 Bean으로 등록이 된다.
@AllArgsConstructor //왜?
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .exceptionHandling(exception -> exception
                        // 인증되지 않은 사용자가 리소스에 접근하려고 하면 수행되는 핸들러이다.
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        return http.build();


                /*.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/admin").authenticated()
                        .anyRequest().permitAll()
                        );*/
                /*.formLogin(form -> form
                        .loginPage("/members/login")
                        .permitAll()
                        //.failureUrl("/members/join")   // 로그인 실패 시 이동할 페이지
                );*/

        /*http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/h2-console/**").permitAll() // H2 콘솔에 대한 접근 허용
                        .anyRequest().authenticated() // 나머지 요청에 대해 인증 필요

        );*/

        /*http.csrf((csrf) -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")));*/
        // use HTTP Basic authentication
        //http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery(CSRF)
        //http.csrf(csrf -> csrf.disable());


    }

    // 패스워드 인코딩을 위한 PasswordEncoder를 Bean으로 등록한다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*// H2 콘솔 관련 설정
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }*/
}
