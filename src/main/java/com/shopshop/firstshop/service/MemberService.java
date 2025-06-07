package com.shopshop.firstshop.service;

import com.shopshop.firstshop.entity.Member;
import com.shopshop.firstshop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional // 로직을 처리하다가 에러가 발생하였다면, 변경된 데이터를 로직을 수행하기 이전 상태로 콜백
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByUsername(member.getUsername());
        if (findMember.getUsername().matches(member.getUsername())) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
            // 이미 가입된 경우이므로 예외처리
        }
    }

    public void login(String email, String password) {

        // 이메일로 사용자를 조회 -> 조회가 안되면 예외 발생
        Member member = memberRepository.findByUsername(email);

        if (member == null) {
            throw new IllegalStateException("해당 이메일을 찾을 수 없습니다.");
        }

        // 비밀번호 검증
        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalStateException("잘못된 비밀번호입니다.");
        }
    }

    public Member getUMemberByUsername(String username) {

        return memberRepository.findByUsername(username);
    }

    public boolean checkPassword(Member member, String password) {

        return passwordEncoder.matches(password, member.getPassword());
    }

    public void updateMember(Member member) {
        memberRepository.save(member);
    }
}
