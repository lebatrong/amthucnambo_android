package lbt.com.amthuc.Presenters.Account.ThanhPhan;

import android.widget.EditText;

import java.util.ArrayList;

import lbt.com.amthuc.Views.TaiKhoan.thanhphan.iViewThanhPhan;
import lbt.com.amthuc.models.modelThanhPhan;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;

public class logicThanhPhan implements impThanhPhan {


    iViewThanhPhan mView;
    modelThanhPhan mModel;

    public logicThanhPhan(iViewThanhPhan mView) {
        this.mView = mView;
        mModel = new modelThanhPhan(mView);
    }

    @Override
    public void themthanhphan(ArrayList<String> listTenThanhPhan , boolean isThucAn) {
        mModel.themthanhphan(listTenThanhPhan,isThucAn);
    }

    @Override
    public void danhsachthanhphan(boolean isThucAn) {
        mModel.danhsachthanhphan(isThucAn);
    }

    @Override
    public void capnhatthanhphan(objthanhphan thanhphan, String TenTP, int position, boolean isThucAn) {
        if(!TenTP.isEmpty()){
            thanhphan.setTenthanhphan(TenTP);
            mModel.capnhatthanhphan(thanhphan,position,isThucAn);
        }else {
            mView.loitenthanhphan();
        }

    }
}
