package com.shopshop.firstshop.dto;

import com.shopshop.firstshop.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditFormDto {

    private String name;
    private String username;
    private String address;

    public EditFormDto(Member member) {

        this.name = member.getName();
        this.username = member.getUsername();
        this.address = member.getAddress();
    }
}
