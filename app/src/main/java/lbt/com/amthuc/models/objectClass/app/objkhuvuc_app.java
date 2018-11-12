package lbt.com.amthuc.models.objectClass.app;

import lbt.com.amthuc.models.objectClass.firebase.objtinhthanh;

public class objkhuvuc_app {

    String makhuvuc;
    objtinhthanh chitietkhuvuc;

    public objkhuvuc_app(String makhuvuc, objtinhthanh chitietkhuvuc) {
        this.makhuvuc = makhuvuc;
        this.chitietkhuvuc = chitietkhuvuc;
    }

    public objkhuvuc_app() {
    }

    public String getMakhuvuc() {
        return makhuvuc;
    }

    public void setMakhuvuc(String makhuvuc) {
        this.makhuvuc = makhuvuc;
    }

    public objtinhthanh getChitietkhuvuc() {
        return chitietkhuvuc;
    }

    public void setChitietkhuvuc(objtinhthanh chitietkhuvuc) {
        this.chitietkhuvuc = chitietkhuvuc;
    }
}
