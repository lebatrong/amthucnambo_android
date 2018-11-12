package lbt.com.amthuc.Views.TaiKhoan.admin;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;

public interface iViewAdmin {

    void result_baivietcuatoi(ArrayList<objbaiviet_app> list);
    void result_delete(boolean isSuccess);
    void listbaivietdafilter(ArrayList<objbaiviet_app> listBaiViet);
}
