package org.example.stockage;

import org.example.data.BoardPOJO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class BoardJPA implements DAO<BoardPOJO> {

    private final EntityManager em;

    public BoardJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<BoardPOJO> get(int id) throws DAOException {
        try {
            BoardPOJO board = em.find(BoardPOJO.class, id);
            return Optional.ofNullable(board);
        } catch (Exception e) {
            throw new DAOException("Error retrieving board", e);
        }
    }

    @Override
    public List<BoardPOJO> getAll() throws DAOException {
        try {
            return em.createQuery("SELECT b FROM BoardPOJO b", BoardPOJO.class).getResultList();
        } catch (Exception e) {
            throw new DAOException("Error retrieving all boards", e);
        }
    }

    @Override
    public int create(BoardPOJO boardPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.merge(boardPOJO);
            em.getTransaction().commit();
            return boardPOJO.getId();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error creating board", e);
        }
    }

    @Override
    public void update(BoardPOJO boardPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.merge(boardPOJO);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error updating board", e);
        }
    }

    @Override
    public void delete(BoardPOJO boardPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            BoardPOJO managed = em.contains(boardPOJO) ? boardPOJO : em.merge(boardPOJO);
            em.remove(managed);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error deleting board", e);
        }
    }
}
