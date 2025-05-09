package org.example.stockage;

import org.example.data.GameSessionPOJO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionDAOTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        try {
            SQLScriptDB.runScriptOnDatabase("/tests/game_session_clean.sql");
            SQLScriptDB.runScriptOnDatabase("/game_session_db.sql");
            SQLScriptDB.runScriptOnDatabase("/tests/game_session_data.sql");
        } catch (DBAccessException e) {
            fail(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            SQLScriptDB.runScriptOnDatabase("/tests/game_session_clean.sql");
        }
        catch (DBAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void get() {
        try {
            GameSessionJPA GameSessionJPA = new GameSessionJPA(em);
            Optional<GameSessionPOJO> pojo = GameSessionJPA.get(0);
            assertTrue(pojo.isPresent());
            assertEquals(2, pojo.get().getPlayers().size());
            assertEquals("Tom", pojo.get().getPlayers().getFirst().getName());
            Optional<GameSessionPOJO> pojo2 = GameSessionJPA.get(1);
            assertTrue(pojo2.isEmpty());
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getAll() {
        try {
            GameSessionJPA GameSessionJPA = new GameSessionJPA(em);
            List<GameSessionPOJO> pojos = GameSessionJPA.getAll();
            assertNotNull(pojos);
            assertFalse(pojos.isEmpty());
            assertEquals(2, pojos.getFirst().getPlayers().size());
            assertEquals("Tom", pojos.getFirst().getPlayers().getFirst().getName());
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void create() {
    }

    @Test
    void delete() {
        try {
            GameSessionJPA GameSessionJPA = new GameSessionJPA(em);
            Optional<GameSessionPOJO> pojo = GameSessionJPA.get(0);
            assertTrue(pojo.isPresent());
            GameSessionJPA.delete(pojo.get());
            Optional<GameSessionPOJO> pojo2 = GameSessionJPA.get(0);
            assertTrue(pojo2.isEmpty());
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void update() {
    }
}