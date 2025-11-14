package edu.ptithcm.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.function.Function;
import edu.ptithcm.configs.databases.HibernateUtil;

public abstract class BaseRepository<T> {

    protected <R> R execute(Function<Session, R> action) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getInstance().getSessionFactory().openSession();
            tx = session.beginTransaction();

            R result = action.apply(session);

            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (RuntimeException rollbackEx) {
                    throw rollbackEx;
                }
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    protected void executeVoid(Function<Session, Void> action) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getInstance().getSessionFactory().openSession();
            tx = session.beginTransaction();

            action.apply(session);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (RuntimeException rollbackEx) {
                    throw rollbackEx;
                }
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    protected List<T> queryList(String hql, Class<T> clazz, Function<Query<T>, Void> configurator) {
        return execute(session -> {
            Query<T> query = session.createQuery(hql, clazz);
            configurator.apply(query);
            return query.list();
        });
    }
}
