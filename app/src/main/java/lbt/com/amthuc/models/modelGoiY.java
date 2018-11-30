package lbt.com.amthuc.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lbt.com.amthuc.Views.TaiKhoan.ThemGoiY.iViewThemGoiY;
import lbt.com.amthuc.models.objectClass.app.objgoiy_app;
import lbt.com.amthuc.models.objectClass.app.objgoiychitiet_app;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;
import lbt.com.amthuc.utils.getDataApp;

public class modelGoiY {

    public String TAG = "modelGoiY";

    FirebaseDatabase mDatabase;
    iViewThemGoiY mView;

    getDataApp mGetData;

    public modelGoiY(iViewThemGoiY mView, Context context) {
        this.mView = mView;
        mDatabase = FirebaseDatabase.getInstance();
        mGetData = new getDataApp(context);
    }

    public void danhsachgoiy(){
        DatabaseReference mRef = mDatabase.getReference().child("goiys");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    ArrayList<objgoiy_app> mlist = new ArrayList<>();
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        GenericTypeIndicator<List<String>> gen = new GenericTypeIndicator<List<String>>() {};
                        mlist.add(new objgoiy_app(i.getKey(),i.getValue(gen)));
                    }

                    getListGoiYChiTiet(mlist);

                }else {
                    mView.resultgoiy(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.resultgoiy(null);
            }
        });
    }

    private void getListGoiYChiTiet(ArrayList<objgoiy_app> mlist) {
        ArrayList<objgoiychitiet_app> listGoiYChiTiet = new ArrayList<>();
        ArrayList<objthanhphan> mlistThanhPhan = mGetData.getListThanhPhan("thanhphanmonan");
        mlistThanhPhan.addAll(mGetData.getListThanhPhan("thanhphannuocuong"));
        for (objgoiy_app gy :mlist) {

            objgoiychitiet_app chitiet = new objgoiychitiet_app();
            chitiet.setMathanhphan(gy.getMathanhphan());
            //Lây tên của gợi ý
            for (objthanhphan tp : mlistThanhPhan) {
                if(tp.getMathanhphan().matches(gy.getMathanhphan())){
                    chitiet.setTenthanhphan(tp.getTenthanhphan());
                    break;
                }
            }

            ArrayList<objthanhphan> listGoiYChild = new ArrayList<>();
            //Lấy tên cho các thành phần con của gợi ý List<String>
            for(String i : gy.getListgoiy()){
                for (objthanhphan tp : mlistThanhPhan) {
                    if(tp.getMathanhphan().matches(i)){
                        listGoiYChild.add(tp);
                        break;
                    }
                }
            }
            chitiet.setListgoiy(listGoiYChild);

            listGoiYChiTiet.add(chitiet);
        }

        mView.resultgoiy(listGoiYChiTiet);

    }


    public void capnhatgoiy(objgoiy_app goiy) {
        DatabaseReference mRef = mDatabase.getReference()
                .child("goiys")
                .child(goiy.getMathanhphan());
        mRef.setValue(goiy.getListgoiy())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,e.toString());
                        mView.resultCapNhatGoiY(false);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mView.resultCapNhatGoiY(true);
                    }
                });
    }


}
