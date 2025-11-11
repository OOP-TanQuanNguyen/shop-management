package edu.ptithcm.repository.employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ptithcm.model.EmployeeModel;
import edu.ptithcm.repository.BaseRepository;

public class EmployeeRepositoryMySQL extends BaseRepository implements EmployeeRepository {

    private static EmployeeRepositoryMySQL instance;

    private static final String BASE_SELECT =
        "SELECT e.employee_id, e.username, e.password, e.name, e.phone, e.role, " +
        "b.name AS branch_name, e.start_at, e.end_at, e.status " +
        "FROM employee AS e " +
        "LEFT JOIN branch AS b ON e.branch_id = b.branch_id ";

    private EmployeeRepositoryMySQL() {}

    public static EmployeeRepositoryMySQL getInstance() {
        if (instance == null) instance = new EmployeeRepositoryMySQL();
        return instance;
    }

    @Override
    public boolean checkEmployeeExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM employee WHERE username = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public EmployeeModel findByUsername(String username) throws SQLException {
        String sql = BASE_SELECT + "WHERE e.username = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapEmployee(rs);
            }
        }
        return null;
    }

    @Override
    public void createEmployee(List<EmployeeModel> employees) throws SQLException {
        String sql = "INSERT INTO employee (employee_id, username, password, name, phone, role, branch_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            for (EmployeeModel e : employees) {
                ps.setString(1, e.getId());
                ps.setString(2, e.getUsername());
                ps.setString(3, e.getPasswordHash());
                ps.setString(4, e.getName());
                ps.setString(5, e.getPhone());
                ps.setString(6, e.getRole());
                if (e.getBranchId() != null)
                    ps.setInt(7, e.getBranchId());
                else
                    ps.setNull(7, Types.INTEGER);
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

        } catch (SQLException ex) {
            safeRollback(conn);
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            closeConnection(conn);
        }
    }

    @Override
    public void updateEmployee(String employeeId, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) return;

        StringBuilder sql = new StringBuilder("UPDATE employee SET ");
        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, Object> e : fields.entrySet()) {
            sql.append(e.getKey()).append(" = ?, ");
            values.add(e.getValue());
        }
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE employee_id = ?");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < values.size(); i++) ps.setObject(i + 1, values.get(i));
            ps.setString(values.size() + 1, employeeId);
            ps.executeUpdate();
        }
    }

    @Override
    public void removeEmployee(String employeeId) throws SQLException {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<EmployeeModel> getAllEmployees(int limit) throws SQLException {
        List<EmployeeModel> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY e.start_at DESC LIMIT ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapEmployee(rs));
            }
        }
        return list;
    }

    @Override
    public List<EmployeeModel> getAllEmployeesActive(int limit) throws SQLException {
        List<EmployeeModel> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE e.status = TRUE ORDER BY e.start_at DESC LIMIT ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapEmployee(rs));
            }
        }
        return list;
    }

    @Override
    public List<EmployeeModel> getAllEmployeesUnactive(int limit) throws SQLException {
        List<EmployeeModel> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE e.status = FALSE ORDER BY e.end_at DESC LIMIT ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapEmployee(rs));
            }
        }
        return list;
    }

    @Override
    public List<EmployeeModel> searchEmployees(String keyword) throws SQLException {
        List<EmployeeModel> list = new ArrayList<>();
        String sql = BASE_SELECT +
            "WHERE e.username LIKE ? OR e.name LIKE ? OR e.phone LIKE ? ORDER BY e.start_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapEmployee(rs));
            }
        }
        return list;
    }

    @Override
    public List<EmployeeModel> filterEmployees(Map<String, Object> filters) throws SQLException {
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

        List<EmployeeModel> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapEmployee(rs));
            }
        }
        return list;
    }

    // 🔹 Mapper
    private EmployeeModel mapEmployee(ResultSet rs) throws SQLException {
        return new EmployeeModel.Builder()
                .id(rs.getString("employee_id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .name(rs.getString("name"))
                .phone(rs.getString("phone"))
                .role(rs.getString("role"))
                .branch(rs.getString("branch_name"))
                .hireDate(rs.getString("start_at"))
                .endDate(rs.getString("end_at"))
                .status(rs.getBoolean("status"))
                .build();
    }
}
