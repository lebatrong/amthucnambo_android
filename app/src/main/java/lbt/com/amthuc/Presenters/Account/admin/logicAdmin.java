package lbt.com.amthuc.Presenters.Account.admin;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import lbt.com.amthuc.Views.TaiKhoan.admin.iViewAdmin;
import lbt.com.amthuc.models.modelAdmin;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;

public class logicAdmin implements impAdmin {

    iViewAdmin mInterface;
    Context context;
    modelAdmin mModel;

    public logicAdmin(iViewAdmin mInterface, Context context) {
        this.mInterface = mInterface;
        this.context = context;
        mModel = new modelAdmin(mInterface,context);
    }

    @Override
    public void getlistbaivietcuatoi() {
        mModel.getBaiVietCuaToi();
    }

    @Override
    public void deletebaiviet(String idbaiviet) {
        mModel.deleteBaiViet(idbaiviet);
    }

    @Override
    public void locbaiviet(boolean isFilterAll, boolean isThucAn, objkhuvuc_app makhuvuc, ArrayList<objbaiviet_app> listBaiViet) {
        if(isFilterAll){
             mInterface.listbaivietdafilter(listBaiViet);
        }else {
            if(isThucAn){
                ArrayList<objbaiviet_app> mList = new ArrayList<>();
                //LỌC TẤT CẢ KHU VỰC
                if(makhuvuc.getMakhuvuc().matches("null")){
                    for(objbaiviet_app i : listBaiViet){
                        String[] decode = i.getIdbaiviet().split("_");
                        if(decode[2].matches("monans")){
                            mList.add(i);
                        }
                    }
                    mInterface.listbaivietdafilter(mList);
                }else {
                    //LỌC THEO KHU VỰC TÌM BÀI VIẾT LÀ MÓN ĂN
                    for(objbaiviet_app i : listBaiViet){
                        String[] decode = i.getIdbaiviet().split("_");
                        if(decode[2].matches("monans") && decode[3].matches(makhuvuc.getMakhuvuc())){
                            mList.add(i);
                        }
                    }
                    mInterface.listbaivietdafilter(mList);
                }
            }else {
                //LỌC NƯỚC UỐNG
                ArrayList<objbaiviet_app> mList = new ArrayList<>();
                //LỌC TẤT CẢ KHU VỰC
                if(makhuvuc.getMakhuvuc().matches("null")){
                    for(objbaiviet_app i : listBaiViet){
                        String[] decode = i.getIdbaiviet().split("_");
                        if(decode[2].matches("nuocuongs")){
                            mList.add(i);
                        }
                    }
                    mInterface.listbaivietdafilter(mList);
                }else {
                    //LỌC THEO KHU VỰC TÌM BÀI VIẾT LÀ MÓN ĂN
                    for(objbaiviet_app i : listBaiViet){
                        String[] decode = i.getIdbaiviet().split("_");
                        if(decode[2].matches("nuocuongs") && decode[3].matches(makhuvuc.getMakhuvuc())){
                            mList.add(i);
                        }
                    }
                    mInterface.listbaivietdafilter(mList);
                }
            }
        }
    }


}
