package com.shopshop.firstshop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping({"","/"})
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication:{}",authentication.getName());

        log.info("isAuthenticated(): {}", authentication.isAuthenticated());
        log.info("getName(): {}", authentication.getName());
        log.info("getPrincipal(): {}", authentication.getPrincipal());

        return "home";
    }
}
