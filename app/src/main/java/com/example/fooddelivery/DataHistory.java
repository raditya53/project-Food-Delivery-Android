package com.example.fooddelivery;

public class DataHistory {
    private String idHistory,idCart,idMenu, idCustomer, NamaMenu, QuantityMenu ,HargaMenu, TotalHarga;

    public DataHistory() {
    }

    public DataHistory(String idHistory, String idCart, String idMenu, String idCustomer, String namaMenu, String quantityMenu, String hargaMenu, String totalHarga) {
        this.idHistory = idHistory;
        this.idCart = idCart;
        this.idMenu = idMenu;
        this.idCustomer = idCustomer;
        NamaMenu = namaMenu;
        QuantityMenu = quantityMenu;
        HargaMenu = hargaMenu;
        TotalHarga = totalHarga;
    }

    public String getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(String idHistory) {
        this.idHistory = idHistory;
    }

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNamaMenu() {
        return NamaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        NamaMenu = namaMenu;
    }

    public String getQuantityMenu() {
        return QuantityMenu;
    }

    public void setQuantityMenu(String quantityMenu) {
        QuantityMenu = quantityMenu;
    }

    public String getHargaMenu() {
        return HargaMenu;
    }

    public void setHargaMenu(String hargaMenu) {
        HargaMenu = hargaMenu;
    }

    public String getTotalHarga() {
        return TotalHarga;
    }

    public void setTotalHarga(String totalHarga) {
        TotalHarga = totalHarga;
    }
}
