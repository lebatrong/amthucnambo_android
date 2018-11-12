package lbt.com.amthuc.Presenters.Main;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;

public interface iangi {
    void loitaidulieu_AnGi();
    void danhsachbaivietchung_AnGi(ArrayList<objbaiviet_app> List);
    void danhsachbaivietdacsan_AnGi(ArrayList<objbaiviet_app> List);
    void danhsachbaivietphobien_AnGi(ArrayList<objbaiviet_app> List);
}
