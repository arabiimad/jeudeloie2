package org.example.stockage;

import org.example.data.InfoPlayerPOJO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class InfoPlayerJPA implements DAO<InfoPlayerPOJO> {

    private final EntityManager em;

    public InfoPlayerJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<InfoPlayerPOJO> get(int id) throws DAOException {
        try {
            InfoPlayerPOJO player = em.find(InfoPlayerPOJO.class, id);
            return Optional.ofNullable(player);
        } catch (Exception e) {
            throw new DAOException("Error retrieving player", e);
        }
    }

    @Override
    public List<InfoPlayerPOJO> getAll() throws DAOException {
        try {
            return em.createQuery("SELECT p FROM InfoPlayerPOJO p", InfoPlayerPOJO.class).getResultList();
        } catch (Exception e) {
            throw new DAOException("Error retrieving all players", e);
        }
    }

    @Override
    public int create(InfoPlayerPOJO infoPlayerPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.persist(infoPlayerPOJO);
            em.getTransaction().commit();
            return infoPlayerPOJO.getId();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error creating player", e);
        }
    }

    @Override
    public void update(InfoPlayerPOJO infoPlayerPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            em.merge(infoPlayerPOJO);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error updating player", e);
        }
    }

    @Override
    public void delete(InfoPlayerPOJO infoPlayerPOJO) throws DAOException {
        try {
            em.getTransaction().begin();
            InfoPlayerPOJO managed = em.contains(infoPlayerPOJO) ? infoPlayerPOJO : em.merge(infoPlayerPOJO);
            em.remove(managed);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new DAOException("Error deleting player", e);
        }
    }
}
