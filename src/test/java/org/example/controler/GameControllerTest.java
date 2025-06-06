package org.example.controler;

import org.example.model.business.*;
import org.example.stockage.ConfigurableDAOFactory;
import org.example.stockage.DAOException;
import org.example.stockage.DAOFactory;
import org.example.stockage.UnknownDAOException;
import org.example.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jeuDeLoiePU");
    EntityManager em = emf.createEntityManager();

    DAOFactory factory = new ConfigurableDAOFactory(em);


    public final List<String> messages = new ArrayList<>();

    GameController createGameController(int nbFaces) {
        Dice dice = new Dice(nbFaces);
        List<String> players = new ArrayList<>();
        players.add("T");
        View view = new View() {

            @Override
            public void display(String message) {
                messages.add(message);
            }

            @Override
            public String playerAnswerToQuestion(String playerName, String question) {
                return "1970";
            }

            @Override
            public void waitForDiceRoll(String playerName) {
                System.out.println("Press Enter to roll the dice for " + playerName + "...");
                try {
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        return new GameController(view, players, dice,factory);
    }

    @BeforeEach
    void setUp() {
        messages.clear();
    }

    @Test
    void runGame() {
        GameController gameController = createGameController(10);
        gameController.runGame();
        assertFalse(messages.isEmpty());
    }

    @Test
    void playerRound() {
        Pawn player = new Pawn("Tom",  Color.RED, 0);
        GameController gamecontroller = createGameController(1);
        gamecontroller.playerRound(player);
        assertEquals(1, player.getPosition());
    }

    @Test
    void playerRoundBad() {
        Pawn player = new Pawn("Tom",  Color.RED, 0);
        GameController gamecontroller = createGameController(1);
        player.setPosition(15);
        gamecontroller.playerRound(player);
        assertEquals(14, player.getPosition());
    }

    @Test
    void applyQuestionEffect() {
        Pawn player = new Pawn("Tom",  Color.RED, 0);
        GameController gamecontroller = createGameController(1);
        player.setPosition(10);
        gamecontroller.applyQuestionEffect(player);
        assertEquals(7, player.getPosition());
        gamecontroller.applyQuestionEffect(player);
        assertEquals(7, player.getPosition());
    }

    @Test
    void loadQuestionList() {
        try {
            GameController gamecontroller = createGameController(1);
            List<Question> questions = gamecontroller.loadQuestionList();
            assertNotNull(questions);
            assertEquals(3, questions.size());
        }
        catch (UnknownDAOException | DAOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getBoard() {
        GameController gamecontroller = createGameController(1);
        Board board = gamecontroller.getGameSession().board();
        assertNotNull(board);
        assertEquals(15, board.getSize());
    }
}