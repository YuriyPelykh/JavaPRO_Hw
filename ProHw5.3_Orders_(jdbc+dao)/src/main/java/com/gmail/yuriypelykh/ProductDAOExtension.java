package com.gmail.yuriypelykh;

import java.sql.Connection;

public class ProductDAOExtension extends AbstractDAO<Integer, Product> {
    public ProductDAOExtension(Connection conn, String table) {
        super(conn, table);
    }
}
