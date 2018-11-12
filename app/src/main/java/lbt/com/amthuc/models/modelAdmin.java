package lbt.com.amthuc.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import lbt.com.amthuc.Presenters.Account.iAccount;
import lbt.com.amthuc.Views.TaiKhoan.admin.iViewAdmin;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;
import lbt.com.amthuc.models.objectClass.firebase.objluubaiviet;

public class modelAdmin {
    public String TAG = "modelAdmin";
    FirebaseDatabase mDatabase;

    iViewAdmin mInterface;
    FirebaseUser mUser;
    Context context;

    public modelAdmin(iViewAdmin mInterface, Context context) {
        this.mInterface = mInterface;
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
        this.context = context;
    }

    public void getBaiVietCuaToi(){
        final String uid = mUser.getUid();
        final DatabaseReference mRef = mDatabase.getReference().child("dacsans").child("KVMN");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    ArrayList<objbaiviet_app> listbaivietcuatoi = new ArrayList<>();

                    //DUYỆT QUA CÁC THÀNH PHỐ TÌM BÀI VIẾT CỦA UID
                    for (DataSnapshot i :dataSnapshot.getChildren()) {

                        //LẶP NODE MONANS
                        for(DataSnapshot iduserbaiviet :i.child("monans").getChildren()){
                            if(iduserbaiviet.getKey().matches(uid)){
                                for (DataSnapshot val :iduserbaiviet.getChildren()) {
                                    objbaiviet_app bv = new objbaiviet_app();
                                    bv.setIdbaiviet(val.getKey());
                                    bv.setChitiet(val.getValue(objchitietbaiviet.class));
                                    listbaivietcuatoi.add(bv);

                                }

                                break;
                            }
                        }

                        //LẶP NODE NUOCUONGS
                        for(DataSnapshot iduserbaiviet :i.child("nuocuongs").getChildren()){
                            if(iduserbaiviet.getKey().matches(uid)){
                                for (DataSnapshot val :iduserbaiviet.getChildren()) {
                                    objbaiviet_app bv = new objbaiviet_app();
                                    bv.setIdbaiviet(val.getKey());
                                    bv.setChitiet(val.getValue(objchitietbaiviet.class));
                                    listbaivietcuatoi.add(bv);
                                }
                                break;
                            }
                        }

                    }//END FOR THÀNH PHỐ

                    mInterface.result_baivietcuatoi(listbaivietcuatoi);

                }//END IF
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.toString());
            }
        });
    }

    public void deleteBaiViet(final String idbaiviet){
        final String data[] = idbaiviet.split("_");
        final String type = data[2];
        String vitri = data[3];

        final DatabaseReference mRef = mDatabase.getReference()
                .child("dacsans")
                .child("KVMN")
                .child(vitri)
                .child(type)
                .child(mUser.getUid())
                .child(idbaiviet);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> gen = new GenericTypeIndicator<ArrayList<String>>(){};
                ArrayList<String> listHinh = dataSnapshot.child("hinh").getValue(gen);
                for(String idimage : listHinh){
                    StorageReference str = FirebaseStorage.getInstance().getReferenceFromUrl(idimage);
                    str.delete();
                }
                mRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mInterface.result_delete(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mInterface.result_delete(false);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
