package edu.ptithcm.repository.shift;

import java.util.List;

import org.hibernate.query.Query;
import edu.ptithcm.models.ShiftModel;
import edu.ptithcm.repository.BaseRepository;

public class ShiftRepositoryImpl extends BaseRepository<ShiftModel> implements ShiftRepository {

    // -------------------- SAVE --------------------
    @Override
    public void save(ShiftModel entity) {
        execute(session -> {
            session.persist(entity);
            return null;
        });
    }

    // -------------------- UPDATE --------------------
    @Override
    public ShiftModel update(ShiftModel newData) {
        return execute(session -> {
            ShiftModel managed = session.get(ShiftModel.class, newData.getId());
            if (managed == null) return null;

            // Chỉ set khi có dữ liệu mới
            if (newData.getName() != null) managed.setName(newData.getName());
            if (newData.getStartTime() != null) managed.setStartTime(newData.getStartTime());
            if (newData.getEndTime() != null) managed.setEndTime(newData.getEndTime());
            if (newData.getAssignments() != null) managed.setAssignments(newData.getAssignments());

            return managed;
        });
    }

    // -------------------- DELETE --------------------
    @Override
    public ShiftModel delete(Integer id) {
        return execute(session -> {
            ShiftModel managed = session.get(ShiftModel.class, id);
            if (managed == null) return null;
            session.remove(managed);
            return managed;
        });
    }

    // -------------------- FIND BY ID --------------------
    @Override
    public ShiftModel findById(Integer id) {
        return execute(session -> session.get(ShiftModel.class, id));
    }

    // -------------------- FIND ALL --------------------
    @Override
    public List<ShiftModel> findAll() {
        return execute(session -> session.createQuery(
            "FROM ShiftModel s ORDER BY s.id ASC", ShiftModel.class
        ).list());
    }

    // -------------------- FIND BY NAME --------------------
    @Override
    public List<ShiftModel> findByName(String name) {
        return execute(session -> {
            Query<ShiftModel> query = session.createQuery(
                "FROM ShiftModel s WHERE LOWER(s.name) LIKE LOWER(:name) ORDER BY s.name ASC",
                ShiftModel.class
            );
            query.setParameter("name", "%" + name + "%");
            query.setMaxResults(50);
            return query.list();
        });
    }
}
