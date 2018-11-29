package lbt.com.amthuc.models.objectClass.app;

import java.util.List;

import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;

public class objgoiychitiet_app {
    String tenthanhphan;
    String mathanhphan;
    List<objthanhphan> listgoiy;

    public objgoiychitiet_app(String tenthanhphan, String mathanhphan, List<objthanhphan> listgoiy) {
        this.tenthanhphan = tenthanhphan;
        this.mathanhphan = mathanhphan;
        this.listgoiy = listgoiy;
    }

    public objgoiychitiet_app() {
    }

    public String getTenthanhphan() {
        return tenthanhphan;
    }

    public void setTenthanhphan(String tenthanhphan) {
        this.tenthanhphan = tenthanhphan;
    }

    public String getMathanhphan() {
        return mathanhphan;
    }

    public void setMathanhphan(String mathanhphan) {
        this.mathanhphan = mathanhphan;
    }

    public List<objthanhphan> getListgoiy() {
        return listgoiy;
    }

    public void setListgoiy(List<objthanhphan> listgoiy) {
        this.listgoiy = listgoiy;
    }
}
