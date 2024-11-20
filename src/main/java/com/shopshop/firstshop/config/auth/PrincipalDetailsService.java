package com.shopshop.firstshop.config.auth;

import com.shopshop.firstshop.entity.Member;
import com.shopshop.firstshop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정 파일에서 loginProcessingUrl("/members/login")
// 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername() 함수가 호출를 호출해서
// 회원 정보를 찾고 된다. - 규칙!!!
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    // 리턴하는 것(userDetails)은 Authentication 객체 내부에 들어간다.
    // Authentication 객체는 시큐리티 session 내부에 들어간다.
    // 최정적으로 로그인이 완료된다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);

        if (member != null){
            return new PrincipalDetails(member);
        }
        return null;
    }
}
