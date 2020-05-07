package com.gmail.yuriypelykh;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Set;

public class Main {

    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {

        try {
            emf = Persistence.createEntityManagerFactory("JPABank");
            em = emf.createEntityManager();

            //Currency rates:
            new Currency("UAH", 1.0, 1.0);
            new Currency("USD", 26.75, 27.02);
            new Currency("EUR", 28.80, 29.24);

            Client client1 = new Client("Yurii Pelykh");
            Account account1 = new Account("UAH Account 1", "5168454512120011", "UAH");
            Account account2 = new Account("Dollar acc. 1", "5168454512120022", "USD");
            client1.addAccount(account1);
            client1.addAccount(account2);

            Client client2 = new Client("David Blaine");
            Account account3 = new Account("My account", "2600550550121033", "USD");
            client2.addAccount(account3);

            Client client3 = new Client("Bjorn Gelotte");
            Account account4 = new Account("My EUR Account", "2300550550121044", "EUR");
            client3.addAccount(account4);

            //Show client accounts:
            Set<Account> client1Accounts = client1.getAccounts();
            for (Account acc : client1Accounts) {
                System.out.println(acc.toString());
            }

            //Account replenishment in the given currency:
            account1.replenishBalance(2350.0, "UAH");
            account2.replenishBalance(120.0, "EUR");

            //Money transfers between clients (in sender account currency):
            account1.sendMoney(account3, 250.0);
            account2.sendMoney(account4, 100.0);
            account3.sendMoney(account1,1.0);

            //Transfer between same client accounts:
            account2.sendMoney(account1, 20.0);

            //Show overall client's balance in UAH:
            System.out.println("Total balance: " + client1.overallBalance());


        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

    }

}
