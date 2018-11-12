package lbt.com.amthuc.models.objectClass.firebase;

public class objtinhthanh {
    String tentinh;
    double lat,lng;

    public objtinhthanh(String tentinh, double lat, double lng) {
        this.tentinh = tentinh;
        this.lat = lat;
        this.lng = lng;
    }

    public objtinhthanh() {
    }

    public String getTentinh() {
        return tentinh;
    }

    public void setTentinh(String tentinh) {
        this.tentinh = tentinh;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
