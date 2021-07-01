package com.example.fooddelivery;

import java.util.HashMap;

public class DataHistory {
    private String key;

    private String Location, deliverStatus, idCustomer, idTransaksi, paymentStatus, tanggalPembelian, totalHarga;
    private HashMap<String, Object> idCart;

    public DataHistory() {
    }

    public DataHistory(String location, String deliverStatus,  HashMap<String, Object> idCart, String idCustomer,  String idTransaksi, String paymentStatus, String tanggalPembelian, String totalHarga) {
        this.Location = location;
        this.deliverStatus = deliverStatus;
        this.idCustomer = idCustomer;
        this.paymentStatus = paymentStatus;
        this.tanggalPembelian = tanggalPembelian;
        this.totalHarga = totalHarga;
        this.idTransaksi = idTransaksi;
        this.idCart = idCart;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTanggalPembelian() {
        return tanggalPembelian;
    }

    public void setTanggalPembelian(String tanggalPembelian) {
        this.tanggalPembelian = tanggalPembelian;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(String totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public HashMap<String, Object> getIdCart() {
        return idCart;
    }

    public void setIdCart(HashMap<String, Object> idCart) {
        this.idCart = idCart;
    }
}
