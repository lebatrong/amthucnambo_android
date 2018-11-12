package lbt.com.amthuc.Presenters.Account.DangBaiViet;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.TaiKhoan.dangbaiviet.iViewDangBaiViet;
import lbt.com.amthuc.models.modelDangBaiViet;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;

public class logicDangBaiViet implements impDangBaiViet {

    modelDangBaiViet mModel;
    iViewDangBaiViet mInterface;
    Context context;

    public logicDangBaiViet(iViewDangBaiViet mInterface, Context context) {
        this.mModel = new modelDangBaiViet(context, mInterface);
        this.mInterface = mInterface;
        this.context = context;
    }

    @Override
    public void kiemtralogicdangbai(EditText edtten,
                                    EditText edtgioithieu,
                                    EditText edtgia,
                                    EditText edtdiachi,
                                    Button btnthanhphan,
                                    Button btnchonkhuvuc,
                                    ArrayList<FrameLayout> listImv) {
        String ten, gioithieu, gia, diachi, thanhphan, khuvuc;
        ten = edtten.getText().toString();
        gioithieu = edtgioithieu.getText().toString();
        gia = edtgia.getText().toString();
        diachi = edtdiachi.getText().toString();
        thanhphan = btnthanhphan.getText().toString();
        khuvuc = btnchonkhuvuc.getText().toString();
        if(!ten.isEmpty()
                && !gioithieu.isEmpty()
                && !gia.isEmpty()
                && !diachi.isEmpty()
                && !thanhphan.matches(context.getText(R.string.chonthanhphan).toString())
                && !khuvuc.matches(context.getText(R.string.chonkhuvuc).toString())){
            int count_imv = 0;
            for(FrameLayout frm : listImv){
                if(frm.getVisibility() == View.VISIBLE)
                    count_imv++;
            }
            if(count_imv!=0)
                mInterface.kiemtralogicthanhcong();
            else
                mInterface.loichonhinhanh();
        }else {
            if(ten.isEmpty())
                mInterface.loiten();

            if(gioithieu.isEmpty())
                mInterface.loigioithieu();

            if(gia.isEmpty())
                mInterface.loigiatien();

            if(diachi.isEmpty())
                mInterface.loidiachi();

            if(thanhphan.matches(context.getText(R.string.chonthanhphan).toString()))
                mInterface.loithanhphan();

            if(khuvuc.matches(context.getText(R.string.chonkhuvuc).toString()))
                mInterface.loichonkhuvuc();
        }
    }

    @Override
    public void dangbaiviet(ArrayList<ImageView> listImv, objchitietbaiviet baiviet, objkhuvuc_app khuvuc, boolean isThucAn) {
        mModel.dangbaiviet(listImv,baiviet,khuvuc,isThucAn);
    }
}
