package lbt.com.amthuc.models.objectClass.firebase;

public class objdanhgia {

    long sosao;
    String binhluan;
    String tennguoidanhgia;
    long ngaydanhgia;

    public objdanhgia(long sosao, String binhluan, String tennguoidanhgia, long ngaydanhgia) {
        this.sosao = sosao;
        this.binhluan = binhluan;
        this.tennguoidanhgia = tennguoidanhgia;
        this.ngaydanhgia = ngaydanhgia;
    }

    public String getTennguoidanhgia() {
        return tennguoidanhgia;
    }

    public void setTennguoidanhgia(String tennguoidanhgia) {
        this.tennguoidanhgia = tennguoidanhgia;
    }

    public long getNgaydanhgia() {
        return ngaydanhgia;
    }

    public void setNgaydanhgia(long ngaydanhgia) {
        this.ngaydanhgia = ngaydanhgia;
    }

    public objdanhgia() {
    }

    public long getSosao() {
        return sosao;
    }

    public void setSosao(long sosao) {
        this.sosao = sosao;
    }

    public String getBinhluan() {
        return binhluan;
    }

    public void setBinhluan(String binhluan) {
        this.binhluan = binhluan;
    }
}
