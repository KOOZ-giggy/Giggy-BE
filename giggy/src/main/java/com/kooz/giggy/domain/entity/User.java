package com.kooz.giggy.domain.entity;

import com.kooz.giggy.domain.entity.BizType;
import com.kooz.giggy.domain.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String contact;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postCode;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private BizType bizType;

    @OneToMany
    private List<Post> posts;

    @Enumerated(EnumType.STRING)
    private UserPermission permission;

    @Column
    private String profileImageUrl;

    // OAuth2 Service provider
    private String provider;
    // Unique ID who Logged in with OAuth2
    private String providerId;
}
