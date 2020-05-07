package com.gmail.yuriypelykh;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static final String dbConnection = "jdbc:mysql://localhost:3306/hw5.3_ordersdb?serverTimezone=Europe/Kiev";
    static final String dbUser = "testuser";
    static final String dbPassword = "testpassword";

    public static void main(String[] args) throws SQLException, NoSuchFieldException {
        Scanner sc = new Scanner(System.in);

        ConnectionFactory factory = new ConnectionFactory(dbConnection, dbUser, dbPassword);

        Connection conn = factory.getConnection();
        try {
            ClientDAOExtension clientDao = new ClientDAOExtension(conn, "clients");
            ProductDAOExtension productDao = new ProductDAOExtension(conn, "products");
            OrderDAOExtension orderDao = new OrderDAOExtension(conn, "orders");

            Client client1 = new Client("Yuriy Pelykh", "0971530480", "Bila Tserkva NP#12");
            Client client2 = new Client("Ivan Ivanenko", "0732003305", "Kyiv NP#135");
            clientDao.add(client1);
            clientDao.add(client2);

            Product product1 = new Product("Xiaomi Redmi 8", "000-0001", "Mobile phone", 2999.99);
            Product product2 = new Product("Asus Archer 500", "000-0035", "Wi-Fi Router", 799.00);
            productDao.add(product1);
            productDao.add(product2);

            Order order1 = new Order(client1.getId(), product2.getId(), 2);
            orderDao.add(order1);
            Order order2 = new Order(client2.getId(), product1.getId(), 1);
            orderDao.add(order2);

            List<Order> list = orderDao.getAll(Order.class);
            for (Order odr : list)
                System.out.println(odr);

        } finally {
            sc.close();
            if (conn != null) conn.close();
        }
    }

}
