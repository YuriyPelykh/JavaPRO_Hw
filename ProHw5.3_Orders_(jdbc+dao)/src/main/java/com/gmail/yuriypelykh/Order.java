package com.gmail.yuriypelykh;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    @Id
    private int id;

    private Object date;
    private int clientID;
    private int productID;
    private int quantity;

    public Order() {
    }

    public Order(int clientID, int productID, int quantity) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        this.date = dateFormat.format(d);
        this.clientID = clientID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", clientID=" + clientID +
                ", productID=" + productID +
                ", quantity=" + quantity +
                '}';
    }
}
