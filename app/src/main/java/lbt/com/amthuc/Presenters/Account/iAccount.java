package lbt.com.amthuc.Presenters.Account;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;

public interface iAccount {
    void taidulieuthatbai();
    void khongluumonan();
    void khongluunuocuong();
    void result_baivietdaluu(objbaiviet_app object);
    void result_capnhatthongtin(boolean isSuccess);
    void result_capnhatmatkhau(boolean isSuccess);
    void result_loc(ArrayList<objbaiviet_app> list);
    void result_baivietcuatoi(ArrayList<objbaiviet_app> list);
    void result_delete(boolean isSuccess);
}
