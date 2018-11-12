package lbt.com.amthuc.models.objectClass.firebase;

public class objnguoidungs {
    boolean quanly;
    String avatar;
    String email;
    Boolean gioitinh;
    String hoten;
    long ngaysinh;
    String quequan;
    String loaidangnhap;

    public objnguoidungs(boolean quanly, String avatar, String email, Boolean gioitinh, String hoten, long ngaysinh, String quequan, String loaidangnhap) {
        this.quanly = quanly;
        this.avatar = avatar;
        this.email = email;
        this.gioitinh = gioitinh;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.quequan = quequan;
        this.loaidangnhap = loaidangnhap;
    }

    public objnguoidungs() {
    }

    public String getLoaidangnhap() {
        return loaidangnhap;
    }

    public void setLoaidangnhap(String loaidangnhap) {
        this.loaidangnhap = loaidangnhap;
    }

    public boolean isQuanly() {
        return quanly;
    }

    public void setQuanly(boolean quanly) {
        this.quanly = quanly;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(Boolean gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public long getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(long ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getQuequan() {
        return quequan;
    }

    public void setQuequan(String quequan) {
        this.quequan = quequan;
    }
}
