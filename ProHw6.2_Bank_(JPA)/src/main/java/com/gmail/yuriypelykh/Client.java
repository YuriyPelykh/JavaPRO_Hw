package com.gmail.yuriypelykh;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>();

    public Client() {
    }

    public Client(String name) {
        this.name = name;
        Services.flushEntity(Main.em, this);
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        if (!accounts.contains(account)) {
            accounts.add(account);
            account.setClient(this);
        }
    }

    public double overallBalance() {
        double obalance = 0.0;
        Main.em.refresh(this);
        for (Account acc : accounts) {
            if (acc.getAccountCurrency().getCurrency().equals("UAH")) {
                obalance += acc.getBalance();
            } else {
                obalance += acc.getBalance() * acc.getAccountCurrency().getBuyRate();
            }
        }
        return obalance;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

