package com.example.demo.entities;

import com.example.demo.entities.UserAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString

@Entity
@Table(name = "userNotes")
public class UserNotes {

    public UserNotes(String title, String activityType, Timestamp startDate, Timestamp endDate, String description, UserAccount userAccount) {
        this.title = title;
        this.activityType = activityType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.userAccount = userAccount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    @Column(name = "id_note")
    private Long idNote;

    @Size(min = 1, message = "Nazwa musi zawierać min. 1 znak!")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

}
