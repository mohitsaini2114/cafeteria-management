package com.msaini.cafetriamanagementsystem;


import java.io.Serializable;
import java.util.Comparator;

public class Order implements Comparator<Order>, Serializable {

    public String description;
    public String price;
    public String quantity;
    public String user;
    public String scheduleTime;


    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean done) {
        isDone = done;
    }

    public boolean isDone;
    public String orderID;
    public String imageUrl;





    public Order() {
    }

    public Order(String description, String price, String quantity, String scheduleTime, boolean isDone) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.isDone = isDone;
        this.scheduleTime = scheduleTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }





    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public int compare(Order o1, Order o2) {
        return 0;
    }
}
