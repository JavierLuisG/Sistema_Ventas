package com.proyect.sistemaventas.model;

import java.sql.Date;

public class Product {

    private int idProduct;
    private String code;
    private String name;
    private int count;
    private int price;
    private String supplier;
    private Date date;

    public Product() {
    }

    public Product(int idProduct, String code, String name, int count, int price, String supplier, Date date) {
        this.idProduct = idProduct;
        this.code = code;
        this.name = name;
        this.count = count;
        this.price = price;
        this.supplier = supplier;
        this.date = date;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
