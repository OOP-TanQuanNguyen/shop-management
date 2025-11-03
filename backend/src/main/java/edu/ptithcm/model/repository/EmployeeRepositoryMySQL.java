package edu.ptithcm.model.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.ptithcm.configs.databases.Database;
import edu.ptithcm.model.EmployeeModel;

public class EmployeeRepositoryMySQL implements EmployeeRepository {
    @Override
    public boolean checkEmployeeExists(String username) throws SQLException{
        String query = "SELECT COUNT(*) AS count FROM employee WHERE username = ?";
        try (
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(query)
        ) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        }
        return false;
    }

    @Override
    public EmployeeModel findByUsername(String username) throws SQLException {
        String query = "SELECT b.name AS branch, e.username, e.password, e.name, e.phone,e.role, e.start_at, e.status"
            +" FROM employee AS e"
            +" LEFT JOIN branch AS b ON e.branch_id = b.branch_id"
             +" WHERE e.username = ? LIMIT 1";
        try (
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(query)
        ) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeModel.Builder()
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .role(rs.getString("role"))
                        .hireDate(rs.getString("start_at"))
                        .status(rs.getBoolean("status"))
                        .branch(rs.getString("branch"))
                        .build();
                }
            }
        }
        return null;
    }

    @Override
    public void createEmployee(List<EmployeeModel> employees) throws SQLException {
        String query = "INSERT INTO employee (employee_id, username, password, name, phone, role, branch_id)" 
                        +"VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(query)
        ) {
            conn.setAutoCommit(false); // dùng transaction để rollback nếu batch lỗi
            for (EmployeeModel emp : employees) {
                ps.setString(1, emp.getId());
                ps.setString(2, emp.getUsername());
                ps.setString(3, emp.getPasswordHash());
                ps.setString(4, emp.getName());
                ps.setString(5, emp.getPhone());
                ps.setString(6, emp.getRole());

                if (emp.getBranchId() != null && emp.getBranchId() > 0) {
                    ps.setInt(7, emp.getBranchId());
                } else {
                    ps.setNull(7, java.sql.Types.INTEGER); // cho phép null
                }
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            // rollback toàn bộ batch nếu 1 phần thất bại
            try (Connection conn = Database.getInstance().getConnection()) {
                conn.rollback();
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw e; // ném lỗi lên cho tầng service xử lý
        }
    }
    // cập nhật thông tin nhân viên
    public void updateEmployee(String employee_id, Map<String, Object> fields) throws SQLException {
    
    }
    // xoá nhân viên (xóa hoàn toàn)
    public void removeEmployee(String employee_id) throws SQLException {
        
    }

    // lấy danh sách tất cả nhân viên
    public List<EmployeeModel> getAllEmployees(int limit) throws SQLException {
        return null;
    }

    // lấy danh sách tất cả nhân viên còn làm việc
    public List<EmployeeModel> getAllEmployeesActive(int limit) throws SQLException {
        return null;
    }

    // lấy danh sách tất cả  nhân viên không còn làm việc
    public List<EmployeeModel> getAllEmployeesUnactive(int limit) throws SQLException {
        return null;
    }

    //search employee by name, username, phone
    public List<EmployeeModel> searchEmployees(String keyword) throws SQLException {
        return null;
    }

    // filter employee by role, branch, status
    public List<EmployeeModel> filterEmployees(Map<String, Object> filters) throws SQLException {
        return null;
    }

}
