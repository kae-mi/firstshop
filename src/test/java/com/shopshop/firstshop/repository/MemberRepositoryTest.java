/*
package com.shopshop.firstshop.repository;


import com.shopshop.firstshop.dto.JoinFormDto;
import com.shopshop.firstshop.entity.Member;
import com.shopshop.firstshop.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(MemberRepositoryTest.class);
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원 저장 테스트")
    void saveMember() {

        JoinFormDto joinFormDto = new JoinFormDto("rudah7815@naver.com", "rudwo0078@", "didrudah", "1234st");
        memberService.saveMember(joinFormDto);

        Member findMember = memberRepository.findByEmail(joinFormDto.getEmail());
        Assertions.assertThat(joinFormDto.getName()).isEqualTo(findMember.getName());
        log.info("memberFormDto.getName()={}", joinFormDto.getName());
        log.info("byEmail.get(0).getName()={}", findMember.getName());
    }

    @Test
    @DisplayName("이메일 중복 회원 저장 테스트")
    void saveDuplicateMember() {
        JoinFormDto member1 = new JoinFormDto("rudah7815@naver.com", "rudwo0078@", "didrudah", "1234st");
        JoinFormDto member2 = new JoinFormDto("rudah7815@naver.com", "rudwo0078@", "didrudah", "1234st");

        memberService.saveMember(member1);

        assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);});
    }
}*/
