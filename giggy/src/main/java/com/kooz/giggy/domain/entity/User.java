package com.kooz.giggy.domain.entity;

import com.kooz.giggy.domain.entity.BizType;
import com.kooz.giggy.domain.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class User extends BaseEntity {
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

    @OneToMany
    private List<Post> posts;

    @Column
    private String profileImageUrl;

    // OAuth2 Service provider
    private String provider;
    // Unique ID who Logged in with OAuth2
    private String providerId;
}
