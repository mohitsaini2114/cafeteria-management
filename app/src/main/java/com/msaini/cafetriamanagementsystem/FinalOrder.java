package com.msaini.cafetriamanagementsystem;

import java.io.Serializable;

public class FinalOrder implements Serializable {

    public String userName;
    public String orderNumber;
    public boolean isCompleted;
    public String scheduleTime;
    public String totalAmount;
    public String orderKey;
    public String foodItems;
    public String foodQuant;

    public FinalOrder() {
    }

    public FinalOrder(String userName, String orderNumber, boolean isCompleted, String scheduleTime, String totalAmount) {
        this.userName = userName;
        this.orderNumber = orderNumber;
        this.isCompleted = isCompleted;
        this.scheduleTime = scheduleTime;
        this.totalAmount = totalAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
}
