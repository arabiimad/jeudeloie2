package org.example.view;

import org.example.controler.GameController;
import org.example.data.QuestionPOJO;
import org.example.stockage.DAOFactory;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Console view for playing the game
 */
public class ConsoleView implements View {

    /**
     * The console
     */
    private final Console console;

    /**
     * The presenter
     */
    private final GameController presenter;

    private final DAOFactory factory;

    /**
     * Constructor
     */
    public ConsoleView(DAOFactory factory) {
        this.factory = factory;
        this.console = System.console();
        List<String> playersName = getPlayers();
        this.presenter = new GameController(this, playersName,this.factory);
    }

    @Override
    public void display(String message) {
        console.printf("%s\n", message);
    }

    @Override
    public String playerAnswerToQuestion(String playerName, String question) {
        console.printf("Player %s must answer the following question.\n", playerName);
        console.printf("%s ", question);
        return console.readLine();
    }

    /**
     * Get the name of the different players
     * @return the list of names
     */
    private List<String> getPlayers() {
        // Get the number of players
        console.printf("How many players? ");
        String nbPlayersText = console.readLine();
        int nbPlayers = Integer.parseInt(nbPlayersText);

        // Create players
        List<String> players = new ArrayList<>(nbPlayers);
        for (int i = 0; i < nbPlayers; i++) {
            console.printf("Name of player %d: ", i);
            String playerName = console.readLine();
            players.add(playerName);
        }

        return players;
    }

    /**
     * Run a new game for the players
     */
    public void runGame() {
        presenter.runGame();
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

}
