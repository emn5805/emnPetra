package com.example.lkuygulama;

public class Urun {
    private String baslik;
    private String fiyat;
    private String kullanici;

    public Urun(String baslik, String fiyat) {
        this.baslik = baslik;
        this.fiyat = fiyat;
    }



    public Urun() {
        this.baslik = this.baslik;
        this.fiyat = this.fiyat;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public String getKullanici() {
        return kullanici;
    }

    public void setKullanici(String kullanici) {
        this.kullanici = kullanici;
    }

}
