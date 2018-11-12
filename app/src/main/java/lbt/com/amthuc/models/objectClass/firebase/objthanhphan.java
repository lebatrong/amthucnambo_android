package lbt.com.amthuc.models.objectClass.firebase;

public class objthanhphan {
    String mathanhphan;
    String tenthanhphan;

    public objthanhphan(String mathanhphan, String tenthanhphan) {
        this.mathanhphan = mathanhphan;
        this.tenthanhphan = tenthanhphan;
    }

    public objthanhphan() {
    }

    public String getMathanhphan() {
        return mathanhphan;
    }

    public void setMathanhphan(String mathanhphan) {
        this.mathanhphan = mathanhphan;
    }

    public String getTenthanhphan() {
        return tenthanhphan;
    }

    public void setTenthanhphan(String tenthanhphan) {
        this.tenthanhphan = tenthanhphan;
    }
}
