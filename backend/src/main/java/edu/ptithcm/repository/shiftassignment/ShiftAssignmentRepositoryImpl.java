package edu.ptithcm.repository.shiftassignment;

import java.util.List;

import org.hibernate.query.Query;

import edu.ptithcm.models.ShiftAssignmentModel;
import edu.ptithcm.models.ShiftAssignmentId;
import edu.ptithcm.repository.BaseRepository;

public class ShiftAssignmentRepositoryImpl extends BaseRepository<ShiftAssignmentModel> implements ShiftAssignmentRepository {

    @Override
    public void save(ShiftAssignmentModel entity) {
        execute(session -> { session.persist(entity); return null; });
    }

    @Override
    public ShiftAssignmentModel update(ShiftAssignmentModel newData) {
        return execute(session -> {
            ShiftAssignmentModel managed = session.get(ShiftAssignmentModel.class,
                    new ShiftAssignmentId(
                        newData.getShift().getId(),
                        newData.getEmployee().getId(),
                        newData.getBranch().getId()
                    )
            );
            if (managed == null) return null;
            
            return managed;
        });
    }

    @Override
    public ShiftAssignmentModel delete(ShiftAssignmentId id) {
        return execute(session -> {
            ShiftAssignmentModel managed = session.get(ShiftAssignmentModel.class, id);
            if (managed == null) return null;
            session.remove(managed);
            return managed;
        });
    }

    @Override
    public ShiftAssignmentModel findById(ShiftAssignmentId id) {
        return execute(session -> session.get(ShiftAssignmentModel.class, id));
    }

    @Override
    public List<ShiftAssignmentModel> findAll() {
        return execute(session -> session.createQuery("FROM ShiftAssignmentModel", ShiftAssignmentModel.class).list());
    }

    @Override
    public List<ShiftAssignmentModel> findByShift(Integer shiftId) {
        return execute(session -> {
            Query<ShiftAssignmentModel> query = session.createQuery(
                "FROM ShiftAssignmentModel s WHERE s.shift.id = :shiftId", ShiftAssignmentModel.class
            );
            query.setParameter("shiftId", shiftId);
            return query.list();
        });
    }

    @Override
    public List<ShiftAssignmentModel> findByEmployee(String employeeId) {
        return execute(session -> {
            Query<ShiftAssignmentModel> query = session.createQuery(
                "FROM ShiftAssignmentModel s WHERE s.employee.id = :employeeId", ShiftAssignmentModel.class
            );
            query.setParameter("employeeId", employeeId);
            return query.list();
        });
    }

    @Override
    public List<ShiftAssignmentModel> findByBranch(Integer branchId) {
        return execute(session -> {
            Query<ShiftAssignmentModel> query = session.createQuery(
                "FROM ShiftAssignmentModel s WHERE s.branch.id = :branchId", ShiftAssignmentModel.class
            );
            query.setParameter("branchId", branchId);
            return query.list();
        });
    }
}
