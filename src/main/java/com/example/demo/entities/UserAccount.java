package com.example.demo.entities;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;


import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString

@Entity
@Table(name = "userAccount")
public class UserAccount {
    public UserAccount(String userName, String userPassword, String userEmail) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    @Column(name = "user_id")
    private Long userId;

    @Size(min = 3, message = "Nazwa musi zawierać od {min} znaków!")
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Size(min = 3 , message = "Nazwa musi zawierać od {min} znaków!")
    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Pattern(regexp = ".+@.+\\..+", message = "Nieprawidłowy adres email")
    @Size(min = 3 , message = "Nazwa musi zawierać od {min} znaków!")
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserNotes> userNotesList = new ArrayList<>();

    public void addUserNotes(UserNotes userNotes) {
        userNotesList.add(userNotes);
        userNotes.setUserAccount(this);
    }

    public void removeUserNotes(UserNotes userNotes) {
        userNotesList.remove(userNotes);
        userNotes.setUserAccount(null);
    }
}