package org.example.stockage;

import org.example.data.GameSessionPOJO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class GameSessionJPA implements DAO<GameSessionPOJO> {

    private final EntityManager em;

    public GameSessionJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<GameSessionPOJO> get(int id) throws DAOException {
        try {
            GameSessionPOJO pojo = em.find(GameSessionPOJO.class, id);
            return Optional.ofNullable(pojo);
        } catch (Exception e) {
            throw new DAOException("Error retrieving game session", e);
        }
    }

    @Override
    public List<GameSessionPOJO> getAll() throws DAOException {
        try {
            return em.createQuery("SELECT g FROM GameSessionPOJO g", GameSessionPOJO.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Error retrieving game sessions", e);
        }
    }

    @Override
    public int create(GameSessionPOJO gameSessionPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.merge(gameSessionPOJO);
            em.getTransaction().commit();
            return gameSessionPOJO.getId();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new DAOException("Error creating game session", e);
        }
    }

    @Override
    public void update(GameSessionPOJO gameSessionPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.merge(gameSessionPOJO);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new DAOException("Error updating game session", e);
        }
    }

    @Override
    public void delete(GameSessionPOJO gameSessionPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            GameSessionPOJO managed = em.merge(gameSessionPOJO);
            em.remove(managed);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw new DAOException("Error deleting game session", e);
        }
    }
}
