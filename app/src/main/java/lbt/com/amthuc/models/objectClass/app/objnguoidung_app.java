package lbt.com.amthuc.models.objectClass.app;

import lbt.com.amthuc.models.objectClass.firebase.objnguoidungs;

public class objnguoidung_app {
    objnguoidungs nguoidung;
    String anhdaidien;
    String password;


    public objnguoidung_app(objnguoidungs nguoidung, String anhdaidien, String password) {
        this.nguoidung = nguoidung;
        this.anhdaidien = anhdaidien;
        this.password = password;
    }

    public objnguoidung_app() {
    }

    public String getAnhdaidien() {
        return anhdaidien;
    }

    public void setAnhdaidien(String anhdaidien) {
        this.anhdaidien = anhdaidien;
    }

    public objnguoidungs getNguoidung() {
        return nguoidung;
    }

    public void setNguoidung(objnguoidungs nguoidung) {
        this.nguoidung = nguoidung;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
