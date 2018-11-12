package lbt.com.amthuc.Views.TaiKhoan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import lbt.com.amthuc.Presenters.Account.iAccount;
import lbt.com.amthuc.Presenters.Account.lAccount;
import lbt.com.amthuc.Presenters.LoginRegister.ilogin;
import lbt.com.amthuc.Presenters.LoginRegister.llogin;
import lbt.com.amthuc.R;
import lbt.com.amthuc.utils.CustomDialogLoading;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;
import lbt.com.amthuc.models.objectClass.firebase.objnguoidungs;

public class CapNhatTaiKhoanActivity extends AppCompatActivity implements ilogin, iAccount {

    llogin mLogin;
    lAccount mAccount;

    Toolbar toolbar;
    TextInputLayout tilHoTen,tilTenDangNhap,tilQueQuan;
    TextView tvNgaySinh;
    RadioButton rdoNam,rdoNu;
    Button btnCapNhatThongTin, btnChinhSuaMatKhau;
    CircleImageView crlhinhnen;

    private int REQUEST_CODE_IMAGE = 1;
    private boolean isTaiHinh;

    CustomDialogLoading mDialogLoading;

    objnguoidung_app mNguoiDung;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_tai_khoan);
        initView();
        actionCapnhat();
        actionChonHinhNen();
        actionchonngaysinh();
        actionSetData();
        actionCapNhatMatKhau();
    }

    private void actionCapNhatMatKhau() {
        if(mNguoiDung.getNguoidung().getLoaidangnhap().matches("sdt"))
            btnChinhSuaMatKhau.setVisibility(View.GONE);
        else
            btnChinhSuaMatKhau.setVisibility(View.VISIBLE);

        btnChinhSuaMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogLoading.showDialog(getText(R.string.dangcapnhatlai).toString());
                mAccount.resetPassword();
            }
        });
    }

    private void actionSetData() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mNguoiDung.getNguoidung().getNgaysinh());
        tvNgaySinh.setText(format.format(calendar.getTime()));

        if(mNguoiDung.getNguoidung().getGioitinh())
            rdoNam.setChecked(true);
        else
            rdoNu.setChecked(true);

        tilHoTen.getEditText().setText(mNguoiDung.getNguoidung().getHoten());
        tilTenDangNhap.getEditText().setText(mNguoiDung.getNguoidung().getEmail());
        tilQueQuan.getEditText().setText(mNguoiDung.getNguoidung().getQuequan());
        if(!mNguoiDung.getAnhdaidien().isEmpty())
            crlhinhnen.setImageDrawable(mLogin.getImage(mNguoiDung.getAnhdaidien()));
        else
            crlhinhnen.setImageResource(R.drawable.defaultuser);

    }

    private void actionchonngaysinh() {
        tvNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String []d = tvNgaySinh.getText().toString().split("/");
                int y = Integer.parseInt(d[2]);
                int m = Integer.parseInt(d[1]);
                int da = Integer.parseInt(d[0]);

                DatePickerDialog dialog = new DatePickerDialog(CapNhatTaiKhoanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int moth, int day) {
                        tvNgaySinh.setText(day+"/"+(moth+1)+"/"+year);

                    }
                },y,m-1,da);

                dialog.show();

            }
        });
    }

    private void actionChonHinhNen() {
        crlhinhnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/");
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });
    }

    private void actionCapnhat() {
        btnCapNhatThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogLoading.showDialog(getText(R.string.dangcapnhatlai).toString());
                try {
                    String ht = tilHoTen.getEditText().getText().toString();
                    String qq = tilQueQuan.getEditText().getText().toString();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                    calendar.setTime(format.parse(tvNgaySinh.getText().toString()));

                    long ngaysinh = calendar.getTimeInMillis();
                    objnguoidungs nd = mNguoiDung.getNguoidung();
                    nd.setHoten(ht);
                    nd.setGioitinh(rdoNam.isChecked());
                    nd.setQuequan(qq);
                    nd.setNgaysinh(ngaysinh);
                    mNguoiDung.setNguoidung(nd);

                    if (!isTaiHinh) {
                        mAccount.capnhattaikhoan(mNguoiDung);
                    }else{
                        mAccount.uploadAvatar(crlhinhnen,mNguoiDung);
                    }

                } catch (ParseException e) {
                    Log.e("kiemtra",e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        this.tilHoTen = findViewById(R.id.tilhoten);
        this.tilTenDangNhap = findViewById(R.id.tiltentaikhoan);
        this.tilQueQuan = findViewById(R.id.tilquequan);
        this.tvNgaySinh = findViewById(R.id.tvngaysinh);
        this.btnCapNhatThongTin = findViewById(R.id.btnCapNhatThongTin);
        this.btnChinhSuaMatKhau = findViewById(R.id.btnChinhSuaMatKhau);
        this.toolbar = findViewById(R.id.toolbarTaiKhoan);
        this.rdoNam = findViewById(R.id.rdonam);
        this.rdoNu = findViewById(R.id.rdonu);
        this.crlhinhnen = findViewById(R.id.crlhinhnen);

        mDialogLoading = new CustomDialogLoading(this);

       mLogin = new llogin(this,this);
       mAccount = new lAccount(this,this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        mNguoiDung = mLogin.getDataNguoiDung();
        isTaiHinh = false;
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

    }

    @Override
    public void result_capnhatthongtin(boolean isSuccess) {
        mDialogLoading.dismissDialog();
        if(isSuccess)
            Toast.makeText(this, getText(R.string.capnhatthanhcong), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getText(R.string.capnhatthatbai), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void result_capnhatmatkhau(boolean isSuccess) {
        mDialogLoading.dismissDialog();
        if(isSuccess)
            Toast.makeText(this, getText(R.string.kiemtramail), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getText(R.string.capnhatthatbai), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void result_loc(ArrayList<objbaiviet_app> list) {

    }

    @Override
    public void result_baivietcuatoi(ArrayList<objbaiviet_app> list) {

    }

    @Override
    public void result_delete(boolean isSuccess) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_IMAGE && data!=null){
            try {
                isTaiHinh = true;
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bm = BitmapFactory.decodeStream(inputStream);
                crlhinhnen.setImageBitmap(bm);

            } catch (FileNotFoundException e) {
                isTaiHinh = false;
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
