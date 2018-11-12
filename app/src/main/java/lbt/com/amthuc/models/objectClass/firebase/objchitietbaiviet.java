package lbt.com.amthuc.models.objectClass.firebase;

import java.io.Serializable;
import java.util.List;

public class objchitietbaiviet implements Serializable {
    String ten;
    String gioithieu;
    String ghichu;
    String diachi;
    long giathamkhao;
    List<String> hinh;
    List<String> thanhphan;

    public objchitietbaiviet(String ten, String gioithieu, String ghichu, String diachi, long giathamkhao, List<String> hinh, List<String> thanhphan) {
        this.ten = ten;
        this.gioithieu = gioithieu;
        this.ghichu = ghichu;
        this.diachi = diachi;
        this.giathamkhao = giathamkhao;
        this.hinh = hinh;
        this.thanhphan = thanhphan;
    }

    public objchitietbaiviet() {
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getGioithieu() {
        return gioithieu;
    }

    public void setGioithieu(String gioithieu) {
        this.gioithieu = gioithieu;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public long getGiathamkhao() {
        return giathamkhao;
    }

    public void setGiathamkhao(long giathamkhao) {
        this.giathamkhao = giathamkhao;
    }


    public List<String> getHinh() {
        return hinh;
    }

    public void setHinh(List<String> hinh) {
        this.hinh = hinh;
    }

    public List<String> getThanhphan() {
        return thanhphan;
    }

    public void setThanhphan(List<String> thanhphan) {
        this.thanhphan = thanhphan;
    }
}
