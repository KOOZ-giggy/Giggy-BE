package com.kooz.giggy.domain.user.entity;

import com.kooz.giggy.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "users")
@ToString
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;

    @Column
    private String contact;

    @Column(nullable = false)
    private LocalDate dob;

    @Column
    private String address;

    @Column
    private String postCode;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private BizType bizType;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.EMPLOYEE;

    @Column
    private String profileImageUrl;

    // OAuth2 Service provider
    private String provider;

    // Unique ID who Logged in with OAuth2
    private String providerId;

    @Builder
    public User(String name, String contact, LocalDate dob, String address, String postCode, String profileImageUrl, String provider, String providerId) {
        this.name = name;
        this.contact = contact;
        this.dob = dob;
        this.address = address;
        this.postCode = postCode;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
        this.providerId = providerId;
    }
}
