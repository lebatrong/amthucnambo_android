package lbt.com.amthuc.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lbt.com.amthuc.Views.TaiKhoan.dangbaiviet.iViewDangBaiViet;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;

public class modelDangBaiViet {

    final String TAG = "modelDangBaiViet";

    Context context;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;
    iViewDangBaiViet mInterface;

    List<String> listHinh;

    public modelDangBaiViet(Context context, iViewDangBaiViet mInterface) {
        this.context = context;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance("gs://thucannambo.appspot.com");
        listHinh = new ArrayList<>();
        this.mInterface = mInterface;
    }

    public void dangbaiviet(ArrayList<ImageView> listImv, final objchitietbaiviet baiviet, final objkhuvuc_app khuvuc, boolean isThucAn){

        final String type;
        if(isThucAn)
            type = "monans";
        else
            type = "nuocuongs";
        //UPLOAD IMAGE
        if(listHinh.size()!=listImv.size()){
            uploadImage(listImv,baiviet,khuvuc,isThucAn, type);
        }else {

            final DatabaseReference mRef = mDatabase.getReference()
                    .child("dacsans")
                    .child("KVMN")
                    .child(khuvuc.getMakhuvuc())
                    .child(type)
                    .child(mUser.getUid());
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<objbaiviet_app> listbaivietcuatoi = new ArrayList<>();
                    for (DataSnapshot val :dataSnapshot.getChildren()) {
                        objbaiviet_app bv = new objbaiviet_app();
                        bv.setIdbaiviet(val.getKey());
                        bv.setChitiet(val.getValue(objchitietbaiviet.class));
                        listbaivietcuatoi.add(bv);
                    }
                    String idbaiviet;
                    //ADD THÊM
                    if(listbaivietcuatoi.size()>0){
                        idbaiviet= mUser.getUid()+"_dacsans_"+type+"_"+khuvuc.getMakhuvuc()+"_"+(listbaivietcuatoi.size()+1);

                    }else { //ADD MỚI
                        idbaiviet = mUser.getUid()+"_dacsans_"+type+"_"+khuvuc.getMakhuvuc()+"_0";

                    }

                    baiviet.setHinh(listHinh);
                    mRef.child(idbaiviet).setValue(baiviet)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mInterface.resultDangBaiViet(true);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mInterface.resultDangBaiViet(false);
                                    Log.e(TAG,e.toString());
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG,databaseError.toString());
                    mInterface.resultDangBaiViet(false);
                }
            });
        }
    }

    private void uploadImage(final ArrayList<ImageView> listImv, final objchitietbaiviet baiviet, final objkhuvuc_app khuvuc, final boolean isThucAn, String type){
        String child = type;
        if(type.matches("monans"))
            child = "thucans";
        final int count = listImv.size();
        Calendar cal = Calendar.getInstance();
        for(int i=0 ; i<count; i++){
            final StorageReference mStorageRef = mStorage.getReference(child).child(type+"-"+i+"-"+cal.getTimeInMillis()+"-"+mUser.getUid());
            final int position = i;
            listImv.get(position).setDrawingCacheEnabled(true);
            listImv.get(position).buildDrawingCache();
            Bitmap bm = ((BitmapDrawable) listImv.get(position).getDrawable()).getBitmap();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,20, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mStorageRef.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata()
                            .getReference()
                            .getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            listHinh.add(uri.toString());
                            if(listHinh.size()==count)
                                dangbaiviet(listImv,baiviet,khuvuc,isThucAn);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mInterface.resultDangBaiViet(false);
                    Log.e(TAG,e.toString());
                }
            });
        }

    }
}
