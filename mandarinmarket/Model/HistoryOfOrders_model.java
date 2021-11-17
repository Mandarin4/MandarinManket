package com.example.mandarinmarket.Model;

public class HistoryOfOrders_model {
    Integer Ordernumber;
    Integer TotalPrice;
    String Date;

    public String getDate() {
        return Date;
    }

    public Integer getOrdernumber() {
        return Ordernumber;
    }

    public Integer getTotalPrice() {
        return TotalPrice;
    }

}
