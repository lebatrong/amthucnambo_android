package lbt.com.amthuc.Views.ChiTietBaiViet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.card.MaterialCardView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.google.firebase.auth.FirebaseAuth;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.SelectDialog;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.transformer.DepthPageTransformer;
import com.view.jameson.library.CardScaleHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lbt.com.amthuc.MapActivity;
import lbt.com.amthuc.Presenters.Main.ichitietbaiviet;
import lbt.com.amthuc.Presenters.FplashScreens.igetdataapp;
import lbt.com.amthuc.Presenters.Main.lchitietbaiviet;
import lbt.com.amthuc.Presenters.FplashScreens.lgetdataapp;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.LoginRegister.MainLoginActivity;
import lbt.com.amthuc.Views.TaiKhoan.TaiKhoanMainActivity;
import lbt.com.amthuc.customAdapter.aRclvBinhLuan;
import lbt.com.amthuc.customAdapter.aRclvChung;
import lbt.com.amthuc.customAdapter.aRclvHinhChiTiet;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objdanhgia_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;

public class ChiTietBaiVietActivity extends AppCompatActivity implements igetdataapp, ichitietbaiviet {

    RadioButton rdoBinhLuan,rdogioithieu;
   // ViewPager vpghinh;
    Toolbar toolbar;

    objbaiviet_app mBaiViet;

    lgetdataapp mgetdata;

    ImageView imvLuuBaiViet;

    String baivietdaluu;

    lchitietbaiviet lchitiet;

    TextView tvTen,tvthongkesosao,tvsosao, tvxemtrenbando;

    LinearLayout lnlgioithieu, lnldanhgia;



    //ĐÁNH GIÁ
    TextView tvChuaCoDanhGia;
    ProgressBar pgbbinhluan;
    RecyclerView rclvBinhLuan;
    aRclvBinhLuan adapterBinhLuan;
    TextView tv1sao,tv2sao,tv3sao,tv4sao,tv5sao;
    Button btnDangDanhGia;
    ArrayList<objdanhgia_app> mListDanhGia;
    EditText edtNoidungdanhgia;
    MaterialCardView mcvDanhgia,mcvdangnhapdanhgia;

    //GIỚI THIỆU
    String loai;
    aRclvChung adapterBaiVietGoiY;
    RecyclerView rclvBaiVietGoiY;
    TextView tvGioiThieu,tvghichu,tvgia,tvdanhsachthanhphan, tvdiachi;
    ArrayList<objthanhphan> mlistThanhPhan;


