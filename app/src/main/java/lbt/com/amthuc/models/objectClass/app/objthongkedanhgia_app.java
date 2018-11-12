package lbt.com.amthuc.models.objectClass.app;

import java.util.List;

public class objthongkedanhgia_app {
    String idbaiviet;
    List<objdanhgia_app> danhgia;
    float diemdanhgia;

    public objthongkedanhgia_app(String idbaiviet, List<objdanhgia_app> danhgia) {
        this.idbaiviet = idbaiviet;
        this.danhgia = danhgia;
        diemdanhgia = 0;
    }

    public float getDiemdanhgia() {
        return diemdanhgia;
    }

    public void setDiemdanhgia(float diemdanhgia) {
        this.diemdanhgia = diemdanhgia;
    }

    public objthongkedanhgia_app() {
    }

    public String getIdbaiviet() {
        return idbaiviet;
    }

    public void setIdbaiviet(String idbaiviet) {
        this.idbaiviet = idbaiviet;
    }

    public List<objdanhgia_app> getDanhgia() {
        return danhgia;
    }

    public void setDanhgia(List<objdanhgia_app> danhgia) {
        this.danhgia = danhgia;
    }
}
