package lbt.com.amthuc.Views.TaiKhoan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import lbt.com.amthuc.Presenters.Account.iAccount;
import lbt.com.amthuc.Presenters.Account.lAccount;
import lbt.com.amthuc.Presenters.LoginRegister.ilogin;
import lbt.com.amthuc.Presenters.LoginRegister.llogin;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.ChiTietBaiViet.ChiTietBaiVietActivity;
import lbt.com.amthuc.customAdapter.aRclvDacSan;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;

public class BaiVietDaLuuActivity extends AppCompatActivity implements ilogin, iAccount {

    llogin mlogin;
    lAccount mAccount;

    Toolbar toolbar;
    RecyclerView rclvBaiVietDaLuu;
    ArrayList<objbaiviet_app> mListBaiViet, mListLoc;
    aRclvDacSan adapterBaiViet;
    Spinner spnLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_viet_da_luu);
        initView();
    }

    private void initView() {
        mlogin = new llogin(this,this);
        mAccount = new lAccount(this,this);
        spnLoc = findViewById(R.id.spinnerlocbvluu_admin);
        rclvBaiVietDaLuu = findViewById(R.id.rclvBaiVietDaLuu_admin);
        toolbar =  findViewById(R.id.toolbarbvluu_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.baivietdaluu);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        mListBaiViet = new ArrayList<>();
        adapterBaiViet = new aRclvDacSan(this,mListBaiViet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rclvBaiVietDaLuu.setHasFixedSize(false);
        rclvBaiVietDaLuu.setLayoutManager(linearLayoutManager);
        rclvBaiVietDaLuu.setAdapter(adapterBaiViet);

        mAccount.getListBaiVietDaLuu();

    }

    private void setupSpinnerLoc() {
        final ArrayList<String> listtitleLoc = new ArrayList<>();
        listtitleLoc.add(getText(R.string.tatca).toString());
        listtitleLoc.add(getText(R.string.thucan).toString());
        listtitleLoc.add(getText(R.string.nuocuong).toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,listtitleLoc);
        spnLoc.setAdapter(adapter);
        spnLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(mListBaiViet.size()!=0)
                    mAccount.locbaiviet(mListBaiViet,listtitleLoc.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onResume() {
        setupRecyclerView();
        super.onResume();
    }

    @Override
    public void taidulieuthatbai() {

    }

    @Override
    public void khongluumonan() {

    }

    @Override
    public void khongluunuocuong() {

    }

    @Override
    public void result_baivietdaluu(objbaiviet_app object) {
        mListBaiViet.add(object);
        adapterBaiViet.setmList(mListBaiViet);
        adapterBaiViet.notifyDataSetChanged();
        setupSpinnerLoc();
    }

    @Override
    public void result_capnhatthongtin(boolean isSuccess) {

    }

    @Override
    public void result_capnhatmatkhau(boolean isSuccess) {

    }

    @Override
    public void result_loc(ArrayList<objbaiviet_app> list) {
        mListLoc = list;
        adapterBaiViet = new aRclvDacSan(this,list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rclvBaiVietDaLuu.setHasFixedSize(false);
        rclvBaiVietDaLuu.setLayoutManager(linearLayoutManager);
        rclvBaiVietDaLuu.setAdapter(adapterBaiViet);
        adapterBaiViet.setOnItemClickListener(new aRclvDacSan.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("baiviet",mListLoc.get(position));
                bundle.putString("loai","monan");
                Intent intent = new Intent(BaiVietDaLuuActivity.this,ChiTietBaiVietActivity.class);
                intent.putExtra("data",bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void result_baivietcuatoi(ArrayList<objbaiviet_app> list) {

    }

    @Override
    public void result_delete(boolean isSuccess) {

    }

    @Override
    public void ResultLogin(boolean isSuccess) {

    }

    @Override
    public void result_listenner_values_user(objnguoidung_app muser) {

    }

    @Override
    public void result_dangnhap_sdt(boolean isSuccess) {

    }

    @Override
    public void code(String code) {

    }
}
