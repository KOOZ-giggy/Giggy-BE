package com.kooz.giggy.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String contact;

    @Enumerated(EnumType.STRING)
    private BizType bizType;

    // OAuth2 Service provider
    private String provider;
    // Unique ID who Logged in with OAuth2
    private String providerId;
}
