package org.example.data;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * JPA Entity for deck
 */
@Entity
@Data
public class QuestionDeckPOJO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ElementCollection
    private List<Integer> currentCards;

    @ElementCollection
    private List<Integer> discardCards;

    // Getters et setters
}
