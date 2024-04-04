package com.proyect.sistemaventas.model;

import java.sql.Date;

public class Sale {

    private int idSales;
    private int idCustomer;
    private int idSeller; // representa al user, quien ingresa a la aplicación
    private int totalSale;
    private Date date;

    public Sale() {
    }

    public Sale(int idSales, int idCustomer, int idSeller, int totalSale, Date date) {
        this.idSales = idSales;
        this.idCustomer = idCustomer;
        this.idSeller = idSeller;
        this.totalSale = totalSale;
        this.date = date;
    }

    public int getIdSales() {
        return idSales;
    }

    public void setIdSales(int idSales) {
        this.idSales = idSales;
    }

    public int getCustomer() {
        return idCustomer;
    }

    public void setCustomer(int customer) {
        this.idCustomer = customer;
    }

    public int getSeller() {
        return idSeller;
    }

    public void setSeller(int seller) {
        this.idSeller = seller;
    }

    public int getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(int totalSale) {
        this.totalSale = totalSale;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
