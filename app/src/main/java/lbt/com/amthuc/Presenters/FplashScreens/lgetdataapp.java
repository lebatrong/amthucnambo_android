package lbt.com.amthuc.Presenters.FplashScreens;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;
import lbt.com.amthuc.models.objectClass.firebase.objtinhthanh;

public class lgetdataapp {

    public final String TAG = "GETDATAAPP";

    FirebaseDatabase mDatabase;
    igetdataapp mInterface;
    Context context;

    boolean isRealtime;

    public lgetdataapp(Context context, igetdataapp mInterface) {
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mInterface = mInterface;
        this.context = context;
    }

    public void getKhuVuc(String vungmien){
        if(!checkData("data_app","khuvuc")) {
            DatabaseReference mRef = mDatabase.getReference().child("vungmiens").child(vungmien);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e(TAG, dataSnapshot.toString());
                    if (dataSnapshot.getValue() != null) {

                        ArrayList<objkhuvuc_app> listKV = new ArrayList<>();
                        for (DataSnapshot i : dataSnapshot.getChildren()) {
                            objkhuvuc_app kv = new objkhuvuc_app();
                            kv.setMakhuvuc(i.getKey());
                            kv.setChitietkhuvuc(i.getValue(objtinhthanh.class));
                            listKV.add(kv);
                        }

                        saveData(listKV);


                    } else
                        mInterface.loadkhuvuc(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, databaseError.toString());
                    mInterface.loadkhuvuc(false);
                }
            });
        }else
            mInterface.loadkhuvuc(true);
    }

    private void saveData(ArrayList<objkhuvuc_app> listKV){
        SharedPreferences spf = context.getSharedPreferences("data_app",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        Gson gson = new Gson();
        editor.clear();
        editor.putString("khuvuc",gson.toJson(listKV));

        if(editor.commit())
            mInterface.loadkhuvuc(true);
        else
            mInterface.loadkhuvuc(false);
    }

    private boolean checkData(String nameSharePre, String key){
        SharedPreferences spf = context.getSharedPreferences(nameSharePre,Context.MODE_PRIVATE);
        String data = spf.getString(key,null);
        if(data!=null)
            return true;
        else
            return false;

    }

    public objkhuvuc_app getmykhuvuc(){
        SharedPreferences spf = context.getSharedPreferences("mydata",Context.MODE_PRIVATE);
        String data = spf.getString("mykhuvuc",null);
        Gson gson = new Gson();
        if(data!=null) {
            return gson.fromJson(data, objkhuvuc_app.class);
        }else{
            return  null;
        }
    }

    public void savemykhuvuc(objkhuvuc_app kv){
        SharedPreferences spf = context.getSharedPreferences("mydata",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        Gson gson = new Gson();

        editor.clear();
        editor.putString("mykhuvuc",gson.toJson(kv));
        if(editor.commit())
            mInterface.saveMyData(true, kv);
        else
            mInterface.saveMyData(false,null);

    }

    public void getthanhphan(){
        isRealtime = false;
        DatabaseReference mRef = mDatabase.getReference().child("thanhphans");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    GenericTypeIndicator<List<objthanhphan>> gen = new GenericTypeIndicator<List<objthanhphan>>() {
                    };
                    List<objthanhphan> listtpmonan = dataSnapshot.child("monans").getValue(gen);
                    List<objthanhphan> listtpnuocuong = dataSnapshot.child("nuocuongs").getValue(gen);
                    SaveData(listtpmonan, listtpnuocuong);

                } else {
                    mInterface.loitaidulieu_getDataApp();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
                mInterface.loitaidulieu_getDataApp();
            }
        });
    }

    private void SaveData(List<objthanhphan> listtpmonan, List<objthanhphan> listtpnuocuong) {
        Gson gson = new Gson();
        SharedPreferences spf = context.getSharedPreferences("thanhphan",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.clear();
        editor.putString("thanhphanmonan",gson.toJson(listtpmonan));
        editor.putString("thanhphannuocuong",gson.toJson(listtpnuocuong));
        if(editor.commit())
            if(!isRealtime){
                mInterface.taidulieuthanhcong(isRealtime);
                isRealtime = true;
            }else{
                mInterface.taidulieuthanhcong(isRealtime);
            }

        else
            mInterface.loitaidulieu_getDataApp();
    }

    public ArrayList<objthanhphan> getListThanhPhan(String key){
        SharedPreferences spf = context.getSharedPreferences("thanhphan",Context.MODE_PRIVATE);
        String data = spf.getString(key,null);
        Gson gson = new Gson();
        if(data!=null) {
            Type type = new TypeToken<ArrayList<objthanhphan>>(){}.getType();
            return gson.fromJson(data,type);
        }else{
            return null;
        }
    }
}
