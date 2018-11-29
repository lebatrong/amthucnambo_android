package lbt.com.amthuc.Presenters.LoginRegister;

import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;

public interface ilogin {
    void ResultLogin(boolean isSuccess);
    void result_listenner_values_user(objnguoidung_app muser);
    void code(String code);
}
