package com.example.fooddelivery;

public class DataCart {
    private String idCart, idMenu, idCustomer, namaMenu, quantityMenu , hargaMenu;

    public DataCart() {
    }

    public DataCart(String hargaMenu, String idCart, String idCustomer, String idMenu, String namaMenu, String quantityMenu) {
        this.idCart = idCart;
        this.idMenu = idMenu;
        this.idCustomer = idCustomer;
        this.namaMenu = namaMenu;
        this.quantityMenu = quantityMenu;
        this.hargaMenu = hargaMenu;
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
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getQuantityMenu() {
        return quantityMenu;
    }

    public void setQuantityMenu(String quantityMenu) {
        this.quantityMenu = quantityMenu;
    }

    public String getHargaMenu() {
        return hargaMenu;
    }

    public void setHargaMenu(String hargaMenu) {
        this.hargaMenu = hargaMenu;
    }
}
