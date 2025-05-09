package org.example.data;

import org.example.controler.GameSession;
import org.example.model.business.Board;
import org.example.model.business.Deck;
import org.example.model.business.Pawn;
import org.example.model.business.Question;

public class ConvertorFactory {

    // Méthode pour obtenir le convertisseur approprié en fonction du modèle
    public static <T, D> Convertor<T, D> getConvertor(Class<T> modelClass, Class<D> dtoClass) {
        if (modelClass.equals(Board.class) && dtoClass.equals(BoardPOJO.class)) {
            return (Convertor<T, D>) new BoardConvertor();
        } else if (modelClass.equals(GameSession.class) && dtoClass.equals(GameSessionPOJO.class)) {
            return (Convertor<T, D>) new GameConvertor();
        } else if (modelClass.equals(Pawn.class) && dtoClass.equals(InfoPlayerPOJO.class)) {
            return (Convertor<T, D>) new InfoPlayerConvertor();
        } else if (modelClass.equals(Deck.class) && dtoClass.equals(QuestionDeckPOJO.class)) {
            return (Convertor<T, D>) new QuestionDeckConvertor();
        } else if (modelClass.equals(QuestionConvertor.class) && dtoClass.equals(QuestionPOJO.class)) {
            return (Convertor<T, D>) new QuestionConvertor();
        }

        throw new IllegalArgumentException("No converter found for " + modelClass.getName() + " to " + dtoClass.getName());
    }
}
