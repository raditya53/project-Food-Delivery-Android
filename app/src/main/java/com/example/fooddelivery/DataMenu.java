package com.example.fooddelivery;

public class DataMenu {
    private String Id, Nama, Deskripsi, Harga, ImgUrl, Kategori;

    public DataMenu(String id, String nama, String deskripsi, String harga, String imgUrl, String kategori) {
        Id = id;
        Nama = nama;
        Deskripsi = deskripsi;
        Harga = harga;
        ImgUrl = imgUrl;
        Kategori = kategori;
    }

    public DataMenu() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        Deskripsi = deskripsi;
    }

    public String getHarga() {
        return Harga;
    }

    public void setHarga(String harga) {
        Harga = harga;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getKategori() {
        return Kategori;
    }

    public void setKategori(String kategori) {
        Kategori = kategori;
    }
}
