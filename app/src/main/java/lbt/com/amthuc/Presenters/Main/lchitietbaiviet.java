package lbt.com.amthuc.Presenters.Main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import lbt.com.amthuc.Presenters.FplashScreens.igetdataapp;
import lbt.com.amthuc.Presenters.FplashScreens.lgetdataapp;
import lbt.com.amthuc.Presenters.LoginRegister.ilogin;
import lbt.com.amthuc.Presenters.LoginRegister.llogin;
import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objdanhgia_app;
import lbt.com.amthuc.models.objectClass.app.objgoiy_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;
import lbt.com.amthuc.models.objectClass.firebase.objdanhgia;
import lbt.com.amthuc.models.objectClass.firebase.objluubaiviet;

public class lchitietbaiviet implements igetdataapp, ilogin {
    public static String TAG = "lchitietbaiviet";

    ichitietbaiviet minterface;
    Context context;
    FirebaseDatabase mDatabase;
    lgetdataapp mgetdata;
    llogin mLogin;

    ArrayList<objbaiviet_app> allbaiviet;

    public lchitietbaiviet(ichitietbaiviet minterface,Context context) {
        this.minterface = minterface;
        mDatabase = FirebaseDatabase.getInstance();
        mgetdata = new lgetdataapp(context,this);
        this.context = context;
        mLogin = new llogin(this,context);
    }

