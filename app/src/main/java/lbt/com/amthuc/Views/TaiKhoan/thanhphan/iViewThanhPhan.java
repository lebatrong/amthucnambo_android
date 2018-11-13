package lbt.com.amthuc.Views.TaiKhoan.thanhphan;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;

public interface iViewThanhPhan {
    void result_themthanhphan(boolean isSuccess);
    void result_danhsachthanhphan(ArrayList<objthanhphan> list);
    void loitenthanhphan();
    void CapNhatThanhPhanThanhCong();
}
