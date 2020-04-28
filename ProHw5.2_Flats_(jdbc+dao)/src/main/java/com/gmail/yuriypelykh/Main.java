package com.gmail.yuriypelykh;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static final String dbConnection = "jdbc:mysql://localhost:3306/flatsdb?serverTimezone=Europe/Kiev";
    static final String dbUser = "testuser";
    static final String dbPassword = "testpassword";

    public static void main(String[] args) throws SQLException, NoSuchFieldException {
        Scanner sc = new Scanner(System.in);

        ConnectionFactory factory = new ConnectionFactory(dbConnection, dbUser, dbPassword);

        Connection conn = factory.getConnection();
        try {
            FlatDAOExtension dao = new FlatDAOExtension(conn, "flats");
            dao.init();
            Flat flat1 = new Flat("Solomianskyi", "Shootova 9a", 35.3, 1, 55000);
            dao.add(flat1);
            Flat flat2 = new Flat("Golosiivskyi", "Vasylkivska 25", 120.5, 4, 255000);
            dao.add(flat2);

            while (true) {
                System.out.println("1: add apartment");
                System.out.println("2: show all apartments");
                System.out.println("3: show apartments where...");
                System.out.println("4: show count in DB");
                System.out.print("-> ");
                String s = sc.nextLine();
                switch (s) {
                    case "1":
                        case1(sc, dao);
                        break;
                    case "2":
                        List<Flat> flatList = dao.getAll(Flat.class);
                        for (Flat flt : flatList) {
                            System.out.println(flt);
                        }
                        break;
                    case "3":
                        case3(sc, dao);
                        break;
                    case "4":
                        System.out.println(dao.count());
                        break;
                    default:
                        return;
                }
            }
        } finally {
            sc.close();
            if (conn != null) conn.close();
        }
    }

    public static void case1 (Scanner sc, FlatDAOExtension dao){
        System.out.print("Enter district: ");
        String district = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter area: ");
        double area = sc.nextDouble();
        System.out.print("Enter number of rooms: ");
        int rooms = sc.nextInt();
        System.out.print("Enter price: ");
        double price = sc.nextDouble();
        sc.nextLine();
        dao.add(new Flat(district, address, area, rooms, price));
    }

    public static void case3(Scanner sc, FlatDAOExtension dao){
        System.out.print("Specify parameter (area|rooms|price): ");
        String parameter = sc.nextLine();
        System.out.print("Specify criteria (more|equals|less): ");
        String c = sc.nextLine();
        String criteria = "";
        switch (c) {
            case "more":
                criteria = ">=";
                break;
            case "equals":
                criteria = "=";
                break;
            case "less":
                criteria = "<=";
                break;
            default:
                return;
        }
        System.out.print("Enter value: ");
        int value = sc.nextInt();
        sc.nextLine();
        List<Flat> listWhere = dao.getWhere(Flat.class, parameter, criteria, value);
        for (Flat flt : listWhere)
            System.out.println(flt);
    }

}
