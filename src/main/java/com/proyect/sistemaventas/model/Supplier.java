package com.proyect.sistemaventas.model;

import java.sql.Date;

public class Supplier {

    private int idSupplier;
    private String rut;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String razonSocial;
    private Date date;

    public Supplier() {
    }

    public Supplier(int idSupplier, String rut, String name, String phoneNumber, String email, String address, String razonSocial, Date date) {
        this.idSupplier = idSupplier;
        this.rut = rut;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.razonSocial = razonSocial;
        this.date = date;
    }

    public int getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(int idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
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
