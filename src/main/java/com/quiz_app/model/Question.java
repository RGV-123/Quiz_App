package com.quiz_app.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"questionTitle", "category"}
    )
)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 500)
    private String questionTitle;
    
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String rightAnswer;
    private String difficultylevel;
    private String category;

}