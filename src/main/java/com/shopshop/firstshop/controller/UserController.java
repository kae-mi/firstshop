package com.shopshop.firstshop.controller;

import com.shopshop.firstshop.dto.EditFormDto;
import com.shopshop.firstshop.entity.Member;
import com.shopshop.firstshop.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/***
 일반 회원으로 인증된 사용자만이 접근할 수 있는 User 컨트롤러
 ***/
@Controller
@RequestMapping("/user")
@Secured("ROLE_USER") // 해당 권한을 가져야지만 이 컨트롤러를 사용할 수 있음
@Slf4j
public class UserController {

    private final MemberService memberService;

    @Autowired
    public UserController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 일반 회원 홈 Get URL
    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        String username = principal.getName();
        log.info("username = {}", username);
        model.addAttribute("username", username);
        return "home";
    }

    // 회원정보 페이지 Get URL
    @GetMapping("/info")
    public String memberInfo(Principal principal, Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        log.info("username: {}, principal: {}", username, principal);

        Member member = memberService.getUMemberByUsername(username);
        log.info("member: {}", member.getOrders().get(0).getOrderStatus());
        model.addAttribute("member", member);
        return "member/memberInfo";
    }

    // 회원 정보 수정 Get URL
    @GetMapping("/verifyPassword")
    public String verifyPasswordForm() {

        return "member/verifyPasswordForm";
    }

    // 회원 정보 수정을 위한 비밀번호 확인 Post URL
    @PostMapping("/verifyPassword")
    public String verifyPassword(@RequestParam("password") String password,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/members/login";  // 로그인되지 않은 경우
        }

        String username = principal.getName();
        Member member = memberService.getUMemberByUsername(username);

        if (memberService.checkPassword(member,password)) {
            return "redirect:/user/edit"; // 비밀번호가 맞다면 회원정보 수정 페이지로 이동
        } else {
            redirectAttributes.addFlashAttribute("error", "잘못된 비밀번호입니다.");
            return "redirect:/user/verifyPassword";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, SecurityContextHolder contextHolder, SecurityContext context) {

        String username = context.getAuthentication().getName();
        log.info("username = {}", username);
        Member member = memberService.getUMemberByUsername(username);
        log.info("member: {}", member.getClass());

        EditFormDto editFormDto = new EditFormDto(member);
        editFormDto.setUsername(username);
        model.addAttribute("editFormDto",editFormDto);
        log.info("메소드 호출 끝");
        return "member/editForm";
    }

    @PutMapping("/edit")
    public String updateMember(@RequestParam("username") String username,
                               @RequestParam("name") String name ,
                               @RequestParam("address") String address){

        log.info("username: {}, name: {}, address: {}", username, name, address);
        Member member = memberService.getUMemberByUsername(username);
        member.setName(name);
        member.setAddress(address);

        memberService.updateMember(member);

        return "redirect:/user/info";
    }

}
