package edu.ptithcm.repository.invoice;

import edu.ptithcm.models.InvoiceModel;
import edu.ptithcm.repository.BaseRepository;
import edu.ptithcm.utils.BigDecimalUtil;
import java.util.List;
import org.hibernate.query.Query;

public class InvoiceRepositoryImpl
    extends BaseRepository<InvoiceModel>
    implements InvoiceRepository {

    // -------------------- SAVE --------------------
    @Override
    public void save(InvoiceModel entity) {
        execute(session -> {
            session.merge(entity);
            return null;
        });
    }

    // -------------------- UPDATE --------------------
    @Override
    public InvoiceModel update(InvoiceModel newData) {
        return execute(session -> {
            InvoiceModel managed = session.get(
                InvoiceModel.class,
                newData.getId()
            );
            if (managed == null) {
                return null;
            }

            // Dirty-checking: Hibernate tự flush khi commit
            if (newData.getEmployee() != null) {
                managed.setEmployee(newData.getEmployee());
            }
            if (newData.getBranch() != null) {
                managed.setBranch(newData.getBranch());
            }
            if (newData.getCustomer() != null) {
                managed.setCustomer(newData.getCustomer());
            }

            // BigDecimal so sánh hiệu quả
            if (!BigDecimalUtil.isZero(newData.getTotal())) {
                managed.setTotal(newData.getTotal());
            }
            if (!BigDecimalUtil.isZero(newData.getDiscount())) {
                managed.setDiscount(newData.getDiscount());
            }

            if (newData.getNote() != null && !newData.getNote().isBlank()) {
                managed.setNote(newData.getNote());
            }

            if (
                newData.getDetails() != null && !newData.getDetails().isEmpty()
            ) {
                managed.setDetails(newData.getDetails());
            }

            return managed;
        });
    }

    // -------------------- DELETE --------------------
    @Override
    public InvoiceModel delete(String id) {
        return execute(session -> {
            InvoiceModel managed = session.get(InvoiceModel.class, id);
            if (managed == null) {
                return null;
            }
            session.remove(managed);
            return managed;
        });
    }

    // -------------------- FIND BY ID --------------------
    @Override
    public InvoiceModel findById(String id) {
        return execute(session -> session.get(InvoiceModel.class, id));
    }

    @Override
    public List<InvoiceModel> findAll() {
        return execute(session -> {
            Query<InvoiceModel> query = session.createQuery(
                "SELECT DISTINCT i FROM InvoiceModel i " +
                    "LEFT JOIN FETCH i.employee e " +
                    "LEFT JOIN FETCH i.branch b " +
                    "LEFT JOIN FETCH i.customer c " +
                    "LEFT JOIN FETCH i.details d " +
                    "LEFT JOIN FETCH d.product p " +
                    "ORDER BY i.createdAt DESC",
                InvoiceModel.class
            );

            return query.list();
        });
    }

    // -------------------- FIND BY CUSTOMER --------------------
    @Override
    public List<InvoiceModel> findByCustomer(String customerId) {
        return execute(session -> {
            Query<InvoiceModel> query = session.createQuery(
                "SELECT i FROM InvoiceModel i " +
                    "JOIN FETCH i.customer c " +
                    "WHERE c.id = :customerId " +
                    "ORDER BY i.createdAt DESC",
                InvoiceModel.class
            );
            query.setParameter("customerId", customerId);
            return query.list();
        });
    }

    // -------------------- FIND BY BRANCH --------------------
    @Override
    public List<InvoiceModel> findByBranch(Integer branchId) {
        return execute(session -> {
            Query<InvoiceModel> query = session.createQuery(
                "SELECT i FROM InvoiceModel i " +
                    "JOIN FETCH i.branch b " +
                    "WHERE b.id = :branchId " +
                    "ORDER BY i.createdAt DESC",
                InvoiceModel.class
            );
            query.setParameter("branchId", branchId);
            return query.list();
        });
    }

    // -------------------- FIND BY EMPLOYEE --------------------
    @Override
    public List<InvoiceModel> findByEmployee(String employeeId) {
        System.out.println("EmployeeId repository: " + employeeId);

        return execute(session -> {
            Query<InvoiceModel> query = session.createQuery(
                "SELECT DISTINCT i FROM InvoiceModel i " +
                    "JOIN FETCH i.employee e " +
                    "JOIN FETCH i.branch b " +
                    "LEFT JOIN FETCH i.customer c " +
                    "LEFT JOIN FETCH i.details d " +
                    "LEFT JOIN FETCH d.product p " +
                    "WHERE e.id = :employeeId " +
                    "ORDER BY i.createdAt DESC",
                InvoiceModel.class
            );

            query.setParameter("employeeId", employeeId);
            List<InvoiceModel> result = query.list();

            System.out.println("Repository full fetch result = " + result);

            return result;
        });
    }
}
