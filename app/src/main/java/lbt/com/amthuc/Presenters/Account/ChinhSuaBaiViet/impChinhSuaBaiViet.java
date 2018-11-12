package lbt.com.amthuc.Presenters.Account.ChinhSuaBaiViet;

import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;

public interface impChinhSuaBaiViet {
    void kiemtralogiceditbaiviet(EditText edtten,
                                 EditText edtgioithieu,
                                 EditText edtgia,
                                 EditText edtdiachi,
                                 Button btnthanhphan,
                                 ArrayList<FrameLayout> listFrmImv);

    void capnhatbaiviet(ArrayList<String> listImv,
                        objbaiviet_app baivietOld,
                        objchitietbaiviet ChiTiettUpdate);
}
