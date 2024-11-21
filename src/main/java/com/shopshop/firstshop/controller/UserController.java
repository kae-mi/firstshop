package com.shopshop.firstshop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private final HttpSession httpSession;

    public UserController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username );
        return "userHome";
    }


}
