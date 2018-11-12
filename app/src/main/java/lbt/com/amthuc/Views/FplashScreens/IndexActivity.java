package lbt.com.amthuc.Views.FplashScreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import lbt.com.amthuc.Presenters.FplashScreens.igetdataapp;
import lbt.com.amthuc.Presenters.FplashScreens.lgetdataapp;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.Main.MainActivity;
import lbt.com.amthuc.utils.CustomDialogLoading;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;

public class IndexActivity extends AppCompatActivity implements igetdataapp {

    Button btnbatdau,btnchonkhuvuc;
    lgetdataapp mKhuVuc;
    CustomDialogLoading mDialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        initView();

        actionChonKhuVuc();
        actionStart();
        checkDataOld();
    }

    //KIỂM TRA NẾU CÓ DỮ LIỆU CŨ THÌ ÁP DỤNG DỮ LIỆU ĐÓ
    private void checkDataOld() {
        objkhuvuc_app objkv = mKhuVuc.getmykhuvuc();
        if(objkv!=null){
            btnchonkhuvuc.setText(objkv.getChitietkhuvuc().getTentinh());
            btnbatdau.setEnabled(true);
            btnbatdau.setVisibility(View.VISIBLE);
        }
    }

    private void actionStart() {
        btnbatdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndexActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void actionChonKhuVuc() {
        btnchonkhuvuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogLoading.showDialog(getText(R.string.dangtaidulieu).toString());
                mKhuVuc.getKhuVuc("KVMN");
            }
        });
    }



    private void initView() {
        mDialogLoading = new CustomDialogLoading(this);
        mKhuVuc = new lgetdataapp(this,this);
        btnbatdau = findViewById(R.id.btnbatdau);
        btnchonkhuvuc = findViewById(R.id.btnchontinhthanh);

        Bundle bundle = getIntent().getBundleExtra("data");
        if(bundle!=null){
            btnchonkhuvuc.setText(bundle.getString("tenkhuvuc"));
        }

        if(btnchonkhuvuc.getText().toString().matches(getText(R.string.chonvitri).toString())){
            btnbatdau.setVisibility(View.INVISIBLE);
            btnbatdau.setEnabled(false);
        }else{
            btnbatdau.setEnabled(true);
            btnbatdau.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadkhuvuc(boolean isSuccess) {
        mDialogLoading.dismissDialog();
       if(isSuccess){
           Intent intent = new Intent(this,SelectKhuVucActivity.class);
           startActivity(intent);

       }else{
           Toast.makeText(this, getText(R.string.taidulieukhongthanhcong), Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void saveMyData(boolean isSuccess, objkhuvuc_app mdata) {

    }

    @Override
    public void loitaidulieu_getDataApp() {

    }



    @Override
    public void taidulieuthanhcong( boolean i) {

    }


}
