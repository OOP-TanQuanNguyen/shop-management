package edu.ptithcm.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.function.Function;
import edu.ptithcm.configs.databases.HibernateUtil;

public abstract class BaseRepository<T> {

    protected <R> R execute(Function<Session, R> action) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            R result = action.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    protected void executeVoid(Function<Session, Void> action) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            action.apply(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
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
