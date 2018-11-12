package lbt.com.amthuc.Views.FplashScreens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import lbt.com.amthuc.Presenters.FplashScreens.igetdataapp;
import lbt.com.amthuc.Presenters.FplashScreens.lgetdataapp;
import lbt.com.amthuc.R;
import lbt.com.amthuc.customAdapter.aRclvChonKhuVuc;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;

public class SelectKhuVucActivity extends AppCompatActivity implements igetdataapp {

    RecyclerView rclvKhuVuc;
    aRclvChonKhuVuc adapter;
    ArrayList<objkhuvuc_app> mList;

    lgetdataapp mKV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_khu_vuc);
        initView();
    }



    private void initView(){
        rclvKhuVuc = findViewById(R.id.rcrkhuvuc);
        mKV = new lgetdataapp(this,this);
        SharedPreferences preferences = getSharedPreferences("data_app",MODE_PRIVATE);
        String jsonkhuvuc = preferences.getString("khuvuc",null);
        if(jsonkhuvuc!=null){

            Gson gson = new Gson();
            Type listtype = new TypeToken<ArrayList<objkhuvuc_app>>(){}.getType();
            mList = gson.fromJson(jsonkhuvuc,listtype);
            setupRecyclerView();
        }else{
            Toast.makeText(this, getText(R.string.loikhuvuc), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupRecyclerView() {
        adapter = new aRclvChonKhuVuc(this,mList);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rclvKhuVuc.hasFixedSize();
        rclvKhuVuc.setLayoutManager(manager);
        rclvKhuVuc.setAdapter(adapter);

        adapter.setOnClickListener(new aRclvChonKhuVuc.OnClickListener() {
            @Override
            public void OnClick(View itemView, int position) {
                mKV.savemykhuvuc(mList.get(position));
            }
        });
    }

    @Override
    public void loadkhuvuc(boolean isSuccess) {

    }

    @Override
    public void saveMyData(boolean isSuccess, objkhuvuc_app mdata) {
        if(isSuccess) {
            Intent intent = new Intent(this, IndexActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Bundle bundle = new Bundle();
            bundle.putString("tenkhuvuc",mdata.getChitietkhuvuc().getTentinh());
            intent.putExtra("data",bundle);
            startActivity(intent);
            finish();
        }else
            Toast.makeText(this, getText(R.string.loikhuvuc), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loitaidulieu_getDataApp() {

    }


    @Override
    public void taidulieuthanhcong(boolean i) {

    }
}
