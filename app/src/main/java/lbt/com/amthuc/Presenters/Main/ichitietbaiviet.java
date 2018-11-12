package lbt.com.amthuc.Presenters.Main;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objdanhgia_app;

public interface ichitietbaiviet {
    void loitaidulieu_chitietbaiviet();
    void danhsachbinhluan(ArrayList<objdanhgia_app> list);
    void danhsachbaivietgoiy(ArrayList<objbaiviet_app> list, boolean cobaivietgoiy);
    void chuacodanhgia();
    void thongkedanhgia(float tongsao, float tongsaodanhgia, String rate);
    void baivietdaluu();
    void baivietchualuu();
    void results_luubaiviet(boolean isSuccess);
    void results_xoabaiviet(boolean isSuccess);
    void results_danhgiabaiviet(boolean isSuccess);

}
