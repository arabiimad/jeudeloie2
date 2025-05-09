package org.example.stockage;

import org.example.config.ConfigurationManager;
import org.example.data.*;

import javax.persistence.EntityManager;

public class ConfigurableDAOFactory implements DAOFactory {

    private final EntityManager em;
    private final String daoType;

    public ConfigurableDAOFactory(EntityManager em) {
        this.em = em;
        this.daoType = ConfigurationManager.getInstance().getDaoType(); // "memory", "jdbc", "jpa"
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DAO<T> getDAO(Class<T> clazz) throws UnknownDAOException {

        if (clazz.equals(QuestionPOJO.class)) {
            return (DAO<T>) getQuestionDAO(daoType);
        }

        if (daoType.equals("jpa")) {
            if (clazz.equals(GameSessionPOJO.class)) return (DAO<T>) new GameSessionJPA(em);
            if (clazz.equals(BoardPOJO.class)) return (DAO<T>) new BoardJPA(em);
            if (clazz.equals(InfoPlayerPOJO.class)) return (DAO<T>) new InfoPlayerJPA(em);
            if (clazz.equals(QuestionDeckPOJO.class)) return (DAO<T>) new QuestionDeckJPA(em);
        }

        throw new UnknownDAOException("No DAO found for class: " + clazz + " with type: " + daoType);
    }

    private DAO<QuestionPOJO> getQuestionDAO(String type) throws UnknownDAOException {
        return switch (type) {
            case "memory" -> new QuestionIMDAO();
            case "memoryWithInit" -> {
                QuestionIMDAO.clearAndPopulate();
                yield new QuestionIMDAO();
            }
            case "jdbc" -> new QuestionJDBCDAO();
            case "jpa" -> new QuestionJPA(em);
            default -> throw new UnknownDAOException("DAO type for QuestionPOJO not found: " + type);
        };
    }

    public String getDaoType() {
        return daoType;
    }
}
