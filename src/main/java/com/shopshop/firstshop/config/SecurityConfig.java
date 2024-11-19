package com.shopshop.firstshop.config;

import com.shopshop.firstshop.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // 시큐리티 필터가 스프링 필터체인에 Bean으로 등록이 된다.
@AllArgsConstructor //왜?
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/user").authenticated() // /user로 접근하려면 인증이 필요하고 자동으로 로그인 페이지로 이동함
                        //.requestMatchers("/admin").hasRole("ADMIN") // 마찬가지이며 Role 조건도 필요함
                        .anyRequest().permitAll() // 위에 설정한 url을 제외한 모든 url은 접근 허용
                )
                .formLogin(form -> form
                        .loginPage("/members/login") // 사용자 정의 로그인 페이지
                        .permitAll() // 로그인 페이지는 누구나 접속 가능
                )
                .csrf(AbstractHttpConfigurer::disable
                );
        return http.build();
    }



    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {



                *//*.formLogin(form -> form
                        .loginPage("/members/login") // 로그인 페이지 Url
                        .defaultSuccessUrl("/") // 로그인 성공시 이동할 Url
                        .usernameParameter("email") // 로그인 시 사용할 파라미터 이름
                        .failureUrl("/members/login/error") //로그인 실패시 이동할 Url
                );
*//*

        http
                .logout(logout -> logout
                        // 해당 url로 요청이 오면 로그아웃을 처리한다.
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                                .logoutSuccessUrl("/") // 로그아웃이 정상적으로 되면 해당 url로 이동한다.
                );

        http
                .exceptionHandling(exception -> exception
                        // 인증되지 않은 사용자가 리소스에 접근하려고 하면 수행되는 핸들러이다.
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        return http.build();


                *//*.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/admin").authenticated()
                        .anyRequest().permitAll()
                        );*//*
                *//*.formLogin(form -> form
                        .loginPage("/members/login")
                        .permitAll()
                        //.failureUrl("/members/join")   // 로그인 실패 시 이동할 페이지
                );*//*

        *//*http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/h2-console/**").permitAll() // H2 콘솔에 대한 접근 허용
                        .anyRequest().authenticated() // 나머지 요청에 대해 인증 필요

        );*//*

        *//*http.csrf((csrf) -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")));*/
        // use HTTP Basic authentication
        //http.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery(CSRF)
        //http.csrf(csrf -> csrf.disable());



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
