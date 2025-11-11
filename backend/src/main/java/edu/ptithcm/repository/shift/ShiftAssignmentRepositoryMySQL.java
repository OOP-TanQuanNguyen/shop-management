package edu.ptithcm.repository.shift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ptithcm.model.ShiftAssignmentModel;
import edu.ptithcm.repository.BaseRepository;

public class ShiftAssignmentRepositoryMySQL extends BaseRepository implements ShiftAssignmentRepository {

    private static ShiftAssignmentRepositoryMySQL instance;
    private ShiftAssignmentRepositoryMySQL() {}
    public static synchronized ShiftAssignmentRepositoryMySQL getInstance() {
        if (instance == null) instance = new ShiftAssignmentRepositoryMySQL();
        return instance;
    }

    @Override
    public void assign(ShiftAssignmentModel sa) throws SQLException {
        String sql = "INSERT INTO shift_assignment(shift_id, employee_id, branch_id) VALUES (?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, sa.getShiftId());
            ps.setString(2, sa.getEmployeeId());
            ps.setInt(3, sa.getBranchId());
            ps.executeUpdate();
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
    public void remove(int shiftId, String employeeId, int branchId) throws SQLException {
        String sql = "DELETE FROM shift_assignment WHERE shift_id=? AND employee_id=? AND branch_id=?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, shiftId);
            ps.setString(2, employeeId);
            ps.setInt(3, branchId);
            ps.executeUpdate();
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
    public List<ShiftAssignmentModel> getAll() throws SQLException {
        List<ShiftAssignmentModel> list = new ArrayList<>();
        String sql = "SELECT shift_id, employee_id, branch_id FROM shift_assignment ORDER BY shift_id ASC";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShiftAssignmentModel.Builder()
                        .shift(rs.getInt("shift_id"))
                        .employee(rs.getString("employee_id"))
                        .branch(rs.getInt("branch_id"))
                        .build());
            }
        }
        return list;
    }

    @Override
    public List<ShiftAssignmentModel> findByEmployee(String employeeId) throws SQLException {
        List<ShiftAssignmentModel> list = new ArrayList<>();
        String sql = "SELECT shift_id, employee_id, branch_id FROM shift_assignment WHERE employee_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ShiftAssignmentModel.Builder()
                            .shift(rs.getInt("shift_id"))
                            .employee(rs.getString("employee_id"))
                            .branch(rs.getInt("branch_id"))
                            .build());
                }
            }
        }
        return list;
    }
}
