package lbt.com.amthuc.Views.FplashScreens;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.ldoublem.loadingviewlib.view.LVCircularJump;

import lbt.com.amthuc.Presenters.FplashScreens.igetdataapp;
import lbt.com.amthuc.Presenters.FplashScreens.lgetdataapp;
import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;

public class SplashScreenActivity extends AppCompatActivity implements igetdataapp {

    lgetdataapp mGetData;
    LVCircularJump mLVCircularJump;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splat_screen);
        mGetData = new lgetdataapp(this,this);
        mGetData.getthanhphan();

        mLVCircularJump =  findViewById(R.id.lv_CircularJump);
        mLVCircularJump.setViewColor(Color.WHITE);
        mLVCircularJump.startAnim();


        SharedPreferences sharedPreferences = getSharedPreferences("dataUser",Context.MODE_PRIVATE);
        String strUser = sharedPreferences.getString("user","");
        Log.e("kiemtra","user: "+strUser);

    }


    private void showAlert(String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(title);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(0);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void loadkhuvuc(boolean isSuccess) {

    }

    @Override
    public void saveMyData(boolean isSuccess, objkhuvuc_app mdata) {

    }

    @Override
    public void loitaidulieu_getDataApp() {
        showAlert(getText(R.string.loitaidulieukhoidonglai).toString());
    }

    @Override
    public void taidulieuthanhcong(boolean isRealTime) {
        if(!isRealTime) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, IndexActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 500);
        }
    }



}
