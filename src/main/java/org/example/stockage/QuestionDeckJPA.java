package org.example.stockage;

import org.example.data.QuestionDeckPOJO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class QuestionDeckJPA implements DAO<QuestionDeckPOJO> {

    private final EntityManager em;

    public QuestionDeckJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<QuestionDeckPOJO> get(int id) throws DAOException {
        try {
            QuestionDeckPOJO deck = em.find(QuestionDeckPOJO.class, id);
            return Optional.ofNullable(deck);
        } catch (Exception e) {
            throw new DAOException("Error retrieving question deck", e);
        }
    }

    @Override
    public List<QuestionDeckPOJO> getAll() throws DAOException {
        try {
            return em.createQuery("SELECT d FROM QuestionDeckPOJO d", QuestionDeckPOJO.class).getResultList();
        } catch (Exception e) {
            throw new DAOException("Error retrieving all question decks", e);
        }
    }

    @Override
    public int create(QuestionDeckPOJO questionDeckPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.persist(questionDeckPOJO);
            em.getTransaction().commit();
            return questionDeckPOJO.getId();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error creating question deck", e);
        }
    }

    @Override
    public void update(QuestionDeckPOJO questionDeckPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.merge(questionDeckPOJO);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error updating question deck", e);
        }
    }

    @Override
    public void delete(QuestionDeckPOJO questionDeckPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            QuestionDeckPOJO managed = em.contains(questionDeckPOJO) ? questionDeckPOJO : em.merge(questionDeckPOJO);
            em.remove(managed);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error deleting question deck", e);
        }
    }
}
