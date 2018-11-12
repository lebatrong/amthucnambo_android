package lbt.com.amthuc.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import lbt.com.amthuc.R;

public class downloadImage {
    public String TAG = "downloadImage";
    FirebaseStorage storage;

    private static downloadImage instance;


    public static downloadImage getInstance() {
        if(instance==null)
            instance = new downloadImage();
        return instance;
    }

    private downloadImage() {

        this.storage  = FirebaseStorage.getInstance("gs://thucannambo.appspot.com");
    }

    public void dowloadImage(String url, final ImageView imageView){
        try {
            StorageReference sRef = storage.getReference().child(url);
            final long ONE_MEGABYTE = 1024 * 1024;
            sRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Drawable drawable = new BitmapDrawable(Resources.getSystem(), bm);
                    imageView.setImageDrawable(drawable);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, e.toString());
                    imageView.setImageResource(R.color.colorErrorImage);
                }
            });
        }catch (Exception e){
            Log.e(TAG,e.toString());
            imageView.setImageResource(R.color.colorErrorImage);
        }
    }


}
