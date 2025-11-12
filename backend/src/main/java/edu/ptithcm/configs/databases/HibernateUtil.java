package edu.ptithcm.configs.databases;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;

public class HibernateUtil {
    private static HibernateUtil instance;
    private EntityManagerFactory emf;

    private HibernateUtil() {}

    public static synchronized HibernateUtil getInstance() {
        if (instance == null) instance = new HibernateUtil();
        return instance;
    }

    public synchronized void init() {
        if (emf == null || !emf.isOpen()) {
            System.out.println("[ORM] Initializing Hibernate EntityManagerFactory...");
            emf = Persistence.createEntityManagerFactory("ShopManagementPU");
            System.out.println("[ORM] Hibernate EntityManagerFactory initialized successfully!");
        }
    }

    public EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) init();
        return emf;
    }

    public SessionFactory getSessionFactory() {
        return getEntityManagerFactory().unwrap(SessionFactory.class);
    }

    public synchronized void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("[ORM] EntityManagerFactory closed.");
        }
    }
}
