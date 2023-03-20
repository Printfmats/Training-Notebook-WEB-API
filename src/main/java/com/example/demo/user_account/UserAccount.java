package com.example.demo.user_account;

import jakarta.persistence.*;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString

@Entity
@Table(name = "userAccount")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    @Column(name = "user_id", nullable = false)
    private Long userId;

//    @Size(min = 5, max = 20)
    @Column(name = "user_name", nullable = false)
    private String userName;

//    @Size(min = 8, max = 30)
    @Column(name = "user_password", nullable = false)
    private String userPassword;

//    @Size(min = 4, max = 60)
    @Column(name = "user_email", nullable = false)
    private String userEmail;
}
