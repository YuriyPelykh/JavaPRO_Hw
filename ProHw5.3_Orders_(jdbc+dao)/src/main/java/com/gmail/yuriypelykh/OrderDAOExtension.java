package com.gmail.yuriypelykh;

import java.sql.Connection;

public class OrderDAOExtension extends AbstractDAO<Integer, Order> {
    public OrderDAOExtension(Connection conn, String table) {
        super(conn, table);
    }
}
