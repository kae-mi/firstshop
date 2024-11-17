package com.shopshop.firstshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginFormDto {

    @NotBlank
    private String email;
    private String password;
}
