package com.devteria.identity_service.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username",unique = true,columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    private String username;
    private String password;

    private String firstname;
    private String lastname;
    private LocalDate dob;

    @ManyToMany
    private Set<Role> roles;
}
