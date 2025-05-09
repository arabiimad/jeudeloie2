package org.example;

import org.example.stockage.ConfigurableDAOFactory;
import org.example.stockage.DAOFactory;
import org.example.view.ConsoleView;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntryPoint {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jeuDeLoiePU");
        EntityManager em = emf.createEntityManager();

        DAOFactory factory = new ConfigurableDAOFactory(em);

        ConsoleView view = new ConsoleView(factory);
        view.runGame();
    }
}