package org.example.stockage;

public interface DAOFactory {
    <T> DAO<T> getDAO(Class<T> clazz) throws UnknownDAOException;

    String getDaoType();
}
