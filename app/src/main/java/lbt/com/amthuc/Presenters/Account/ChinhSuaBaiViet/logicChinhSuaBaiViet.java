package lbt.com.amthuc.Presenters.Account.ChinhSuaBaiViet;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.TaiKhoan.chinhsuabaiviet.iViewChinhSuaBaiViet;
import lbt.com.amthuc.models.modelChinhSuaBaiViet;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;

public class logicChinhSuaBaiViet implements impChinhSuaBaiViet {

    iViewChinhSuaBaiViet mView;
    Context context;
    modelChinhSuaBaiViet mModel;

    public logicChinhSuaBaiViet(iViewChinhSuaBaiViet mView, Context context) {
        this.mView = mView;
        this.context = context;
        this.mModel = new modelChinhSuaBaiViet(context, mView);
    }

    @Override
    public void kiemtralogiceditbaiviet(EditText edtten,
                                        EditText edtgioithieu,
                                        EditText edtgia,
                                        EditText edtdiachi,
                                        Button btnthanhphan,
                                        ArrayList<FrameLayout> listFrmImv) {

        String ten, gioithieu, gia, diachi, thanhphan;
        ten = edtten.getText().toString();
        gioithieu = edtgioithieu.getText().toString();
        gia = edtgia.getText().toString();
        diachi = edtdiachi.getText().toString();
        thanhphan = btnthanhphan.getText().toString();
        if(!ten.isEmpty()
                && !gioithieu.isEmpty()
                && !gia.isEmpty()
                && !diachi.isEmpty()
                && !thanhphan.matches(context.getText(R.string.chonthanhphan).toString())){
            int count_imv = 0;
            for(FrameLayout frm : listFrmImv){
                if(frm.getVisibility() == View.VISIBLE)
                    count_imv++;
            }
            if(count_imv!=0)
                mView.kiemtralogicthanhcong();
            else
                mView.loichonhinhanh();
        }else {
            if(ten.isEmpty())
                mView.loiten();

            if(gioithieu.isEmpty())
                mView.loigioithieu();

            if(gia.isEmpty())
                mView.loigiatien();

            if(diachi.isEmpty())
                mView.loidiachi();

            if(thanhphan.matches(context.getText(R.string.chonthanhphan).toString()))
                mView.loithanhphan();
        }

    }

    @Override
    public void capnhatbaiviet(ArrayList<String> listImv,
                               objbaiviet_app baivietOld,
                               objchitietbaiviet ChiTiettUpdate) {
        mModel.chinhsuabaiviet(listImv,baivietOld,ChiTiettUpdate);
    }
}
