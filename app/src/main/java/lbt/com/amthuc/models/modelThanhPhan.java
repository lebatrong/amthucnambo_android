package lbt.com.amthuc.models;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lbt.com.amthuc.Views.TaiKhoan.thanhphan.iViewThanhPhan;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;

public class modelThanhPhan {
    iViewThanhPhan mView;

    FirebaseDatabase mDatabase;

    public modelThanhPhan(iViewThanhPhan mView) {
        this.mView = mView;
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void themthanhphan(final ArrayList<String> listTenThanhPhan , boolean isThucAn) {
        String type = "";
        if(isThucAn)
            type = "monans";
        else
            type = "nuocuongs";

        //Lấy danh sách thành phần cũ rồi add thêm
        final DatabaseReference mRef = mDatabase.getReference().child("thanhphans").child(type);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    GenericTypeIndicator<ArrayList<objthanhphan>> gen = new GenericTypeIndicator<ArrayList<objthanhphan>>() {};
                    ArrayList<objthanhphan> mThanhPhan = dataSnapshot.getValue(gen);
                    String mathanhphanold = mThanhPhan.get(mThanhPhan.size()-1).getMathanhphan();
                    String[] decode = mathanhphanold.split("_");
                    //lấy ra giá trị tăng
                    String ma = decode[0];
                    int index = 0;
                    try {
                        index = Integer.parseInt(decode[1]);
                    }catch (Exception e){
                        mView.result_themthanhphan(false);
                    }
                    for(String ten : listTenThanhPhan){
                        mThanhPhan.add(new objthanhphan(ma+"_"+(++index),ten));
                    }

                    //Add lại giá trị
                    mRef.setValue(mThanhPhan)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mView.result_themthanhphan(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mView.result_themthanhphan(false);
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.result_themthanhphan(false);
            }
        });
    }

    public void danhsachthanhphan(boolean isThucAn) {
        String type = "";
        if(isThucAn)
            type = "monans";
        else
            type = "nuocuongs";

        final DatabaseReference mRef = mDatabase.getReference().child("thanhphans").child(type);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    GenericTypeIndicator<ArrayList<objthanhphan>> gen = new GenericTypeIndicator<ArrayList<objthanhphan>>() {};
                    ArrayList<objthanhphan> mThanhPhan = dataSnapshot.getValue(gen);
                    mView.result_danhsachthanhphan(mThanhPhan);
                }else
                    mView.result_danhsachthanhphan(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.result_danhsachthanhphan(null);
            }
        });
    }

    public void capnhatthanhphan(objthanhphan thanhphan, int position, boolean isThucAn) {
        String type = "";
        if(isThucAn)
            type = "monans";
        else
            type = "nuocuongs";

        final DatabaseReference mRef = mDatabase.getReference().child("thanhphans").child(type).child(String.valueOf(position));
        mRef.setValue(thanhphan)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.loitenthanhphan();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mView.CapNhatThanhPhanThanhCong();
                    }
                });

    }
}
