package com.gmail.yuriypelykh;

import java.sql.Connection;

public class FlatDAOExtension extends AbstractDAO<Integer, Flat> {
    public FlatDAOExtension(Connection conn, String table) {
        super(conn, table);
    }
}
