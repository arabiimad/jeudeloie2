package org.example.data;

import org.example.model.business.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionConvertorTest {

    @Test
    void fromDTO() {
        QuestionPOJO QuestionPOJO = new QuestionPOJO( "What ?", "yes");
        QuestionConvertor convertor = new QuestionConvertor();
        Question question = convertor.fromDTO(QuestionPOJO);
        assertNotNull(question);
        assertEquals(QuestionPOJO.getQuestion(), question.getAskedQuestion());
        assertEquals(QuestionPOJO.getAnswer(), question.getAnswer());
        assertEquals(QuestionPOJO.getId(), question.getId());
    }

    @Test
    void toDTO() {
        Question question = new Question(-1, "What ?", "yes");
        QuestionConvertor convertor = new QuestionConvertor();
        QuestionPOJO QuestionPOJO = convertor.toDTO(question);
        assertNotNull(QuestionPOJO);
        assertEquals(question.getAskedQuestion(), QuestionPOJO.getQuestion());
        assertEquals(question.getAnswer(), QuestionPOJO.getAnswer());
        assertEquals(question.getId(), QuestionPOJO.getId());
    }
}