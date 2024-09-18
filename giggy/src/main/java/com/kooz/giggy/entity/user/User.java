package com.kooz.giggy.domain.user.entity;

import com.kooz.giggy.domain.BaseEntity;
import com.kooz.giggy.domain.user.dto.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private String password;

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

    @Enumerated(value = EnumType.STRING)
    // OAuth2 Service provider
    private OAuthProviderType provider;

    // Unique ID who Logged in with OAuth2
    private String providerId;

    @Builder
    public User(String name, String password, String contact, LocalDate dob, String address, String postCode, String profileImageUrl, OAuthProviderType provider, String providerId) {
        this.name = name;
        this.password = password;
        this.contact = contact;
        this.dob = dob;
        this.address = address;
        this.postCode = postCode;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void update(UserUpdateRequest request, PasswordEncoder encoder) {
        this.password = request.getPassword() == null || request.getPassword().isBlank() ? this.password : encoder.encode(request.getPassword());
        this.contact = request.getContact();
        this.address = request.getAddress();
        this.postCode = request.getPostCode();
        this.bizType = request.getBizType();
    }
}
