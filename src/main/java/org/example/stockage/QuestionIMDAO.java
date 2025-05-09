package org.example.stockage;

import org.example.data.QuestionPOJO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In memory DAO for question
 */
public class QuestionIMDAO implements DAO<QuestionPOJO> {

    /**
     * Counter for identifier
     */
    private static int counter = 0;

    /**
     * The array of questions
     */
    private static final List<QuestionPOJO> questions = new ArrayList<>();

    /**
     * Mutex for protection
     */
    private static final Object mutex = new Object();

    /**
     * Populate with some questions
     */
    public static void clearAndPopulate() {
        synchronized (mutex) {
            questions.clear();
        }
        QuestionIMDAO questionIMDAO = new QuestionIMDAO();
        questionIMDAO.create(new QuestionPOJO("What is the capital of Great Britain?", "London"));
        questionIMDAO.create(new QuestionPOJO("When is year 0 for Unix time?", "1970"));
        questionIMDAO.create(new QuestionPOJO("Who is the first president of the fifth French republic?", "De Gaulle"));
    }

    /**
     * Clear the database
     */
    public static void clear () {
        synchronized (mutex) {
            questions.clear();
        }
    }

    @Override
    public int create(QuestionPOJO question) {
        QuestionPOJO newQuestion;
        synchronized (mutex) {
            newQuestion = new QuestionPOJO(question.getQuestion(), question.getAnswer());
            questions.add(newQuestion);
            counter++;
        }
        return newQuestion.getId();
    }

    @Override
    public void delete(QuestionPOJO question) {
        questions.remove(question);
    }

    @Override
    public void update(QuestionPOJO question) {
        Optional<QuestionPOJO> oldQuestion = questions.stream().filter(q -> q.getId() == question.getId()).findAny();
        if (oldQuestion.isPresent()) {
            synchronized (mutex) {
                questions.remove(oldQuestion.get());
                questions.add(question);
            }
        }
    }

    @Override
    public Optional<QuestionPOJO> get(int id) {
        return questions.stream().filter(q -> q.getId() == id).findAny();
    }

    @Override
    public List<QuestionPOJO> getAll() {
        return questions.stream().toList();
    }
}
