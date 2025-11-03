package edu.ptithcm.configs.databases;

import java.sql.Connection;

public interface DatabaseAdapter {
    void init();
    Connection getConnection();
    void close();
}