    //RECYCLERVIEW
    private RecyclerView mRclvhinh;
    private ImageView mBlurView;
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    private List<String> mListHinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bai_viet);

        initView();
        setData();

        setupViewPagerHinh();

        actionSetupMenu();
        actionSoSaoDanhGia();
        actionXemTrenBanDo();

    }

    private void actionXemTrenBanDo() {
        tvxemtrenbando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("baiviet",mBaiViet);
                Intent intent = new Intent(ChiTietBaiVietActivity.this,MapActivity.class);
                intent.putExtra("data",bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionLuuBaiViet();
        actionDangDanhGia();
    }

    //==========PHẢI ĐĂNG NHẬP===================
    private void actionDangDanhGia() {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            mcvdangnhapdanhgia.setVisibility(View.GONE);
            mcvDanhgia.setVisibility(View.VISIBLE);

            objdanhgia_app mDanhGia = lchitiet.getdanhgiacuatoi(mListDanhGia);

            //có đánh giá rồi
            if(mDanhGia!=null){
                edtNoidungdanhgia.setText(mDanhGia.getChitietdanhgia().getBinhluan());
                switch ((int)mDanhGia.getChitietdanhgia().getSosao()){
                    case 1:
                        set1sao();
                        break;
                    case 2:
                        set2sao();
                        break;
                    case 3:
                        set3sao();
                        break;
                    case 4:
                        set4sao();
                        break;
                    case 5:
                        set5sao();
                        break;
                }
            }

            btnDangDanhGia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sosao = tv1sao.getText().toString()
                            + tv2sao.getText().toString()
                            + tv3sao.getText().toString()
                            + tv4sao.getText().toString()
                            + tv5sao.getText().toString();
                    int tongsaodanhgia;
                    if (sosao.matches(getText(R.string.rate1).toString()))
                        tongsaodanhgia = 1;
                    else if (sosao.matches(getText(R.string.rate2).toString()))
                        tongsaodanhgia = 2;
                    else if (sosao.matches(getText(R.string.rate3).toString()))
                        tongsaodanhgia = 3;
                    else if (sosao.matches(getText(R.string.rate4).toString()))
                        tongsaodanhgia = 4;
                    else
                        tongsaodanhgia = 5;

                    if(lchitiet.checklogicdanhgia(edtNoidungdanhgia.getText().toString())){
                        lchitiet.danhgiabaiviet(edtNoidungdanhgia.getText().toString(), tongsaodanhgia, mBaiViet.getIdbaiviet());
                    }

                }
            });

        }else {
            mcvDanhgia.setVisibility(View.GONE);
            mcvdangnhapdanhgia.setVisibility(View.VISIBLE);
            mcvdangnhapdanhgia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ChiTietBaiVietActivity.this,MainLoginActivity.class));
                }
            });
        }
    }

    private void actionLuuBaiViet() {
        imvLuuBaiViet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    if(!baivietdaluu.matches("")){
                        if(baivietdaluu.matches("luu")){
                            showDialog(getText(R.string.xoabaiviet).toString(),false);
                        }else{
                            showDialog(getText(R.string.luubaiviet).toString(),true);
                        }
                    }
                }
                else {
                    startActivity(new Intent(ChiTietBaiVietActivity.this,MainLoginActivity.class));
                    //Toast.makeText(ChiTietBaiVietActivity.this, getText(R.string.vuilongdangnhap), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //===========================================

    private void set1sao(){
        tv1sao.setText(getText(R.string.rate));
        tv2sao.setText(getText(R.string.norate));
        tv3sao.setText(getText(R.string.norate));
        tv4sao.setText(getText(R.string.norate));
        tv5sao.setText(getText(R.string.norate));
    }

    private void set2sao(){
        tv1sao.setText(getText(R.string.rate));
        tv2sao.setText(getText(R.string.rate));
        tv3sao.setText(getText(R.string.norate));
        tv4sao.setText(getText(R.string.norate));
        tv5sao.setText(getText(R.string.norate));
    }

    private void set3sao(){
        tv1sao.setText(getText(R.string.rate));
        tv2sao.setText(getText(R.string.rate));
        tv3sao.setText(getText(R.string.rate));
        tv4sao.setText(getText(R.string.norate));
        tv5sao.setText(getText(R.string.norate));
    }

    private void set4sao(){
        tv1sao.setText(getText(R.string.rate));
        tv2sao.setText(getText(R.string.rate));
        tv3sao.setText(getText(R.string.rate));
        tv4sao.setText(getText(R.string.rate));
        tv5sao.setText(getText(R.string.norate));
    }

    private void set5sao(){
        tv1sao.setText(getText(R.string.rate));
        tv2sao.setText(getText(R.string.rate));
        tv3sao.setText(getText(R.string.rate));
        tv4sao.setText(getText(R.string.rate));
        tv5sao.setText(getText(R.string.rate));
    }

    private void actionSoSaoDanhGia() {
        tv1sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set1sao();
            }
        });

        tv2sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set2sao();
            }
        });

        tv3sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set3sao();
            }
        });

        tv4sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set4sao();
            }
        });

        tv5sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set5sao();
            }
        });
    }

    private void actionSetupMenu() {
        if(rdoBinhLuan.isChecked()){
            lnldanhgia.setVisibility(View.VISIBLE);
            lnlgioithieu.setVisibility(View.GONE);
        }else{
            lnldanhgia.setVisibility(View.GONE);
            lnlgioithieu.setVisibility(View.VISIBLE);
        }
        rdoBinhLuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    lnldanhgia.setVisibility(View.VISIBLE);
                    lnlgioithieu.setVisibility(View.GONE);
                } else{
                    lnldanhgia.setVisibility(View.GONE);
                    lnlgioithieu.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupViewPagerHinh() {
        aRclvHinhChiTiet adapterhinh = new aRclvHinhChiTiet(mBaiViet.getChitiet().getHinh(), this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) this, LinearLayoutManager.HORIZONTAL, false);
        mRclvhinh.setLayoutManager(linearLayoutManager);
        mRclvhinh.setAdapter(adapterhinh);
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(2);
        mCardScaleHelper.attachToRecyclerView(mRclvhinh);
        mRclvhinh.setHasFixedSize(true);



    }


    private void setData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle bundle = getIntent().getBundleExtra("data");
        if(bundle!=null){
            mBaiViet = (objbaiviet_app) bundle.getSerializable("baiviet");
            toolbar.setTitle(mBaiViet.getChitiet().getTen());
            tvTen.setText(mBaiViet.getChitiet().getTen());
            lchitiet.getdanhgia(mBaiViet.getIdbaiviet(),true);
            if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                lchitiet.kiemtrabaivietdaluu(mBaiViet.getIdbaiviet());


            //ĐÁNH GIÁ
            mBaiViet = (objbaiviet_app) bundle.getSerializable("baiviet");
            lchitiet.getdanhgia(mBaiViet.getIdbaiviet(),false);

            //GIỚI THIỆU
            loai = bundle.getString("loai");
            tvGioiThieu.setText(mBaiViet.getChitiet().getGioithieu());
            tvghichu.setText(mBaiViet.getChitiet().getGhichu());



            //get list thành phần theo loại
            if(loai.matches("monan"))
                mlistThanhPhan = mgetdata.getListThanhPhan("thanhphanmonan");
            else
                mlistThanhPhan = mgetdata.getListThanhPhan("thanhphannuocuong");

            String thanhphan = "";
            int count = mBaiViet.getChitiet().getThanhphan().size();
            for (int i = 0; i<count; i++) {
                if(i<count-1) {
                    for (objthanhphan tp : mlistThanhPhan) {
                        if(tp.getMathanhphan().matches(mBaiViet.getChitiet().getThanhphan().get(i)))
                            thanhphan += tp.getTenthanhphan() + " ,";
                    }
                }
                else {
                    for (objthanhphan tp : mlistThanhPhan) {
                        if(tp.getMathanhphan().matches(mBaiViet.getChitiet().getThanhphan().get(i)))
                            thanhphan += tp.getTenthanhphan();
                    }
                }
            }

            tvdanhsachthanhphan.setText(thanhphan);
            tvdiachi.setText(mBaiViet.getChitiet().getDiachi());
            DecimalFormat format = new DecimalFormat("###,###");
            tvgia.setText(format.format(mBaiViet.getChitiet().getGiathamkhao()));

            lchitiet.getgoiy(loai.matches("monan") ? "nuocuongs" : "monans",mBaiViet);


        }else{
            finish();
            Toast.makeText(this, getText(R.string.loidulieu), Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        lchitiet = new lchitietbaiviet(this,this);
        mgetdata = new lgetdataapp(this,this);
       // vpghinh = findViewById(R.id.vpghinh);
        toolbar = findViewById(R.id.toolbarchitiet);
        imvLuuBaiViet = findViewById(R.id.imvLuubaiviet);
        baivietdaluu = "";
        rdogioithieu = findViewById(R.id.rdogioithieu);
        rdoBinhLuan = findViewById(R.id.rdobinhluan);

        tvthongkesosao = findViewById(R.id.tvthongkesosao_ct);
        tvsosao = findViewById(R.id.tvsosao);
        tvTen = findViewById(R.id.tvtenbaiviet);

        lnlgioithieu = findViewById(R.id.lnlgioithieu);
        lnldanhgia = findViewById(R.id.lnldanhgia);

        //ĐÁNH GIÁ
        rclvBinhLuan = findViewById(R.id.rclvBinhLuan);
        pgbbinhluan = findViewById(R.id.progressbinhluan);
        tvChuaCoDanhGia = findViewById(R.id.tvchuacodanhgia);
        tv1sao = findViewById(R.id.tv1sao_dangdanhgia);
        tv2sao = findViewById(R.id.tv2sao_dangdanhgia);
        tv3sao = findViewById(R.id.tv3sao_dangdanhgia);
        tv4sao = findViewById(R.id.tv4sao_dangdanhgia);
        tv5sao = findViewById(R.id.tv5sao_dangdanhgia);
        btnDangDanhGia = findViewById(R.id.btnDangDanhGia);
        edtNoidungdanhgia = findViewById(R.id.edtnoidungdanhgia);
        mcvDanhgia = findViewById(R.id.mcvdanhgiacuatoi);
        mcvdangnhapdanhgia = findViewById(R.id.mcvdangnhapdanhgia);
        mListDanhGia = new ArrayList<>();

        //GIỚI THIỆU
        rclvBaiVietGoiY = findViewById(R.id.rclvGoiY);
        tvghichu = findViewById(R.id.tvghichu_ct);
        tvgia = findViewById(R.id.tvgiathamkhao);
        tvGioiThieu = findViewById(R.id.tvbaiviet);
        tvdanhsachthanhphan = findViewById(R.id.tvthanhphan_ct);
        tvdiachi = findViewById(R.id.tvdiachi_ct);


        mRclvhinh = (RecyclerView) findViewById(R.id.rclvHinhChiTiet);


        tvxemtrenbando = findViewById(R.id.tvxemtrenbando);


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
    public void loadkhuvuc(boolean isSuccess) {

    }

    @Override
    public void saveMyData(boolean isSuccess, objkhuvuc_app mdata) {

    }

    @Override
    public void loitaidulieu_getDataApp() {

    }

    @Override
    public void taidulieuthanhcong(boolean rt) {

    }

    @Override
    public void loitaidulieu_chitietbaiviet() {
        pgbbinhluan.setVisibility(View.GONE);
        tvChuaCoDanhGia.setVisibility(View.VISIBLE);
    }

    @Override
    public void danhsachbinhluan(ArrayList<objdanhgia_app> list) {
        pgbbinhluan.setVisibility(View.GONE);
        tvChuaCoDanhGia.setVisibility(View.GONE);

        mListDanhGia = list;

        actionDangDanhGia();

        rclvBinhLuan.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager((Context) this,LinearLayoutManager.VERTICAL,false);


        adapterBinhLuan = new aRclvBinhLuan(this,list);

        rclvBinhLuan.setLayoutManager(manager);
        //rclvBinhLuan.setHasFixedSize(true);

        rclvBinhLuan.setAdapter(adapterBinhLuan);
    }

    @Override
    public void danhsachbaivietgoiy(final ArrayList<objbaiviet_app> list, boolean cobaivietgoiy) {

        if(cobaivietgoiy){
            LinearLayoutManager manager = new LinearLayoutManager((Context) this,LinearLayoutManager.HORIZONTAL,false);
            adapterBaiVietGoiY = new aRclvChung(this,list);
            rclvBaiVietGoiY.setLayoutManager(manager);
            rclvBaiVietGoiY.setAdapter(adapterBaiVietGoiY);
            rclvBaiVietGoiY.setHasFixedSize(true);
            adapterBaiVietGoiY.setOnClickListener(new aRclvChung.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("baiviet",list.get(position));
                    bundle.putString("loai",loai.matches("monan") ? "nuocuong" : "monan");
                    Intent intent = new Intent(ChiTietBaiVietActivity.this,ChiTietBaiVietActivity.class);
                    intent.putExtra("data",bundle);
                    startActivity(intent);
                }
            });
        }

    }

    private void showDialog(String mess, final boolean isLuu){

        SelectDialog.show(this,
                mess,
                null,
                getText(R.string.xacnhan).toString(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isLuu){
                            lchitiet.luubaivietluu(mBaiViet.getIdbaiviet());
                        }else{
                            lchitiet.xoabaivietluu(mBaiViet.getIdbaiviet());
                        }
                    }
                }, getText(R.string.huy).toString()
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


    }

    @Override
    public void chuacodanhgia() {
        pgbbinhluan.setVisibility(View.GONE);
        tvChuaCoDanhGia.setVisibility(View.VISIBLE);
    }

    @Override
    public void thongkedanhgia(float tongsao, float tongsaodanhgia, String rate) {
        tvsosao.setText(rate);
        tvthongkesosao.setText("("+(int)tongsaodanhgia+"/"+(int)tongsao+")");
    }

    @Override
    public void baivietdaluu() {
        imvLuuBaiViet.setImageResource(R.drawable.pin);
        baivietdaluu = "luu";
    }

    @Override
    public void baivietchualuu() {
        imvLuuBaiViet.setImageResource(R.drawable.nopin);
        baivietdaluu = "khongluu";
    }

    @Override
    public void results_luubaiviet(boolean isSuccess) {
        if(isSuccess){
            Toast.makeText(this, getText(R.string.luubaivietthanhcong), Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, getText(R.string.luubaivietkhongthanhcong), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void results_xoabaiviet(boolean isSuccess) {
        if(isSuccess){
            Toast.makeText(this, getText(R.string.xoabaivietthanhcong), Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, getText(R.string.xoabaivietkhongthanhcong), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void results_danhgiabaiviet(boolean isSuccess) {
        if(isSuccess){
            Toast.makeText(this, getText(R.string.danhgiathanhcong), Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, getText(R.string.danhgiakhongthanhcong), Toast.LENGTH_SHORT).show();
    }
}
