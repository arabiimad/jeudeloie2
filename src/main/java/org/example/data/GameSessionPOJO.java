package org.example.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import java.util.List;

/**
 * JPA Entity for the game session
 */
@Entity
@Data
public class GameSessionPOJO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<InfoPlayerPOJO> players;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private QuestionDeckPOJO deck;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private BoardPOJO board;

    private int nbFaces;

    // Getters et setters
}
