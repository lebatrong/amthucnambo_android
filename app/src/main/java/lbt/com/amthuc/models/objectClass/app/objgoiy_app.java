package lbt.com.amthuc.models.objectClass.app;

import java.util.List;

public class objgoiy_app {
    String mathanhphan;
    List<String> listgoiy;

    public objgoiy_app(String mathanhphan, List<String> listgoiy) {
        this.mathanhphan = mathanhphan;
        this.listgoiy = listgoiy;
    }

    public objgoiy_app() {
    }

    public String getMathanhphan() {
        return mathanhphan;
    }

    public void setMathanhphan(String mathanhphan) {
        this.mathanhphan = mathanhphan;
    }

    public List<String> getListgoiy() {
        return listgoiy;
    }

    public void setListgoiy(List<String> listgoiy) {
        this.listgoiy = listgoiy;
    }
}
