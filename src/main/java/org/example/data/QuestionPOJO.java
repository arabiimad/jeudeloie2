package org.example.data;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class QuestionPOJO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String question;
    private String answer;

    // Constructeurs
    public QuestionPOJO() {}

    public QuestionPOJO(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

}

