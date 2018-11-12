package lbt.com.amthuc.Presenters.Account.DangBaiViet;

import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;

public interface impDangBaiViet {
    void kiemtralogicdangbai(EditText edtten,
                             EditText edtgioithieu,
                             EditText edtgia,
                             EditText edtdiachi,
                             Button btnthanhphan,
                             Button btnchonkhuvuc,
                             ArrayList<FrameLayout> listFrmImv);

    void dangbaiviet(ArrayList<ImageView> listImv,
                     final objchitietbaiviet baiviet,
                     final objkhuvuc_app khuvuc,
                     boolean isThucAn);
}
