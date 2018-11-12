package lbt.com.amthuc.models;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lbt.com.amthuc.Views.TaiKhoan.chinhsuabaiviet.iViewChinhSuaBaiViet;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;

public class modelChinhSuaBaiViet {

    final String TAG = "modelChinhSuaBaiViet";

    Context context;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;
    iViewChinhSuaBaiViet mInterface;

    List<String> listHinh;

    public modelChinhSuaBaiViet(Context context, iViewChinhSuaBaiViet mInterface) {
        this.context = context;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance("gs://thucannambo.appspot.com");
        listHinh = new ArrayList<>();
        this.mInterface = mInterface;
    }

    public void chinhsuabaiviet(ArrayList<String> listImv,
                                final objbaiviet_app baivietOld,
                                objchitietbaiviet ChiTiettUpdate) {

        String[] decode = baivietOld.getIdbaiviet().split("_");
        String type = decode[2];
        String khuvuc = decode[3];
        ArrayList<Uri> listUri = new ArrayList<>();
        for(String i: listImv){
            if(!baivietOld.getChitiet().getHinh().contains(i))
                listUri.add(Uri.parse(i));
        }

        if(listHinh.size()!=listUri.size()){
            uploadImage(listImv,baivietOld,ChiTiettUpdate, type,listUri);
        }else {

            ArrayList<String> listRemove = new ArrayList<>();
            //ADD lại hình cũ
            for (String i: baivietOld.getChitiet().getHinh()) {
                if(listImv.contains(i))
                    listHinh.add(i);
                else
                    listRemove.add(i);
            }

            removeImage(listRemove);

            final DatabaseReference mRef = mDatabase.getReference()
                    .child("dacsans")
                    .child("KVMN")
                    .child(khuvuc)
                    .child(type)
                    .child(mUser.getUid())
                    .child(baivietOld.getIdbaiviet());

            ChiTiettUpdate.setHinh(listHinh);

            mRef.setValue(ChiTiettUpdate)
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mInterface.resultDangBaiViet(false);
                    Log.e(TAG, e.toString());
                }
            })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mInterface.resultDangBaiViet(true);
                }
            });

        }

    }

    private void removeImage(ArrayList<String> url){
        for (String i :url) {
            StorageReference sRef = FirebaseStorage.getInstance().getReferenceFromUrl(i);
            sRef.delete();
        }
    }

    private void uploadImage(final ArrayList<String> listimv,
                             final objbaiviet_app baivietOld,
                             final objchitietbaiviet ChiTiettUpdate,
                             String type,
                             final ArrayList<Uri> listUri) {
        String child = type;
        if (type.matches("monans"))
            child = "thucans";

        final int count = listUri.size();
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < count; i++) {
            final StorageReference mStorageRef = mStorage.getReference(child)
                    .child(type +"-"+ i + "-" + "-" + cal.getTimeInMillis() + "-" + mUser.getUid());

            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(),listUri.get(i));
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,e.toString());
            }

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
                                    Log.e("kiemtra", listHinh.size() + "- " +count);
                                    if(listHinh.size()==count)
                                        chinhsuabaiviet(listimv,baivietOld, ChiTiettUpdate);

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
