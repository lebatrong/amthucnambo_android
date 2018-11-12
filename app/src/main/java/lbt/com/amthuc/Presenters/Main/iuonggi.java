package lbt.com.amthuc.Presenters.Main;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;

public interface iuonggi {
    void loitaidulieu_UongGi();
    void danhsachbaivietchung_uonggi(ArrayList<objbaiviet_app> list);
    void danhsachbaivietdacsan_uonggi(ArrayList<objbaiviet_app> list);
    void danhsachbaivietphobien_uonggi(ArrayList<objbaiviet_app> List);
}
