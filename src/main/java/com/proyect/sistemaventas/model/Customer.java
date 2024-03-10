package com.proyect.sistemaventas.model;

import java.sql.Date;

public class Customer {

    private int idCustomer;
    private String identification;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String razonSocial;
    private Date date;

    public Customer() {
    }

    public Customer(int idCustomer, String identification, String name, String phoneNumber, String email, String address, String razonSocial, Date date) {
        this.idCustomer = idCustomer;
        this.identification = identification;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.razonSocial = razonSocial;
        this.date = date;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
