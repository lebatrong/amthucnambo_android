package lbt.com.amthuc.Presenters.Main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objdanhgia_app;
import lbt.com.amthuc.models.objectClass.app.objthongkedanhgia_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;
import lbt.com.amthuc.models.objectClass.firebase.objdanhgia;

public class luonggi {

    public String TAG = "luonggi";

    iuonggi mInterface;
    FirebaseDatabase mDatabase;

    public luonggi(iuonggi mInterface) {
        this.mInterface = mInterface;
        this.mDatabase = FirebaseDatabase.getInstance();
    }



    public void getBaiVietDatSan(String makhuvuc){
        DatabaseReference mRef = mDatabase.getReference().child("dacsans").child("KVMN").child(makhuvuc).child("nuocuongs");
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
                    getPhobien( baiviet);
                }else{
                    mInterface.danhsachbaivietdacsan_uonggi(null);
                    Log.e(TAG,"values null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
                mInterface.loitaidulieu_UongGi();
            }
        });
    }

    private void getPhobien(final ArrayList<objbaiviet_app> baiviet){
        final DatabaseReference mRef = mDatabase.getReference().child("danhgias");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){

                    //Lấy danh sách đánh giá của bài viết
                    ArrayList<objthongkedanhgia_app> mthongke = new ArrayList<>();
                    for(DataSnapshot i: dataSnapshot.getChildren()){
                        for (objbaiviet_app bv : baiviet) {
                            if(i.getKey().matches(bv.getIdbaiviet())){
                                List<objdanhgia_app> listDanhgia = new ArrayList<>();
                                for (DataSnapshot dt : i.getChildren()) {
                                    objdanhgia_app danhgia = new objdanhgia_app();
                                    objdanhgia obj = dt.getValue(objdanhgia.class);
                                    danhgia.setChitietdanhgia(obj);
                                    danhgia.setIduser(dt.getKey());
                                    listDanhgia.add(danhgia);
                                }
                                mthongke.add(new objthongkedanhgia_app(bv.getIdbaiviet(),listDanhgia));
                            }
                        }
                    }

                    if(mthongke.size()!=0) {

                        //TÍNH ĐIỂM ĐÁNH GIÁ
                        for (objthongkedanhgia_app i : mthongke) {
                            float sosaodanhgia = 0;
                            for (objdanhgia_app j : i.getDanhgia()) {
                                sosaodanhgia += j.getChitietdanhgia().getSosao();
                            }
                            float tongsosao = i.getDanhgia().size() * 5;
                            i.setDiemdanhgia((sosaodanhgia / tongsosao) * 5);
                        }

                        //SẮP XẾP THỨ TỰ GIẢM DẦN THEO ĐIỂM ĐÁNH GIÁ
                        int count = mthongke.size();
                        for (int i = 0; i < count - 1; i++) {
                            for (int j = i + 1; j < count; j++) {
                                objthongkedanhgia_app t;
                                if (mthongke.get(i).getDiemdanhgia() < mthongke.get(j).getDiemdanhgia()) {
                                    t = mthongke.get(j);
                                    mthongke.set(j, mthongke.get(i));
                                    mthongke.set(i, t);
                                }
                            }
                        }

                        //GET BÀI VIẾT TỪ THỐNG KÊ RA
                        ArrayList<objbaiviet_app> mlist = new ArrayList<>();
                        int sl = 0;
                        for (objthongkedanhgia_app i : mthongke) {
                            if (sl <= 5) {
                                for (objbaiviet_app j : baiviet) {
                                    if (i.getIdbaiviet().matches(j.getIdbaiviet()))
                                        mlist.add(j);
                                }
                            } else {
                                break;
                            }
                            sl++;
                        }

                        //ADD THÊM NẾU LIST PHỔ BIẾN CHƯA ĐỦ 5
                        if(mlist.size()<5){
                            int contbaiviet = baiviet.size();
                            int soluongcanthem = 5 - mlist.size();
                            int flag = 0;
                            for(int i=0; i<contbaiviet; i++){
                                boolean isTonTai = false;
                                for(objbaiviet_app j: mlist){
                                    if(baiviet.get(i).getIdbaiviet().matches(j.getIdbaiviet())){
                                        isTonTai = true;
                                        break;
                                    }
                                }
                                if(flag<soluongcanthem){
                                    if(!isTonTai){
                                        mlist.add(baiviet.get(i));
                                        flag++;
                                    }
                                }else
                                    break;
                            }
                        }

                        mInterface.danhsachbaivietphobien_uonggi(mlist);
                    }else {
                        ArrayList<objbaiviet_app> mlist = new ArrayList<>();
                        int sl = 0;
                        for (objbaiviet_app j : baiviet) {
                            if(sl<5)
                                mlist.add(j);
                            else
                                break;
                            sl++;
                        }
                        mInterface.danhsachbaivietphobien_uonggi(mlist);
                    }

                }else{

                    ArrayList<objbaiviet_app> mlist = new ArrayList<>();
                    int sl = 0;
                    for (objbaiviet_app j : baiviet) {
                        if(sl<5)
                            mlist.add(j);
                        else
                            break;
                        sl++;
                    }
                    mInterface.danhsachbaivietphobien_uonggi(mlist);
                }

                mInterface.danhsachbaivietdacsan_uonggi(baiviet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
            }
        });
    }

}
