package lbt.com.amthuc.models.objectClass.app;

import java.io.Serializable;

import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;

public class objbaiviet_app implements Serializable {

    String idbaiviet;
    objchitietbaiviet chitiet;

    public objbaiviet_app(String idbaiviet, objchitietbaiviet chitiet) {
        this.idbaiviet = idbaiviet;
        this.chitiet = chitiet;
    }

    public objbaiviet_app() {
    }


    public String getIdbaiviet() {
        return idbaiviet;
    }

    public void setIdbaiviet(String idbaiviet) {
        this.idbaiviet = idbaiviet;
    }

    public objchitietbaiviet getChitiet() {
        return chitiet;
    }

    public void setChitiet(objchitietbaiviet chitiet) {
        this.chitiet = chitiet;
    }
}
