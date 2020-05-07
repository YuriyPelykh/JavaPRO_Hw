package com.gmail.yuriypelykh;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String currency;

    @Column(name = "buy_rate")
    private Double buyRate;

    @Column(name = "sell_rate")
    private Double sellRate;

    @OneToMany(mappedBy = "accountCurrency", cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>();

    public Currency() {
    }

    public Currency(String currency, double buyRate, double sellRate) {
        this.currency = currency;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
        Services.flushEntity(Main.em,this);
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(Double buyRate) {
        this.buyRate = buyRate;
    }

    public Double getSellRate() {
        return sellRate;
    }

    public void setSellRate(Double sellRate) {
        this.sellRate = sellRate;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", buyRate=" + buyRate +
                ", sellRate=" + sellRate +
                '}';
    }
}
