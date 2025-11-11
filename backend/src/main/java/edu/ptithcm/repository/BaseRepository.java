package edu.ptithcm.repository;

import java.sql.Connection;
import java.sql.SQLException;

import edu.ptithcm.configs.databases.Database;

public abstract class BaseRepository {
    protected Connection getConnection() throws SQLException {
        return Database.getInstance().getConnection();
    }

    protected void safeRollback(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); }
            catch (SQLException e) { System.err.println("[DB] Rollback failed: " + e.getMessage()); }
        }
    }

    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try { conn.close(); }
            catch (SQLException e) { System.err.println("[DB] Close failed: " + e.getMessage()); }
        }
    }
}
