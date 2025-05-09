package org.example.controler;

import org.example.config.ConfigurationManager;
import org.example.data.*;
import org.example.model.business.*;
import org.example.model.technical.ClassicalBoardGenerator;
import org.example.stockage.*;
import org.example.view.View;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * General game controller
 */
public class GameController {


    /**
     * Link with the view
     */
    private final View view;

    /**
     * Game session
     */
    private final GameSession session;

    private final DAOFactory factory;

    private final ConvertorFactory convertor;


    /**
     * The constructor
     *
     * @param playerNames the name of the players
     */
    public GameController(View view, List<String> playerNames, DAOFactory factory) {
        this(view, playerNames, new Dice(), factory);
    }

    /**
     * The constructor
     *
     * @param playerNames the name of the players
     * @param dice        the dice
     */
    public GameController(View view, List<String> playerNames, Dice dice, DAOFactory factory) {
        try {
            this.convertor = new ConvertorFactory();
            this.factory = Objects.requireNonNull(factory);
            initDatabase();

            if (factory.getDaoType().equals("jpa")) {
                DAO<QuestionPOJO> dao = factory.getDAO(QuestionPOJO.class);
                QuestionJPA questionDAO = (QuestionJPA) dao;
                questionDAO.insertSampleQuestions();  // Insert sample questions into the database
            }
            List<Question> questions = loadQuestionList();
            ClassicalBoardGenerator boardGenerator = new ClassicalBoardGenerator();
            Board gameBoard = boardGenerator.generateBoard();
            this.session = new GameSession(2,
                    createPlayers(playerNames),
                    gameBoard,
                    dice,
                    new Deck<>(questions));
            if (factory.getDaoType().equals("jpa")) {
                BoardPOJO boardPOJO = ConvertorFactory.getConvertor(Board.class, BoardPOJO.class).toDTO(gameBoard);
                DAO<BoardPOJO> boardDAO = factory.getDAO(BoardPOJO.class);
                boardDAO.create(boardPOJO);
            }

            if (factory.getDaoType().equals("jpa")) {
                GameSessionPOJO gamePOJO = ConvertorFactory.getConvertor(GameSession.class, GameSessionPOJO.class).toDTO(this.session);
                DAO<GameSessionPOJO> gameDAO = factory.getDAO(GameSessionPOJO.class);
                gameDAO.create(gamePOJO);
            }



            this.view = view;
        } catch (DAOException | DBAccessException | UnknownDAOException e) {
            throw new GameError(e);
        }
    }

    /**
     * The round for one player
     *
     * @param p a player
     */
    public void playerRound(Pawn p) {
        Objects.requireNonNull(p);
        if (p.isInPrison() && p.getRemainingPrisonTurns() > 0) {
            p.decrementPrisonTurns();
            sendMessage(p.getName() + " is in prison. " + p.getRemainingPrisonTurns() + " turn(s) remaining. Turn skipped.");
            return;
        }
        sendMessage("Player " + p.getName() + " on case " + p.getPosition() + " roll the dice.");
        int number = p.rollDice(this.session.dice());
        sendMessage(p.getName() + " go on " + number + " cases.");

        if (p.getPosition() + number > this.session.board().getSize()) {
            sendMessage(p.getName() + " get out of the board.");
            p.setPosition(this.session.board().getSize() - number);
            return;
        }

        p.setPosition(p.getPosition() + number);

        if (p.getPosition() + number < this.session.board().getSize()) {
            Case c = this.session.board().getCase(p.getPosition());
            sendMessage(p.getName() + " is on a new case.");
            CaseEffect effect = c.getCaseEffect();
            if (effect == null) {
            } else if (effect instanceof QuestionEffect) {
                applyQuestionEffect(p);
            } else {
                Optional<String> res = effect.effect(p);
                res.ifPresent(this::sendMessage);
            }
        }
    }

    /**
     * Apply the question effect to a player
     *
     * @param p the player
     */
    public void applyQuestionEffect(Pawn p) {
        Objects.requireNonNull(p);
        Question q = session.questionDeck().drawCard();
        Objects.requireNonNull(q);
        String answer = view.playerAnswerToQuestion(p.getName(), q.getAskedQuestion());
        if (!q.checkAnswer(answer)) {
            p.setPosition(p.getPosition() - 3);
            sendMessage(p.getName() + " give a wrong answer.");
        } else
            sendMessage(p.getName() + " give a valid answer.");
    }

    /**
     * Main game loop
     */
    public void runGame() {
        Pawn winner = null;
        while (winner == null) {
            for(Pawn p: session.players()){
                try {
                    if (factory.getDaoType().equals("jpa")) {
                        InfoPlayerPOJO playerPOJO = ConvertorFactory.getConvertor(Pawn.class, InfoPlayerPOJO.class).toDTO(p);
                        DAO<InfoPlayerPOJO> playerDAO = factory.getDAO(InfoPlayerPOJO.class);
                        playerDAO.create(playerPOJO);
                    }
                } catch (DAOException | UnknownDAOException e) {
                    // Handle exception, maybe log it or display a message
                    sendMessage("Error saving player: " + p.getName() + " (" + e.getMessage() + ")");
                }
            }
            for (Pawn p : session.players()) {
                view.waitForDiceRoll(p.getName());
                playerRound(p);
                try{
                    if (factory.getDaoType().equals("jpa")) {
                        InfoPlayerPOJO playerPOJO = ConvertorFactory.getConvertor(Pawn.class, InfoPlayerPOJO.class).toDTO(p);
                        DAO<InfoPlayerPOJO> playerDAO = factory.getDAO(InfoPlayerPOJO.class);
                        playerDAO.update(playerPOJO);
                    }
                }catch (DAOException | UnknownDAOException e) {
                    // Handle exception, maybe log it or display a message
                    sendMessage("Error saving player: " + p.getName() + " (" + e.getMessage() + ")");
                }
                if (p.getPosition() == session.board().getSize()) {
                    winner = p;
                    break;
                }
            }
        }
        sendMessage(winner.getName() + " win the game.");
    }

    /**
     * Send message to the view
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        view.display(message);
    }

    /**
     * Get the list of questions
     *
     * @return the deck
     * @throws DAOException        if issues with DAO
     * @throws UnknownDAOException if DAO is unknown
     */
    public List<Question> loadQuestionList() throws DAOException, UnknownDAOException {
        DAO<QuestionPOJO> dao = factory.getDAO(QuestionPOJO.class);
        List<QuestionPOJO> questionsDto = dao.getAll();
        Convertor<Question, QuestionPOJO> convert = new QuestionConvertor();
        return questionsDto.stream().map(convert::fromDTO).toList();
    }

    /**
     * Accessor to the session
     */
    public GameSession getGameSession() {
        return session;
    }

    /**
     * Create human player from a list of names
     *
     * @param playerNames the list of names
     * @return the list of players
     */
    public List<Pawn> createPlayers(List<String> playerNames) {
        Objects.requireNonNull(playerNames);
        return playerNames
                .stream()
                .map(playerName -> new Pawn(playerName, Color.BLUE)).toList();
    }

    /**
     * Initialize the database and initialize
     *
     * @throws DBAccessException if access fails
     */
    public void initDatabase() throws DBAccessException {
        ConfigurationManager conf = ConfigurationManager.getInstance();
        if (conf.isInitDatabase()) {
            SQLScriptDB.runScriptOnDatabase("/question_db.sql");
            SQLScriptDB.runScriptOnDatabase("/game_session_db.sql");
        }
        if (conf.isPopulateDatabase()) {
            SQLScriptDB.runScriptOnDatabase("/questions_insert_db.sql");
        }
    }
}
