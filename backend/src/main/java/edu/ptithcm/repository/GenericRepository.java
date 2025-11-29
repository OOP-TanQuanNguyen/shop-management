package edu.ptithcm.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
    void save(T entity);
    T update(T entity);
    T delete(ID id);
    T findById(ID id);
    List<T> findAll();
}
