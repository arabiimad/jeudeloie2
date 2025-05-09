package org.example.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * JPA Entity for player information
 */
@Entity
@Data
public class InfoPlayerPOJO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int position;
    private int color;
    private int score;

    // Getters et setters
}
