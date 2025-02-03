package com.shopshop.firstshop.security;

import com.shopshop.firstshop.constant.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.util.Collection;

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String defaultRedirectUrl = "/";

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info("authorities: {}", authorities);

        for (GrantedAuthority authority : authorities) {
            log.info("authority: {}", authority);
            if(authority.getAuthority().equals(Role.ROLE_USER.toString())){
                defaultRedirectUrl = "/user/home";
                break;
            } else if(authority.getAuthority().equals(Role.ROLE_ADMIN.toString())){
                defaultRedirectUrl = "/admin/home";
                break;
            }
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest != null){ // 접근 권한 없는 경로로 접근해서 스프링 시큐리티가 인터셉트를 통한 로그인 성공한 경우
            String redirectUrl = savedRequest.getRedirectUrl();
            log.info("redirectUrl: {}", redirectUrl);
            response.sendRedirect(redirectUrl);
        }else{ // 로그인 버튼 눌러서 로그인한 경우 기존 있던 페이지로 이동해야 함.
            String prevPage = request.getHeader("Referer");
            log.info("prevPage: {}", prevPage);
            response.sendRedirect(defaultRedirectUrl);
        }
    }
}

