package lbt.com.amthuc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;

public class getDataApp {

    Context context;

    public getDataApp(Context context) {
        this.context = context;
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

    public ArrayList<objkhuvuc_app> getListKhuVuc(){
        SharedPreferences spf = context.getSharedPreferences("data_app",Context.MODE_PRIVATE);
        String data = spf.getString("khuvuc",null);
        Gson gson = new Gson();
        if(data!=null) {
            Type type = new TypeToken<ArrayList<objkhuvuc_app>>(){}.getType();
            return gson.fromJson(data,type);
        }else{
            return null;
        }
    }
}
