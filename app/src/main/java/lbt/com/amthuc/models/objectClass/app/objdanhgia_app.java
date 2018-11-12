package lbt.com.amthuc.models.objectClass.app;

import lbt.com.amthuc.models.objectClass.firebase.objdanhgia;

public class objdanhgia_app {
    String iduser;
    objdanhgia chitietdanhgia;

    public objdanhgia_app(String iduser, objdanhgia chitietdanhgia) {
        this.iduser = iduser;
        this.chitietdanhgia = chitietdanhgia;
    }

    public objdanhgia_app() {
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public objdanhgia getChitietdanhgia() {
        return chitietdanhgia;
    }

    public void setChitietdanhgia(objdanhgia chitietdanhgia) {
        this.chitietdanhgia = chitietdanhgia;
    }
}
