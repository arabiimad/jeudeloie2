package org.example.stockage;

import org.example.data.InfoPlayerPOJO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InfoPlayerJPATest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private InfoPlayerJPA dao;

    @BeforeEach
    void setUp() {
        // Créez un EntityManager pour le test à partir du persistence unit "testPU"
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();
        dao = new InfoPlayerJPA(em);
    }

    @AfterEach
    void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void testCreateAndGet() {
        try {
            // Création d'un joueur
            InfoPlayerPOJO player = new InfoPlayerPOJO();
            player.setName("John");
            player.setPosition(1);
            player.setColor(0);
            player.setScore(100);

            int id = dao.create(player);
            assertTrue(id > 0, "L'ID doit être positif après création");

            // Récupération du joueur créé
            Optional<InfoPlayerPOJO> retrieved = dao.get(id);
            assertTrue(retrieved.isPresent(), "Le joueur doit être présent");
            InfoPlayerPOJO p = retrieved.get();
            assertEquals("John", p.getName(), "Le nom du joueur doit être 'John'");
            assertEquals(1, p.getPosition(), "La position doit être 1");
            assertEquals(0, p.getColor(), "La couleur doit être 0");
            assertEquals(100, p.getScore(), "Le score doit être 100");
        } catch (DAOException e) {
            fail("Exception dans testCreateAndGet: " + e.getMessage());
        }
    }

    @Test
    void testUpdate() {
        try {
            // Création d'un joueur
            InfoPlayerPOJO player = new InfoPlayerPOJO();
            player.setName("Alice");
            player.setPosition(2);
            player.setColor(1);
            player.setScore(50);
            int id = dao.create(player);

            // Mise à jour : modification du score
            Optional<InfoPlayerPOJO> optPlayer = dao.get(id);
            assertTrue(optPlayer.isPresent(), "Le joueur doit être présent pour la mise à jour");

            InfoPlayerPOJO toUpdate = optPlayer.get();
            toUpdate.setScore(75);
            dao.update(toUpdate);

            // Vérification de la mise à jour
            Optional<InfoPlayerPOJO> updated = dao.get(id);
            assertTrue(updated.isPresent(), "Le joueur mis à jour doit être retrouvé");
            assertEquals(75, updated.get().getScore(), "Le score mis à jour doit être 75");
        } catch (DAOException e) {
            fail("Exception dans testUpdate: " + e.getMessage());
        }
    }

    @Test
    void testGetAllAndDelete() {
        try {
            // Création de deux joueurs
            InfoPlayerPOJO player1 = new InfoPlayerPOJO();
            player1.setName("Bob");
            player1.setPosition(1);
            player1.setColor(2);
            player1.setScore(10);
            int id1 = dao.create(player1);

            InfoPlayerPOJO player2 = new InfoPlayerPOJO();
            player2.setName("Carol");
            player2.setPosition(2);
            player2.setColor(3);
            player2.setScore(20);
            int id2 = dao.create(player2);

            // Récupération de tous les joueurs
            List<InfoPlayerPOJO> players = dao.getAll();
            assertNotNull(players, "La liste des joueurs ne doit pas être nulle");
            assertTrue(players.size() >= 2, "La liste doit contenir au moins 2 joueurs");

            // Suppression du premier joueur
            Optional<InfoPlayerPOJO> optPlayer1 = dao.get(id1);
            assertTrue(optPlayer1.isPresent(), "Le premier joueur doit être présent avant suppression");
            dao.delete(optPlayer1.get());

            // Vérifier que le joueur supprimé n'est plus présent
            List<InfoPlayerPOJO> playersAfterDelete = dao.getAll();
            boolean found = playersAfterDelete.stream().anyMatch(p -> p.getId() == id1);
            assertFalse(found, "Le joueur avec l'ID " + id1 + " ne doit plus exister");
        } catch (DAOException e) {
            fail("Exception dans testGetAllAndDelete: " + e.getMessage());
        }
    }
}
