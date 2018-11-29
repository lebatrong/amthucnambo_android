package lbt.com.amthuc.Presenters.Account.GoiY;

import android.content.Context;

import lbt.com.amthuc.Views.TaiKhoan.ThemGoiY.iViewThemGoiY;
import lbt.com.amthuc.models.modelGoiY;

public class LogicGoiY implements impGoiY {

    iViewThemGoiY mView;
    modelGoiY mModel;

    public LogicGoiY(iViewThemGoiY mView, Context context) {
        this.mView = mView;
        mModel = new modelGoiY(mView, context);
    }

    @Override
    public void danhsachgoiy() {
        mModel.danhsachgoiy();
    }
}
