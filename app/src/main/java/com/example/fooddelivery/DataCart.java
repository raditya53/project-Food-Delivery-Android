package com.example.fooddelivery;

public class DataCart {
    private String idCart,idMenu, idCustomer, NamaMenu, QuantityMenu ,HargaMenu;

    public DataCart(String idCart, String idMenu, String idCustomer, String namaMenu, String quantityMenu, String hargaMenu) {
        this.idCart = idCart;
        this.idMenu = idMenu;
        this.idCustomer = idCustomer;
        this.NamaMenu = namaMenu;
        this.QuantityMenu = quantityMenu;
        this.HargaMenu = hargaMenu;
    }

    public DataCart() {
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
}
