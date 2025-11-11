package edu.ptithcm.repository.shift;

import java.sql.*;
import java.util.*;
import edu.ptithcm.model.ShiftModel;
import edu.ptithcm.repository.BaseRepository;

public class ShiftRepositoryMySQL extends BaseRepository implements ShiftRepository {

    private static ShiftRepositoryMySQL instance;
    private ShiftRepositoryMySQL() {}
    public static synchronized ShiftRepositoryMySQL getInstance() {
        if (instance == null) instance = new ShiftRepositoryMySQL();
        return instance;
    }

    @Override
    public void create(ShiftModel s) throws SQLException {
        String sql = "INSERT INTO shift(name, start_time, end_time) VALUES (?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setTime(2, s.getStartTime());
            ps.setTime(3, s.getEndTime());
            ps.executeUpdate();
        }
    }

    @Override
    public List<ShiftModel> getAll() throws SQLException {
        List<ShiftModel> list = new ArrayList<>();
        String sql = "SELECT shift_id, name, start_time, end_time FROM shift ORDER BY shift_id ASC";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ShiftModel.Builder()
                        .id(rs.getInt("shift_id"))
                        .name(rs.getString("name"))
                        .start(rs.getTime("start_time"))
                        .end(rs.getTime("end_time"))
                        .build());
            }
        }
        return list;
    }

    @Override
    public ShiftModel findById(int id) throws SQLException {
        String sql = "SELECT shift_id, name, start_time, end_time FROM shift WHERE shift_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ShiftModel.Builder()
                            .id(rs.getInt("shift_id"))
                            .name(rs.getString("name"))
                            .start(rs.getTime("start_time"))
                            .end(rs.getTime("end_time"))
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public void update(int id, ShiftModel s) throws SQLException {
        String sql = "UPDATE shift SET name=?, start_time=?, end_time=? WHERE shift_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setTime(2, s.getStartTime());
            ps.setTime(3, s.getEndTime());
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void remove(int id) throws SQLException {
        String sql = "DELETE FROM shift WHERE shift_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
