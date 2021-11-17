package com.example.mandarinmarket.Model;

public class Productes_model {

   // String src;
    String name;
    String srcImage;
    Integer price;
    String sht;
    String nameP;
    String description;
    Integer IdProduct;
    Integer discount_price;

    public Integer getDiscount_price() {
        return discount_price;
    }

    public Integer getIdProduct() {
        return IdProduct;
    }

    public String getNameP() {
        return nameP;
    }


    public Integer getPrice() {
        return price;
    }

    public String getSht() {
        return sht;
    }

    public  String getName() {
        return name;
    }

    public String getSrcImage() {
        return srcImage;
    }

    public String getDescription() {
        return description;
    }
}
