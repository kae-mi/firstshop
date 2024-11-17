package com.shopshop.firstshop.controller;

import com.shopshop.firstshop.dto.JoinFormDto;
import com.shopshop.firstshop.entity.Member;
import com.shopshop.firstshop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public String memberForm(Model model) {
        model.addAttribute("joinFormDTO", new JoinFormDto());
        return "member/joinForm"; // resources/templates의 member/joinForm 렌더링해서 보여줌
    }

    @PostMapping("/join")
    public String memberForm(@Valid JoinFormDto joinFormDto, BindingResult bindingResult, Model model) {
        // 검증하려는 객체 앞에 @Valud 애노테이션 붙여줘야 한다.
        // bindingResult 파라미터를 추가해주고 검증의 결과가 여기에 저장되게 한다.

        if(bindingResult.hasErrors()) {
            return "member/joinForm"; // bindingResult 객체에 에어가 있으면 회원가입 페이지 다시 이동함
        }

        try {
            //DTO를 통해 실제 회원 객체를 만들고 만들어진 회원 객체를 통해 DB에 저장하는 로직
            Member member = Member.createMember(joinFormDto, passwordEncoder); //Member 객체를 만드는 메소드 실행
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/joinForm";
        }

        return "redirect:/";
    }

    /*@GetMapping("/login")
    public String memberLogin(Model model) {

        model.addAttribute("loginFormDTO", new LoginFormDto());
        return "loginForm";
    }

    @PostMapping("/login")
    public String memberLogin(LoginFormDto loginFormDto) {

        try {
            Member member = memberService.login(loginFormDto.getEmail(), loginFormDto.getPassword());
            return "redirect:/";
        } catch (IllegalStateException e) {
            log.info("error", e);
            return "redirect:/members/login";
        }
    }*/
}
