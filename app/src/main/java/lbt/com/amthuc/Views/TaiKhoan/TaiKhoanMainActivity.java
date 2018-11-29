package lbt.com.amthuc.Views.TaiKhoan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.SelectDialog;

import java.util.ArrayList;

import lbt.com.amthuc.Presenters.Account.iAccount;
import lbt.com.amthuc.Presenters.Account.lAccount;
import lbt.com.amthuc.Presenters.LoginRegister.ilogin;
import lbt.com.amthuc.Presenters.LoginRegister.llogin;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.Main.SettingActivity;
import lbt.com.amthuc.Views.ChiTietBaiViet.ChiTietBaiVietActivity;
import lbt.com.amthuc.customAdapter.aRclvDacSan;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;

public class TaiKhoanMainActivity extends AppCompatActivity implements ilogin, iAccount {

    llogin mlogin;
    lAccount mAccount;


    Toolbar toolbar;
    Button btnLogout,btncapnhat;

    RecyclerView rclvBaiVietDaLuu;

    TextView tvTen, tvEmail;
    ImageView imvanhdaidien, imvgioitinh, imvSetting;

    objnguoidung_app mCTTK;

    ArrayList<objbaiviet_app> mListBaiViet, mListLoc;
    aRclvDacSan adapterBaiViet;



    Spinner spnLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan_main);

        initView();
        getData();
        actionToolbar();
        actionCapNhat();
        actionDangXuat();
        actionSetting();
    }

    private void actionSetting() {
        imvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaiKhoanMainActivity.this,SettingActivity.class));
            }
        });
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

    private void setupRecyclerView() {
        mListBaiViet = new ArrayList<>();
        adapterBaiViet = new aRclvDacSan(this,mListBaiViet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) this,LinearLayoutManager.VERTICAL,false);
        rclvBaiVietDaLuu.setHasFixedSize(false);
        rclvBaiVietDaLuu.setLayoutManager(linearLayoutManager);
        rclvBaiVietDaLuu.setAdapter(adapterBaiViet);

        mAccount.getListBaiVietDaLuu();
    }


    private void actionDangXuat() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectDialog.show(TaiKhoanMainActivity.this,
                        getText(R.string.banmuondangxuat).toString(),
                        null,
                        getText(R.string.ok).toString(),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                }, getText(R.string.huy).toString()
                        , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


            }
        });
    }

    private void actionCapNhat() {
        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaiKhoanMainActivity.this,CapNhatTaiKhoanActivity.class));
            }
        });
    }

    private void getData() {
        mCTTK = mlogin.getDataNguoiDung();
        if(mCTTK!=null) {
            mlogin.listenerValuesUser();

            if(mCTTK.getNguoidung().getGioitinh())
                imvgioitinh.setImageResource(R.drawable.gioitinhnam);
            else
                imvgioitinh.setImageResource(R.drawable.gioitinhnu);

            tvTen.setText(mCTTK.getNguoidung().getHoten());
            tvEmail.setText(mCTTK.getNguoidung().getEmail());
            String urlImg = mCTTK.getNguoidung().getAvatar();
            if (urlImg.matches("") || mlogin.getImage(mCTTK.getAnhdaidien())==null) {
                imvanhdaidien.setImageResource(R.drawable.defaultuser);
            } else {
                imvanhdaidien.setImageDrawable(mlogin.getImage(mCTTK.getAnhdaidien()));
            }
        }

    }

    private void actionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.chitiettaikhoan);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mlogin = new llogin(this,this);
        mAccount = new lAccount(this,this);

        btnLogout = findViewById(R.id.btnLogOut);
        btncapnhat = findViewById(R.id.btncapnhat_cttk);

        toolbar = (Toolbar) findViewById(R.id.toolbarAccount);

        tvTen = findViewById(R.id.tvten_cttk);
        tvEmail = findViewById(R.id.tvemai_cttk);
        imvanhdaidien = findViewById(R.id.imvanhdaidien);

        spnLoc = findViewById(R.id.spinnerloc_tk);

        imvgioitinh = findViewById(R.id.imvgioitinh_cttk);
        imvSetting = findViewById(R.id.imvsetting);

        rclvBaiVietDaLuu = findViewById(R.id.rclvBaiVietDaLuu);

        DialogSettings.blur_alpha = 200;
        DialogSettings.use_blur = true;
        DialogSettings.type = DialogSettings.TYPE_IOS;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogSettings.unloadAllDialog();
    }


    @Override
    public void ResultLogin(boolean isSuccess) {

    }

    @Override
    public void result_listenner_values_user(objnguoidung_app muser) {
        mCTTK = muser;
        tvTen.setText(mCTTK.getNguoidung().getHoten());
        tvEmail.setText(mCTTK.getNguoidung().getEmail());
        String urlImg = mCTTK.getNguoidung().getAvatar();
        if (urlImg.matches("") || mlogin.getImage(mCTTK.getAnhdaidien())==null) {
            imvanhdaidien.setImageResource(R.drawable.defaultuser);
        } else {
            imvanhdaidien.setImageDrawable(mlogin.getImage(mCTTK.getAnhdaidien()));
            // download.dowloadImage(mCTTK.getUser().getAvatar(), imvanhdaidien);
        }

        if(mCTTK.getNguoidung().getGioitinh())
            imvgioitinh.setImageResource(R.drawable.gioitinhnam);
        else
            imvgioitinh.setImageResource(R.drawable.gioitinhnu);
    }



    @Override
    public void code(String code) {

    }

    @Override
    public void taidulieuthatbai() {

    }

    @Override
    protected void onResume() {
        setupRecyclerView();
        super.onResume();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) this,LinearLayoutManager.VERTICAL,false);
        rclvBaiVietDaLuu.setHasFixedSize(false);
        rclvBaiVietDaLuu.setLayoutManager(linearLayoutManager);
        rclvBaiVietDaLuu.setAdapter(adapterBaiViet);
        adapterBaiViet.setOnItemClickListener(new aRclvDacSan.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("baiviet",mListLoc.get(position));
                bundle.putString("loai","monan");
                Intent intent = new Intent(TaiKhoanMainActivity.this,ChiTietBaiVietActivity.class);
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

}
