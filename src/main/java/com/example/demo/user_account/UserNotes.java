package com.example.demo.user_account;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString

@Entity
@Table(name = "userNotes")
public class UserNotes {
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
}
