package com.shopshop.firstshop.entity;

import com.shopshop.firstshop.constant.Role;
import com.shopshop.firstshop.dto.JoinFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member") // 클래스 이름을 따서 만들기에 굳이 안써도 되긴 함
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @OneToMany(mappedBy = "member") // Order 엔티티와 연관관계 매핑
    private List<Order> orders = new ArrayList<>(); // 회원은 여러 주문 가능

    private String name; // 회원 실명

    @Column(unique = true) // 이메일을 통해 회원을 구분하므로 UNIQUE
    private String username; // 이메일 - 로그인을 위한 id

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    // 회원 정보를 만드는 static 함수
    public static Member createMember(JoinFormDto joinFormDto,
                                      PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setName(joinFormDto.getName());
        member.setUsername(joinFormDto.getUsername());
        String encodedPassword = passwordEncoder.encode(joinFormDto.getPassword());
        member.setPassword(encodedPassword);
        member.setAddress(joinFormDto.getAddress());
        member.setRole(Role.ROLE_USER);
        return member;
    }

    public static Member createAdmin(JoinFormDto joinFormDto,
                              PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setName(joinFormDto.getName());
        member.setUsername(joinFormDto.getUsername());
        String encodedPassword = passwordEncoder.encode(joinFormDto.getPassword());
        member.setPassword(encodedPassword);
        member.setAddress(joinFormDto.getAddress());
        member.setRole(Role.ROLE_ADMIN);
        return member;
    }
}
