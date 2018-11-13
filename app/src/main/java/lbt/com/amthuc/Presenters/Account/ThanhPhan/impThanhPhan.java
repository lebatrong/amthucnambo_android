package lbt.com.amthuc.Presenters.Account.ThanhPhan;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;


public interface impThanhPhan {
    void themthanhphan(ArrayList<String> listTenThanhPhan, boolean isThucAn);
    void danhsachthanhphan(boolean isThucAn);
    void capnhatthanhphan(objthanhphan thanhphan, String tentp, int position, boolean isThucAn);
}
