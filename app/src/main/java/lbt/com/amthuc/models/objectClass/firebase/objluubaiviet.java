package lbt.com.amthuc.models.objectClass.firebase;

public class objluubaiviet {
    String mabaiviet;
    String makhuvuc;

    public objluubaiviet( String mabaiviet, String makhuvuc) {
        this.mabaiviet = mabaiviet;
        this.makhuvuc = makhuvuc;
    }

    public objluubaiviet() {
    }


    public String getMabaiviet() {
        return mabaiviet;
    }

    public void setMabaiviet(String mabaiviet) {
        this.mabaiviet = mabaiviet;
    }

    public String getMakhuvuc() {
        return makhuvuc;
    }

    public void setMakhuvuc(String makhuvuc) {
        this.makhuvuc = makhuvuc;
    }
}
