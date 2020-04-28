package com.gmail.yuriypelykh;

import java.sql.Connection;

public class ClientDAOExtension extends AbstractDAO<Integer, Client> {
    public ClientDAOExtension(Connection conn, String table) {
        super(conn, table);
    }
}
