package org.example.stockage;

import org.example.data.QuestionPOJO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class QuestionIMDAOTest {

    @Test
    void get () {
        QuestionIMDAO questionIMDAO = new QuestionIMDAO();
        QuestionPOJO QuestionPOJO = new QuestionPOJO( "What?", "yes");
        int id = questionIMDAO.create(QuestionPOJO);
        Optional<QuestionPOJO> optional = questionIMDAO.get(id);
        assertTrue(optional.isPresent());

        Optional<QuestionPOJO> optional2 = questionIMDAO.get(90);
        assertTrue(optional2.isEmpty());
    }

    @Test
    void get_and_create() {
        QuestionIMDAO questionIMDAO = new QuestionIMDAO();
        QuestionPOJO QuestionPOJO = new QuestionPOJO("What?", "yes");
        int id = questionIMDAO.create(QuestionPOJO);
        assertTrue(id >= 0);
        Optional<QuestionPOJO> res = questionIMDAO.get(id);
        assertTrue(res.isPresent());
        assertEquals("What?", res.get().getQuestion());
        assertEquals("yes", res.get().getAnswer());
    }

    @Test
    void delete() {
        QuestionIMDAO.clear();
        QuestionIMDAO questionIMDAO = new QuestionIMDAO();
        QuestionPOJO QuestionPOJO = new QuestionPOJO("What?", "yes");
        questionIMDAO.create(QuestionPOJO);

        QuestionPOJO QuestionPOJO2 = new QuestionPOJO( "What?", "yes");
        questionIMDAO.delete(QuestionPOJO2);
        Optional<QuestionPOJO> res = questionIMDAO.get(0);
        assertFalse(res.isPresent());
    }

    @Test
    void update() {
        QuestionIMDAO questionIMDAO = new QuestionIMDAO();
        QuestionPOJO QuestionPOJO = new QuestionPOJO("What?", "yes");
        int id = questionIMDAO.create(QuestionPOJO);

        QuestionPOJO QuestionPOJO2 = new QuestionPOJO("What?", "no");
        questionIMDAO.update(QuestionPOJO2);
        Optional<QuestionPOJO> res = questionIMDAO.get(id);
        assertTrue(res.isPresent());
        assertEquals("What?", res.get().getQuestion());
        assertEquals("no", res.get().getAnswer());

        QuestionPOJO QuestionPOJO3 = new QuestionPOJO( "What?", "no");
        questionIMDAO.update(QuestionPOJO3);
        Optional<QuestionPOJO> res2 = questionIMDAO.get(100);
        assertTrue(res2.isEmpty());
    }

    @Test
    void getAll() {
        QuestionIMDAO.clear();
        QuestionIMDAO questionIMDAO = new QuestionIMDAO();
        QuestionPOJO QuestionPOJO = new QuestionPOJO("What?", "yes");
        questionIMDAO.create(QuestionPOJO);

        List<QuestionPOJO> questions = questionIMDAO.getAll();
        assertEquals(1, questions.size());
        assertEquals("What?", questions.getFirst().getQuestion());
        assertEquals("yes", questions.getFirst().getAnswer());
    }
}