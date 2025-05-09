package org.example.data;

import org.example.model.business.Question;

public class QuestionConvertor implements Convertor<Question, QuestionPOJO> {

    @Override
    public Question fromDTO(QuestionPOJO dto) {
        return new Question(dto.getId(), dto.getQuestion(), dto.getAnswer());
    }

    @Override
    public QuestionPOJO toDTO(Question q) {
        return new QuestionPOJO(q.getAskedQuestion(), q.getAnswer());
    }
}
