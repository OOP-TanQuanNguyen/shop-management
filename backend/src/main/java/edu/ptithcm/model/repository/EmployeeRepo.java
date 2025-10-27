package edu.ptithcm.model.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import edu.ptithcm.configs.Database;
import edu.ptithcm.model.EmployeeModel;


public class EmployeeRepo {

    public static void createUser(EmployeeModel employee){
        
    }

    public static boolean checkEmployeeExists(String username){
        String query = "SELECT COUNT(*) AS count FROM employee WHERE username = ?";
        try(Connection connection = Database.getInstance().getConnection()){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            try ( ResultSet rs = ps.executeQuery() ){
                if(rs.next()){
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static EmployeeModel findByUsername(String username){
        String query ="SELECT b.name as branch, e.username, e.password, e.name, e.phone, e.role, e.start_at, e.status " +
                        "FROM employee AS e " +               // thêm space sau e
                        "JOIN branch AS b " +                 // thêm space sau b
                        "ON e.branch_id = b.branch_id " +     // thêm space sau branch_id
                        "WHERE e.username = ? LIMIT 1";       // thêm prefix e. cho rõ
        try(Connection connection = Database.getInstance().getConnection()){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            try ( ResultSet rs = ps.executeQuery() ){
                if(rs.next()){
                    return new EmployeeModel.Builder()
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .role(rs.getString("role"))
                        .hireDate(rs.getString("hire_date"))
                        .status(rs.getBoolean("status"))
                        .build();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void createEmployee(List<EmployeeModel> employees){
        String query = "INSERT INTO employee (employee_id,username, password, name, phone,role,branch_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?,?)";
        try (
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(query)
            ){
                for(EmployeeModel emp : employees){
                    ps.setString(1, emp.getId());
                    ps.setString(2, emp.getUsername());
                    ps.setString(3, emp.getPasswordHash());
                    ps.setString(4, emp.getName());
                    ps.setString(5, emp.getPhone());
                    ps.setString(6, emp.getRole());
                    ps.setInt(7, emp.getBranchId());
                    ps.addBatch();
            }
            ps.executeBatch();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