    public void getdanhgia(String idbaiviet, final boolean isThongke){
        DatabaseReference mRef = mDatabase.getReference().child("danhgias").child(idbaiviet);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    ArrayList<objdanhgia_app> mlist = new ArrayList<>();

                    for (DataSnapshot i :dataSnapshot.getChildren()) {
                        objdanhgia_app danhgia = new objdanhgia_app();
                        objdanhgia obj = i.getValue(objdanhgia.class);
                        danhgia.setChitietdanhgia(obj);
                        danhgia.setIduser(i.getKey());
                        mlist.add(danhgia);
                    }

                    if(isThongke) {
                        thongkedanhgia(mlist);
                    }else{
                        minterface.danhsachbinhluan(mlist);
                    }

                }else{
                    if(!isThongke) {
                        minterface.chuacodanhgia();
                    }
                    Log.e(TAG,"Values danhgias null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
                minterface.loitaidulieu_chitietbaiviet();
            }
        });
    }

    public void danhgiabaiviet(String noidung, int sosao, String idbaiviet){
        Calendar calendar = Calendar.getInstance();
        objdanhgia mdanhgia = new objdanhgia();
        mdanhgia.setBinhluan(noidung);
        mdanhgia.setTennguoidanhgia(mLogin.getDataNguoiDung().getNguoidung().getHoten());
        mdanhgia.setNgaydanhgia(calendar.getTimeInMillis());
        mdanhgia.setSosao(sosao);

        DatabaseReference mRef = mDatabase.getReference()
                .child("danhgias")
                .child(idbaiviet)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mRef.setValue(mdanhgia).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                minterface.results_danhgiabaiviet(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                minterface.results_danhgiabaiviet(true);
                Log.e(TAG,e.toString());
            }
        });
    }

    public boolean checklogicdanhgia(String danhgia){
        if(danhgia.isEmpty()){
            Toast.makeText(context, context.getText(R.string.danhgiakhongduoctrong), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            String []listdanhgia = danhgia.split(" ");
            if(listdanhgia.length>100){
                Toast.makeText(context, context.getText(R.string.danhgiakhongqua), Toast.LENGTH_SHORT).show();
                return false;
            }else{
                if(danhgia.replace(" ","").length()<2){
                    Toast.makeText(context, context.getText(R.string.danhgiakhonghople), Toast.LENGTH_SHORT).show();
                    return  false;
                }else
                    return true;
            }
        }

    }

    public objdanhgia_app getdanhgiacuatoi(ArrayList<objdanhgia_app> list){
        String iduser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for (objdanhgia_app i : list) {
            if(iduser.matches(i.getIduser())) {
                return i;
            }
        }
        return null;
    }

    private void thongkedanhgia(ArrayList<objdanhgia_app> list){
        float tongsosao = list.size()*5;
        float sosaodanhgia = 0;
        String strSoSao = context.getText(R.string.rate1).toString();
        for (objdanhgia_app i: list) {
            sosaodanhgia+= i.getChitietdanhgia().getSosao();
        }


        switch (Math.round((sosaodanhgia/tongsosao)*5)){
            case 1:
                strSoSao = context.getText(R.string.rate1).toString();
                break;
            case 2:
                strSoSao = context.getText(R.string.rate2).toString();
                break;
            case 3:
                strSoSao = context.getText(R.string.rate3).toString();
                break;
            case 4:
                strSoSao = context.getText(R.string.rate4).toString();
                break;
            case 5:
                strSoSao = context.getText(R.string.rate5).toString();
                break;
            default:
                strSoSao = context.getText(R.string.rate5).toString();
                break;

        }

        minterface.thongkedanhgia(tongsosao,sosaodanhgia,strSoSao);
    }

    public void getgoiy(final String maloai, final objbaiviet_app mbaiviet){
        DatabaseReference mRef = mDatabase.getReference().child("goiys");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    List<objgoiy_app> mlist = new ArrayList<>();
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        objgoiy_app obj = new objgoiy_app();
                        obj.setMathanhphan(i.getKey());

                        GenericTypeIndicator<List<String>> gen = new GenericTypeIndicator<List<String>>() {
                        };
                        obj.setListgoiy(i.getValue(gen));
                        mlist.add(obj);
                    }

                    getMonAnGoiY(maloai, mlist, mbaiviet, mgetdata.getmykhuvuc());
                }else {
                    Log.e(TAG,"GỢI Ý NULL");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
                minterface.loitaidulieu_chitietbaiviet();
            }
        });
    }

    private void getMonAnGoiY(final String loai, final List<objgoiy_app> alllistgoiy, final objbaiviet_app mbaiviet, objkhuvuc_app mkhuvuc){

        DatabaseReference mref = mDatabase.getReference().child("dacsans").child("KVMN").child(mkhuvuc.getMakhuvuc()).child(loai);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    allbaiviet = new ArrayList<>();

                    for (DataSnapshot i :dataSnapshot.getChildren()) {
                        for (DataSnapshot j : i.getChildren()) {
                            objbaiviet_app bv = new objbaiviet_app();
                            bv.setIdbaiviet(j.getKey());
                            bv.setChitiet(j.getValue(objchitietbaiviet.class));
                            allbaiviet.add(bv);
                        }
                    }

                    ArrayList<objgoiy_app> listgoiy_mbaiviet = new ArrayList<>();
                    for(String mtp : mbaiviet.getChitiet().getThanhphan()){
                        for (objgoiy_app allgy: alllistgoiy) {
                            if(mtp.matches(allgy.getMathanhphan()))
                                listgoiy_mbaiviet.add(allgy);
                        }
                    }

                    //LẤY DANH SÁCH NHỮNG BÀI VIẾT CÓ THÀNH PHẦN TRÙNG VỚI THÀNH PHẦN GỢI Ý CỦA BÀI VIẾT
                    ArrayList<objbaiviet_app> baivietgoiy = new ArrayList<>();
                    for (objbaiviet_app all : allbaiviet) {
                        for(objgoiy_app mgoiy : listgoiy_mbaiviet){
                            for(String tp_all : all.getChitiet().getThanhphan()){
                                for (String tp_mbaiviet: mgoiy.getListgoiy()) {
                                    if(tp_all.matches(tp_mbaiviet))
                                        baivietgoiy.add(all);
                                }
                            }
                        }
                    }

                    //LỌC LẠI NHỮNG BÀI VIẾT CÓ ID TRÙNG NHAU
                    HashSet<objbaiviet_app> hashSet = new HashSet<>();
                    hashSet.addAll(baivietgoiy);
                    baivietgoiy.clear();
                    baivietgoiy.addAll(hashSet);

                    if(baivietgoiy.size()>0){
                        minterface.danhsachbaivietgoiy(baivietgoiy,true);
                    }else{
                        minterface.danhsachbaivietgoiy(baivietgoiy,false);
                    }

                }else{
                    //mInterface.loitaidulieu_AnGi();
                    Log.e(TAG,"values dat san null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
            }
        });
    }

    public void kiemtrabaivietdaluu(final String idbaiviet){
        String []decode = idbaiviet.split("_");
        String loai = decode[2];
        DatabaseReference mRef = mDatabase.getReference()
                .child("luulais")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(loai);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    ArrayList<objluubaiviet> mListLuu = new ArrayList<>();
                    for (DataSnapshot i :dataSnapshot.getChildren()) {
                        mListLuu.add(i.getValue(objluubaiviet.class));
                    }

                    //Kiểm tra bài viết đã lưu chưa
                    boolean isSave = false;
                    for (objluubaiviet i :mListLuu) {
                        if(i.getMabaiviet().matches(idbaiviet))
                        {
                            isSave = true;
                            break;
                        }
                    }

                    if(isSave){
                        minterface.baivietdaluu();
                    }else{
                        minterface.baivietchualuu();
                    }

                }else{
                    minterface.baivietchualuu();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
                minterface.baivietchualuu();
            }
        });
    }


    public void luubaivietluu(String idbaiviet){
        String []decode = idbaiviet.split("_");
        String loai = decode[2];
        objluubaiviet mluubaiviet = new objluubaiviet();
        mluubaiviet.setMabaiviet(idbaiviet);
        mluubaiviet.setMakhuvuc(decode[3]);


        DatabaseReference mRef = mDatabase.getReference()
                .child("luulais")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(loai);
        mRef.push().setValue(mluubaiviet).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                minterface.results_luubaiviet(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.toString());
                minterface.results_luubaiviet(false);
            }
        });
    }

    public void xoabaivietluu(final String idbaiviet){
        String []decode = idbaiviet.split("_");
        final String loai = decode[2];
        DatabaseReference mRef = mDatabase.getReference()
                .child("luulais")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(loai);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){

                    for (DataSnapshot i :dataSnapshot.getChildren()) {
                        objluubaiviet obj = i.getValue(objluubaiviet.class);
                        if(obj.getMabaiviet().matches(idbaiviet)){
                            DatabaseReference mr = mDatabase.getReference()
                                    .child("luulais")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(loai).child(i.getKey());
                            mr.setValue(null)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    minterface.results_xoabaiviet(true);
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    minterface.results_xoabaiviet(false);
                                }
                            });
                            break;
                        }
                    }
                }else
                    minterface.results_xoabaiviet(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
                minterface.results_xoabaiviet(false);
            }
        });
    }


    @Override
    public void loadkhuvuc(boolean isSuccess) {

    }

    @Override
    public void saveMyData(boolean isSuccess, objkhuvuc_app mdata) {

    }

    @Override
    public void loitaidulieu_getDataApp() {

    }

    @Override
    public void taidulieuthanhcong(boolean isRealtime) {

    }

    @Override
    public void ResultLogin(boolean isSuccess) {

    }

    @Override
    public void result_listenner_values_user(objnguoidung_app muser) {

    }



    @Override
    public void code(String code) {

    }
}
