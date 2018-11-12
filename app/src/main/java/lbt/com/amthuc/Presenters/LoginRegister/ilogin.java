package lbt.com.amthuc.Presenters.LoginRegister;

import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;

public interface ilogin {
    void ResultLogin(boolean isSuccess);
    void result_listenner_values_user(objnguoidung_app muser);
    void result_dangnhap_sdt(boolean isSuccess);
    void code(String code);
}
