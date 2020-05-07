package com.gmail.yuriypelykh;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String number;

    @Column(name = "currency_name")
    private String currencyName;

    private Double balance;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL)
    private Set<Transaction> transactions = new HashSet<>();

    //@OneToMany(mappedBy = "recipientAccount", cascade = CascadeType.ALL)
    //private List<Transaction> transactionsRec = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency accountCurrency;

    public Account() {
    }

    public Account(String name, String number, String currencyName) {
        this.name = name;
        this.number = number;
        this.balance = 0.0;
        this.currencyName = currencyName;
        setAccountCurrency(currencyName);
        Services.flushEntity(Main.em, this);
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public Currency getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String currencyName) {
        Currency cur = null;
        try {
            Query query = Main.em.createQuery("SELECT cur FROM Currency cur WHERE cur.currency = :currencyName", Currency.class);
            query.setParameter("currencyName", currencyName);
            cur = (Currency) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Currency not found!");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }
        this.accountCurrency = cur;
    }

    public void replenishBalance(double summ, String currency) {
        Main.em.getTransaction().begin();
        try {
            if (currency.equals(this.currencyName)) {
                this.balance += summ;
            } else {
                this.balance += summ * getCurrency(currency).getBuyRate() / this.getAccountCurrency().getSellRate();
            }
            Main.em.getTransaction().commit();
        } catch (Exception ex) {
            Main.em.getTransaction().rollback();
        }
    }

    public Currency getCurrency(String currencyName) {
        Currency cur = null;
        try {
            Query query = Main.em.createQuery("SELECT cur FROM Currency cur WHERE cur.currency = :currencyName", Currency.class);
            query.setParameter("currencyName", currencyName);
            cur = (Currency) query.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("Currency not found!");
            return null;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return null;
        }
        return cur;
    }

    public void sendMoney(Account recipientAccount, double summ) {
        if (this.balance < summ) {
            System.out.println("Not enough money to transfer!");
            return;
        } else {
            Main.em.getTransaction().begin();
            try {
                this.setBalance(balance - summ);
                if (recipientAccount.getAccountCurrency().equals(this.getAccountCurrency())) {
                    recipientAccount.setBalance(recipientAccount.getBalance() + summ);
                } else {
                    recipientAccount.setBalance(recipientAccount.getBalance() +
                            summ * this.getAccountCurrency().getBuyRate() / recipientAccount.getAccountCurrency().getSellRate());
                }
                Main.em.getTransaction().commit();
            } catch (Exception ex) {
                Main.em.getTransaction().rollback();
            }
            Transaction t = new Transaction(this, recipientAccount, summ);
            transactions.add(t);
            recipientAccount.getTransactions().add(t);
        }
    }


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", client=" + client +
                ", currencyName='" + currencyName + '\'' +
                ", balance=" + balance +
                '}';
    }
}
