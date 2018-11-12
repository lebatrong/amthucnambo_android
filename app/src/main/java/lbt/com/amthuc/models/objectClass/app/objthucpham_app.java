package lbt.com.amthuc.models.objectClass.app;

import java.util.List;

public class objthucpham_app {
    String idUser;
    List<objbaiviet_app> baiviet;

    public objthucpham_app(String idUser, List<objbaiviet_app> baiviet) {
        this.idUser = idUser;
        this.baiviet = baiviet;
    }

    public objthucpham_app() {
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public List<objbaiviet_app> getBaiviet() {
        return baiviet;
    }

    public void setBaiviet(List<objbaiviet_app> baiviet) {
        this.baiviet = baiviet;
    }
}
