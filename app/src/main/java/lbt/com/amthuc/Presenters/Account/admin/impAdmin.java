package lbt.com.amthuc.Presenters.Account.admin;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;

public interface impAdmin {
    void getlistbaivietcuatoi();
    void deletebaiviet(String idbaiviet);
    void locbaiviet(boolean isFilterAll,
                    boolean isThucAn,
                    objkhuvuc_app makhuvuc,
                    ArrayList<objbaiviet_app> listBaiViet);
}
