package com.shopshop.firstshop.controller;

import com.shopshop.firstshop.dto.JoinFormDto;
import com.shopshop.firstshop.dto.LoginFormDto;
import com.shopshop.firstshop.entity.Member;
import com.shopshop.firstshop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
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
    public String joinForm(Model model) {

        log.info("페이지 members/join이 열렸음");
        model.addAttribute("joinFormDto", new JoinFormDto());
        return "member/joinForm"; // resources/templates의 member/joinForm 렌더링해서 보여줌
    }

    @PostMapping("/join")
    public String memberForm(@Valid JoinFormDto joinFormDto, BindingResult bindingResult, Model model) {
        // 검증하려는 객체 앞에 @Valid 애노테이션 붙여줘야 한다.
        // bindingResult 파라미터를 추가해주고 검증의 결과가 여기에 저장되게 한다.

        if(bindingResult.hasErrors()) {
            log.info("bindingResult has error");
            // getModel() 메소드를 통해 joinFormDto의 검증 결과를 가져온다.
            log.info("BindingResult: {}", bindingResult.getModel());

            // bindingResult 객체에 에러가 있으면 회원가입 페이지 다시 이동함
            // 이때 bindingResult가 html로 넘어간다.
            return "member/joinForm";
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




    @GetMapping("/login")
    public String memberLogin(Model model) {

        model.addAttribute("loginFormDto", new LoginFormDto());

        return "/member/loginForm";
    }

    /*@PostMapping("/login")
    public String memberLogin(@Valid LoginFormDto loginFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult has error");
            log.info("BindingResult: {}", bindingResult.getModel());

            return "member/loginForm";
        }

        try {
            memberService.login(loginFormDto.getEmail(), loginFormDto.getPassword());
            return "redirect:/";
        } catch (IllegalStateException e) {
            log.info("error", e);
            model.addAttribute("LoginErrorMessage", e.getMessage());
            return "/member/loginForm";
        }
    }*/

    @GetMapping("/joinAdmin")
    public String joinAdminForm(Model model) {
        model.addAttribute("joinFormDto", new JoinFormDto());
        model.addAttribute("role", "admin");
        return "member/joinForm";
    }

    @PostMapping("/joinAdmin")
    public String joinAdmin(@Valid JoinFormDto joinFormDto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            log.info("bindingResult has error");
            log.info("BindingResult: {}", bindingResult.getModel());


            return "member/joinForm";
        }

        try {
            Member member = Member.createAdmin(joinFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/joinForm";
        }

        return "redirect:/";
    }



    @GetMapping("/user")
    public String user() {
        return "adminHome";
    }

    @Secured("ROLE_USER")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보 페이지";
    }
}
