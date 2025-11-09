package edu.ptithcm.repository.employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ptithcm.configs.databases.Database;
import edu.ptithcm.model.EmployeeModel;


public class EmployeeRepositoryMySQL implements EmployeeRepository {
    public static EmployeeRepository instance;
    private EmployeeRepositoryMySQL(){

    }
    private static final String BASE_SELECT =
        "SELECT e.employee_id,e.password,e.username, e.name, e.phone, e.role, " +
        "b.name AS branch_name, e.start_at, e.end_at, e.status " +
        "FROM employee AS e " +
        "LEFT JOIN branch AS b ON e.branch_id = b.branch_id ";
 
    public static EmployeeRepository getInstance(){
        if  (EmployeeRepositoryMySQL.instance == null){
            EmployeeRepositoryMySQL.instance = new EmployeeRepositoryMySQL();
        }
        return EmployeeRepositoryMySQL.instance;
    }

    @Override
    public boolean checkEmployeeExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM employee WHERE username = ? LIMIT 1";
        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public EmployeeModel findByUsername(String username) throws SQLException {
        String sql = BASE_SELECT + "WHERE e.username = ? LIMIT 1";
        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEmployee(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void createEmployee(List<EmployeeModel> employees) throws SQLException {
        String sql = "INSERT INTO employee (employee_id, username, password, name, phone, role, branch_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = Database.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            for (EmployeeModel emp : employees) {
                ps.setString(1, emp.getId());
                ps.setString(2, emp.getUsername());
                ps.setString(3, emp.getPasswordHash());
                ps.setString(4, emp.getName());
                ps.setString(5, emp.getPhone());
                ps.setString(6, emp.getRole());
                if (emp.getBranchId() != null)
                    ps.setInt(7, emp.getBranchId());
                else
                    ps.setNull(7, Types.INTEGER);
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    @Override
    public void updateEmployee(String employee_id, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) return;

        StringBuilder sql = new StringBuilder("UPDATE employee SET ");
        List<Object> values = new ArrayList<>();

        for (String key : fields.keySet()) {
            sql.append(key).append(" = ?, ");
            values.add(fields.get(key));
        }
        sql.setLength(sql.length() - 2); // remove last comma
        sql.append(" WHERE employee_id = ?");

        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            ps.setString(values.size() + 1, employee_id);
            ps.executeUpdate();
        }
    }

    @Override
    public void removeEmployee(String employee_id) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, employee_id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<EmployeeModel> getAllEmployees(int limit) throws SQLException {
        List<EmployeeModel> employees = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY e.start_at DESC LIMIT ?";

        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        }
        return employees;
    }

    @Override
    public List<EmployeeModel> getAllEmployeesActive(int limit) throws SQLException {
        List<EmployeeModel> employees = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE e.status = TRUE ORDER BY e.start_at DESC LIMIT ?";

        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        }
        return employees;
    }

    @Override
    public List<EmployeeModel> getAllEmployeesUnactive(int limit) throws SQLException {
        List<EmployeeModel> employees = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE e.status = FALSE ORDER BY e.end_at DESC LIMIT ?";

        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        }
        return employees;
    }

    @Override
    public List<EmployeeModel> searchEmployees(String keyword) throws SQLException {
        List<EmployeeModel> employees = new ArrayList<>();
        String sql = BASE_SELECT +
            "WHERE e.username LIKE ? OR e.name LIKE ? OR e.phone LIKE ? ORDER BY e.start_at DESC";

        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        }
        return employees;
    }

    @Override
    public List<EmployeeModel> filterEmployees(Map<String, Object> filters) throws SQLException {
        List<EmployeeModel> employees = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT + "WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (filters.containsKey("role")) {
            sql.append("AND e.role = ? ");
            params.add(filters.get("role"));
        }
        if (filters.containsKey("branch_id")) {
            sql.append("AND e.branch_id = ? ");
            params.add(filters.get("branch_id"));
        }
        if (filters.containsKey("status")) {
            sql.append("AND e.status = ? ");
            params.add(filters.get("status"));
        }

        sql.append("ORDER BY e.start_at DESC");

        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        }
        return employees;
    }

    private EmployeeModel mapEmployee(ResultSet rs) throws SQLException {
        return new EmployeeModel.Builder()
                .id(rs.getString("employee_id"))
                .username(rs.getString("username"))
                .name(rs.getString("name"))
                .password(rs.getString("password"))
                .phone(rs.getString("phone"))
                .role(rs.getString("role"))
                .branch(rs.getString("branch_name"))
                .hireDate(rs.getString("start_at"))
                .status(rs.getBoolean("status"))
                .build();
    }
}
