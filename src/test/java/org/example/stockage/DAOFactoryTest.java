package org.example.stockage;

import org.example.data.QuestionPOJO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAOFactoryTest {

    /**
     * This is a simple test implementation of our DAOFactory interface.
     * It uses a provided DAO type string to decide which DAO to create for QuestionPOJO.
     * For "jpa", it accepts an EntityManager instance (can be null if not used).
     */
    static class TestDAOFactory implements DAOFactory {
        private final String daoType;
        private final javax.persistence.EntityManager em; // Used only for the "jpa" case

        public TestDAOFactory(javax.persistence.EntityManager em, String daoType) {
            this.em = em;
            this.daoType = daoType;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> DAO<T> getDAO(Class<T> clazz) throws UnknownDAOException {
            if (clazz.equals(QuestionPOJO.class)) {
                return (DAO<T>) switch (daoType) {
                    case "memory" -> new QuestionIMDAO();
                    case "memoryWithInit" -> {
                        QuestionIMDAO.clearAndPopulate();
                        yield new QuestionIMDAO();
                    }
                    case "jdbc" -> new QuestionJDBCDAO();
                    case "jpa" -> new QuestionJPA(em);
                    default -> throw new UnknownDAOException("DAO type not found: " + daoType);
                };
            }
            throw new UnknownDAOException("No DAO available for class: " + clazz.getName());
        }

        @Override
        public String getDaoType() {
            return "";
        }
    }

    @Test
    void testUnknownDAO() {
        // For an unknown type (empty string), expect an UnknownDAOException
        DAOFactory factory = new TestDAOFactory(null, "");
        assertThrows(UnknownDAOException.class, () -> factory.getDAO(QuestionPOJO.class));
    }

    @Test
    void testJdbcDAO() {
        // "jdbc" case: no EntityManager is required.
        DAOFactory factory = new TestDAOFactory(null, "jdbc");
        try {
            DAO<QuestionPOJO> jdbcDao = factory.getDAO(QuestionPOJO.class);
            assertNotNull(jdbcDao);
        } catch (UnknownDAOException e) {
            fail(e);
        }
    }

    @Test
    void testMemoryDAO() {
        // "memory" case: check that getAll() returns a non-null list.
        DAOFactory factory = new TestDAOFactory(null, "memory");
        try {
            DAO<QuestionPOJO> memDao = factory.getDAO(QuestionPOJO.class);
            assertNotNull(memDao);
            List<QuestionPOJO> listq1 = memDao.getAll();
            assertNotNull(listq1);
        } catch (DAOException | UnknownDAOException e) {
            fail(e);
        }
    }

    @Test
    void testMemoryWithInitDAO() {
        // "memoryWithInit" case: the DAO should be initialized with 3 entries.
        DAOFactory factory = new TestDAOFactory(null, "memoryWithInit");
        try {
            DAO<QuestionPOJO> memDao2 = factory.getDAO(QuestionPOJO.class);
            assertNotNull(memDao2);
            List<QuestionPOJO> listq2 = memDao2.getAll();
            assertEquals(3, listq2.size());
            // Confirm the DAO type is our in-memory DAO implementation.
            assertInstanceOf(QuestionIMDAO.class, memDao2);
        } catch (DAOException | UnknownDAOException e) {
            fail(e);
        }
    }
}
