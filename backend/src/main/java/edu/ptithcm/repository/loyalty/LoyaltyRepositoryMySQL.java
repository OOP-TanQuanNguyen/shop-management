package edu.ptithcm.repository.loyalty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ptithcm.model.LoyaltyModel;
import edu.ptithcm.repository.BaseRepository;

public class LoyaltyRepositoryMySQL extends BaseRepository implements LoyaltyRepository {
    private static LoyaltyRepositoryMySQL instance;
    private LoyaltyRepositoryMySQL() {}
    public static synchronized LoyaltyRepositoryMySQL getInstance() {
        if (instance == null) instance = new LoyaltyRepositoryMySQL();
        return instance;
    }

    @Override
    public LoyaltyModel findByCustomer(String cid) throws SQLException {
        final String sql =
            "SELECT loyalty_id, customer_id, total_points " +
            "FROM loyalty WHERE customer_id = ? LIMIT 1";

        try (Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cid);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LoyaltyModel(
                            rs.getString("loyalty_id"),
                            rs.getString("customer_id"),
                            rs.getInt("total_points")
                    );
                }
            }
        }
        return null;
    }


    @Override
    public void updatePoints(String cid, int delta) throws SQLException {
        String sql = "UPDATE loyalty SET total_points = total_points + ? WHERE customer_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setString(2, cid);
            ps.executeUpdate();
        }
    }
}
