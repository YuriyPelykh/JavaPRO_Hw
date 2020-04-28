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

//            Order flat1 = new Order("Solomianskyi", "Shootova 9a", 35.3, 1, 55000);
//            dao.add(flat1);
//            Order flat2 = new Order("Golosiivskyi", "Vasylkivska 25", 120.5, 4, 255000);
//            dao.add(flat2);
//
//            while (true) {
//                System.out.println("1: add apartment");
//                System.out.println("2: show all apartments");
//                System.out.println("3: show apartments where...");
//                System.out.println("4: show count in DB");
//                System.out.print("-> ");
//                String s = sc.nextLine();
//                switch (s) {
//                    case "1":
//                        case1(sc, dao);
//                        break;
//                    case "2":
//                        List<Order> flatList = dao.getAll(Order.class);
//                        for (Order flt : flatList) {
//                            System.out.println(flt);
//                        }
//                        break;
//                    case "3":
//                        case3(sc, dao);
//                        break;
//                    case "4":
//                        System.out.println(dao.count());
//                        break;
//                    default:
//                        return;
//                }
//            }
//        } finally {
//            sc.close();
//            if (conn != null) conn.close();
//        }
//    }
//
//    public static void case1 (Scanner sc, FlatDAOExtension dao){
//        System.out.print("Enter district: ");
//        String district = sc.nextLine();
//        System.out.print("Enter address: ");
//        String address = sc.nextLine();
//        System.out.print("Enter area: ");
//        double area = sc.nextDouble();
//        System.out.print("Enter number of rooms: ");
//        int rooms = sc.nextInt();
//        System.out.print("Enter price: ");
//        double price = sc.nextDouble();
//        sc.nextLine();
//        dao.add(new Order(district, address, area, rooms, price));
//    }
//
//    public static void case3(Scanner sc, FlatDAOExtension dao){
//        System.out.print("Specify parameter (area|rooms|price): ");
//        String parameter = sc.nextLine();
//        System.out.print("Specify criteria (more|equals|less): ");
//        String c = sc.nextLine();
//        String criteria = "";
//        switch (c) {
//            case "more":
//                criteria = ">=";
//                break;
//            case "equals":
//                criteria = "=";
//                break;
//            case "less":
//                criteria = "<=";
//                break;
//            default:
//                return;
//        }
//        System.out.print("Enter value: ");
//        int value = sc.nextInt();
//        sc.nextLine();
//        List<Order> listWhere = dao.getWhere(Order.class, parameter, criteria, value);
//        for (Order flt : listWhere)
//            System.out.println(flt);
//    }

}
