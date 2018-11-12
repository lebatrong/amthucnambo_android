package lbt.com.amthuc.Presenters.Main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;

public class lfilter {
    public final String TAG = "lfilter";

    ifilter minterface;
    FirebaseDatabase mDatabase;

    public lfilter(ifilter minterface) {
        this.minterface = minterface;
        mDatabase = FirebaseDatabase.getInstance();
    }


    public void timkiem(boolean isMonAn, String makhuvuc, final String tenmonan, final String mathanhphan, final long giatientoida){
        String root = "monans";
        if(isMonAn)
            root = "monans";
        else
            root = "nuocuongs";
        DatabaseReference mRef = mDatabase.getReference().child("dacsans").child("KVMN").child(makhuvuc).child(root);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null){
                    ArrayList<objbaiviet_app> baiviet = new ArrayList<>();
                    for (DataSnapshot i :dataSnapshot.getChildren()) {
                        for (DataSnapshot j : i.getChildren()) {
                            objbaiviet_app bv = new objbaiviet_app();
                            bv.setIdbaiviet(j.getKey());
                            bv.setChitiet(j.getValue(objchitietbaiviet.class));
                            baiviet.add(bv);
                        }
                    }

                    if(baiviet.size()>0){
                        //Lọc
                        loc(baiviet,tenmonan,mathanhphan,giatientoida);
                    }else{
                        minterface.ketquatimkiem(null,false);
                    }

                }else{
                    minterface.ketquatimkiem(null,false);
                    //mInterface.loitaidulieu_AnGi();
                    Log.e(TAG,"values datsan null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
                minterface.loitaidulieu_filter();
            }
        });
    }

    private void getChung(String root, final String tenmonan, final String mathanhphan, final long giatientoida, final DataSnapshot dataSnapshotDatSan){
        DatabaseReference mref = mDatabase.getReference().child("chungs").child(root);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                if(dataSnapshot.getValue()!=null){

                }else{
                    //mInterface.loitaidulieu_AnGi();
                    Log.e(TAG,"values chung null");
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
                minterface.loitaidulieu_filter();
            }
        });


    }

    private void loc(ArrayList<objbaiviet_app> baiviet, String tenmonan, String mathanhphan, long giatientoida){
        ArrayList<objbaiviet_app> mBaiViet = new ArrayList<>();
        //TÊN MÓN ĂN TRỐNG THÌ TÌM TẤT CẢ
        if(tenmonan.isEmpty()){
            for (objbaiviet_app i : baiviet) {
                //KIỂM TRA GIÁ TIỀN. NẾU 0 LÀ TÌM HẾT
                if(giatientoida!=0){
                    if (i.getChitiet().getGiathamkhao() <= giatientoida) {
                        //0 Là tất cả
                        if(mathanhphan.matches("MA_0") || mathanhphan.matches("NU_0")){
                            mBaiViet.add(i);
                        }else{
                            for (String thanhphan : i.getChitiet().getThanhphan()) {
                                if (thanhphan.matches(mathanhphan)) {
                                    mBaiViet.add(i);
                                }
                            }
                        }
                    }
                }else {
                    //0 Là tất cả
                    if(mathanhphan.matches("MA_0") || mathanhphan.matches("NU_0")){
                        mBaiViet.add(i);
                    }else{
                        for (String thanhphan : i.getChitiet().getThanhphan()) {
                            if (thanhphan.matches(mathanhphan)) {
                                mBaiViet.add(i);
                            }
                        }
                    }
                }
            }
            if(mBaiViet.size()>0)
                minterface.ketquatimkiem(mBaiViet,true);
            else
                minterface.ketquatimkiem(mBaiViet,false);

        }else{
            for (objbaiviet_app i : baiviet) {
               
                if(i.getChitiet().getTen().toLowerCase().indexOf(tenmonan.toLowerCase())!=-1) {
                    //KIỂM TRA GIÁ TIỀN. NẾU 0 LÀ TÌM HẾT
                    if(giatientoida!=0){
                        if (i.getChitiet().getGiathamkhao() <= giatientoida) {
                            //0 Là tất cả
                            if(mathanhphan.matches("MA_0") || mathanhphan.matches("NU_0")){
                                mBaiViet.add(i);
                            }else{
                                for (String thanhphan : i.getChitiet().getThanhphan()) {
                                    if (thanhphan.matches(mathanhphan)) {
                                        mBaiViet.add(i);
                                    }
                                }
                            }
                        }
                    }else {
                        //0 Là tất cả
                        if(mathanhphan.matches("MA_0") || mathanhphan.matches("NU_0")){
                            mBaiViet.add(i);
                        }else{
                            for (String thanhphan : i.getChitiet().getThanhphan()) {
                                if (thanhphan.matches(mathanhphan)) {
                                    mBaiViet.add(i);
                                }
                            }
                        }
                    }
                }
            }

            if(mBaiViet.size()>0)
                minterface.ketquatimkiem(mBaiViet,true);
            else
                minterface.ketquatimkiem(mBaiViet,false);

        }

    }
}
