package com.shopshop.firstshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginFormDto {

    @NotBlank(message = "아이디 또는 비밀번호를 입력해주세요.")
    private String email;

    @NotBlank(message = "아이디 또는 비밀번호를 입력해주세요.")
    private String password;
}
