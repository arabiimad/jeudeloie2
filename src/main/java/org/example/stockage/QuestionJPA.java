package org.example.stockage;

import org.example.data.QuestionPOJO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class QuestionJPA implements DAO<QuestionPOJO> {

    private final EntityManager em;

    public QuestionJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<QuestionPOJO> get(int id) throws DAOException {
        try {
            QuestionPOJO question = em.find(QuestionPOJO.class, id);
            return Optional.ofNullable(question);
        } catch (Exception e) {
            throw new DAOException("Error retrieving question", e);
        }
    }

    @Override
    public List<QuestionPOJO> getAll() throws DAOException {
        try {
            return em.createQuery("SELECT q FROM QuestionPOJO q", QuestionPOJO.class).getResultList();
        } catch (Exception e) {
            throw new DAOException("Error retrieving all questions", e);
        }
    }

    @Override
    public int create(QuestionPOJO questionPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.persist(questionPOJO);
            em.getTransaction().commit();
            return questionPOJO.getId();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error creating question", e);
        }
    }

    @Override
    public void update(QuestionPOJO questionPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.merge(questionPOJO);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error updating question", e);
        }
    }

    @Override
    public void delete(QuestionPOJO questionPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            QuestionPOJO managed = em.contains(questionPOJO) ? questionPOJO : em.merge(questionPOJO);
            em.remove(managed);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error deleting question", e);
        }
    }


    public void insertSampleQuestions() {
        try {
            em.getTransaction().begin();

            // Create some sample data for the QuestionPOJO table
            QuestionPOJO question1 = new QuestionPOJO();
            question1.setQuestion("What is the capital of Great Britain?");
            question1.setAnswer("London");

            QuestionPOJO question2 = new QuestionPOJO();
            question2.setQuestion("When is year 0 for Unix time?");
            question2.setAnswer("1970");

            QuestionPOJO question3 = new QuestionPOJO();
            question3.setQuestion("Who is the first president of the fifth French republic?");
            question3.setAnswer("de Gaulle");

            // Persist the questions
            em.persist(question1);
            em.persist(question2);
            em.persist(question3);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error while inserting sample data: " + e.getMessage());
        }
    }

}
