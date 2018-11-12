package lbt.com.amthuc.Presenters.Account;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;
import lbt.com.amthuc.models.objectClass.firebase.objluubaiviet;

public class lAccount {
    public String TAG = "lAccount";
    FirebaseDatabase mDatabase;

    iAccount mInterface;
    FirebaseUser mUser;
    Context context;

    private StorageReference mStorageRef;

    public lAccount(iAccount mInterface, Context context) {
        this.mInterface = mInterface;
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
        this.context = context;
    }

    public void getListBaiVietDaLuu(){
        if(mUser!=null){
            DatabaseReference mRef = mDatabase.getReference().child("luulais").child(mUser.getUid());
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<objluubaiviet> mMonAn = new ArrayList<>();

                    if(dataSnapshot.child("monans").getValue()!=null) {
                        for (DataSnapshot i : dataSnapshot.child("monans").getChildren()) {
                            mMonAn.add(i.getValue(objluubaiviet.class));
                        }
                    }

                    ArrayList<objluubaiviet> mNuocUong = new ArrayList<>();
                    if(dataSnapshot.child("nuocuongs").getValue()!=null) {
                        for (DataSnapshot i : dataSnapshot.child("nuocuongs").getChildren()) {
                            mNuocUong.add(i.getValue(objluubaiviet.class));
                        }
                    }

                    getChiTietBaiVietDaLuu(mMonAn,mNuocUong);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG,databaseError.toString());
                    mInterface.taidulieuthatbai();
                }
            });
        }
    }

    private void getChiTietBaiVietDaLuu(ArrayList<objluubaiviet> listmonan, ArrayList<objluubaiviet> listnuocuong ){
        if(listmonan!=null){
            for (final objluubaiviet i : listmonan) {
                String iduserdangbai = i.getMabaiviet().substring(0,28);
                final String []data = i.getMabaiviet().substring(29,i.getMabaiviet().length()-1).split("_");
                String loai = data[0];
                DatabaseReference mRef;
                if(loai.matches("dacsans"))
                    mRef = mDatabase.getReference()
                            .child(loai)
                            .child("KVMN")
                            .child(i.getMakhuvuc())
                            .child("monans")
                            .child(iduserdangbai)
                            .child(i.getMabaiviet());
                else
                    mRef = mDatabase.getReference()
                            .child(loai)
                            .child("monans")
                            .child(iduserdangbai)
                            .child(i.getMabaiviet());

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null){
                            objbaiviet_app obj = new objbaiviet_app();
                            obj.setChitiet(dataSnapshot.getValue(objchitietbaiviet.class));
                            obj.setIdbaiviet(dataSnapshot.getKey());
                            mInterface.result_baivietdaluu(obj);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG,databaseError.toString());
                        mInterface.taidulieuthatbai();
                    }
                });
            }

        }else {
           mInterface.khongluumonan();
        }

        if(listnuocuong!=null){

            for (final objluubaiviet i : listnuocuong) {
                String iduserdangbai = i.getMabaiviet().substring(0,28);
                final String []data = i.getMabaiviet().substring(29,i.getMabaiviet().length()-1).split("_");
                String loai = data[0];
                DatabaseReference mRef;
                if(loai.matches("dacsans"))
                    mRef = mDatabase.getReference()
                            .child(loai)
                            .child("KVMN")
                            .child(i.getMakhuvuc())
                            .child("nuocuongs")
                            .child(iduserdangbai)
                            .child(i.getMabaiviet());
                else
                    mRef = mDatabase.getReference()
                            .child(loai)
                            .child("nuocuongs")
                            .child(iduserdangbai)
                            .child(i.getMabaiviet());

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null){
                            objbaiviet_app obj = new objbaiviet_app();
                            obj.setChitiet(dataSnapshot.getValue(objchitietbaiviet.class));
                            obj.setIdbaiviet(dataSnapshot.getKey());
                            mInterface.result_baivietdaluu(obj);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG,databaseError.toString());
                        mInterface.taidulieuthatbai();
                    }
                });
            }

        }else {
            mInterface.khongluunuocuong();
        }

    }

    public void capnhattaikhoan(final objnguoidung_app nguoidungupdate){
        DatabaseReference mRef = mDatabase.getReference()
                .child("nguoidungs")
                .child(mUser.getUid());
        mRef.setValue(nguoidungupdate.getNguoidung()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mInterface.result_capnhatthongtin(true);
                capnhatdanhgia(nguoidungupdate);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mInterface.result_capnhatthongtin(false);
                Log.e(TAG,e.toString());
            }
        });
    }

    private void capnhatdanhgia(final objnguoidung_app nguoidungupdate){
        DatabaseReference mRef = mDatabase.getReference()
                .child("danhgias");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        for (DataSnapshot j :i.getChildren()) {
                            if(j.getKey().matches(mUser.getUid())) {
                                DatabaseReference mr = mDatabase.getReference()
                                        .child("danhgias")
                                        .child(i.getKey())
                                        .child(j.getKey())
                                        .child("tennguoidanhgia");
                                mr.setValue(nguoidungupdate.getNguoidung().getHoten());
                                break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void uploadAvatar(ImageView imv, final objnguoidung_app nguoidungupdate){

        final FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://thucannambo.appspot.com");
        Calendar cal = Calendar.getInstance();
        mStorageRef = mStorage.getReference("avatar").child("avatar-"+cal.getTimeInMillis()+"-"+mUser.getUid());

        imv.setDrawingCacheEnabled(true);
        imv.buildDrawingCache();
        Bitmap bm = ((BitmapDrawable) imv.getDrawable()).getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,20, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mInterface.result_capnhatthongtin(false);
                Log.e(TAG,"upload: "+e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //PATH IMAGE NEW
                final String url = taskSnapshot.getMetadata().getReference().getPath();
                if (! nguoidungupdate.getNguoidung().getAvatar().matches("")) {
                    StorageReference stRef = mStorage.getReference().child(nguoidungupdate.getNguoidung().getAvatar());
                    stRef.delete().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mInterface.result_capnhatthongtin(false);
                            Log.e(TAG, "del: " + e.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            objnguoidung_app nd_app = nguoidungupdate;
                            nd_app.getNguoidung().setAvatar(url);
                            capnhattaikhoan(nd_app);
                        }
                    });
                }else {
                    objnguoidung_app nd_app = nguoidungupdate;
                    nd_app.getNguoidung().setAvatar(url);
                    capnhattaikhoan(nd_app);
                }
            }
        });
    }

    public void locbaiviet(ArrayList<objbaiviet_app> listBaiViet,  String type){
        if(type.matches(context.getText(R.string.tatca).toString())){
            mInterface.result_loc(listBaiViet);
        }else if(type.matches(context.getText(R.string.thucan).toString())){
            ArrayList<objbaiviet_app> listthucan = new ArrayList<>();
            for (objbaiviet_app i :listBaiViet) {
                final String []data = i.getIdbaiviet().substring(29,i.getIdbaiviet().length()-1).split("_");
                if(data[1].matches("monans"))
                    listthucan.add(i);
            }

            mInterface.result_loc(listthucan);

        }else {
            ArrayList<objbaiviet_app> listnuocuong = new ArrayList<>();
            for (objbaiviet_app i :listBaiViet) {
                final String []data = i.getIdbaiviet().substring(29,i.getIdbaiviet().length()-1).split("_");
                if(data[1].matches("nuocuongs"))
                    listnuocuong.add(i);
            }

            mInterface.result_loc(listnuocuong);
        }
    }

    public void resetPassword(){
        if(mUser!=null) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(mUser.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                mInterface.result_capnhatmatkhau(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mInterface.result_capnhatmatkhau(false);
                            Log.e(TAG, e.toString());
                        }
                    });
        }
    }

}
