package lbt.com.amthuc.Views.TaiKhoan.dangbaiviet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kongzue.dialog.v2.WaitDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lbt.com.amthuc.Presenters.Account.DangBaiViet.logicDangBaiViet;
import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;
import lbt.com.amthuc.utils.getDataApp;

public class DangBaiVietActivity extends AppCompatActivity implements iViewDangBaiViet {

    int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<String> paths;

    Button btnChonHinhAnh, btnDang, btnchonkhuvuc;
    ImageView imv1,imv2,imv3,imv4,imv5, imvDel1,imvDel2,imvDel3,imvDel4,imvDel5;
    FrameLayout frm1,frm2,frm3,frm4,frm5;

    RadioButton rdoThucAn, rdoNuocUong;
    Button btnChonThanhPhan;
    EditText edtTen, edtGioiThieu, edtGhiChu, edtDiaChi, edtGiaTien;

    Toolbar toolbar;

    boolean[] checkedItems;
    ArrayList<objthanhphan> listThanhPhan;
    String[] listNameThanhPhan;
    ArrayList<Integer> mIndexThanhPhan;

    String[] listNameKhuVuc;
    ArrayList<objkhuvuc_app> listKhuVuc;
    objkhuvuc_app mKhuVucBaiViet;

    ArrayList<ImageView> listImv;
    ArrayList<ImageView> listRemove;
    ArrayList<FrameLayout> listFrm;

    getDataApp mGetData;

    logicDangBaiViet mLogic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_bai_viet);

        initView();
        actionChonHinhAnh();
        actionDang();
        actionChonThanhPhan();
        actionLoai();
        actionChonKhuVuc();
    }

    private void actionChonKhuVuc() {
        btnchonkhuvuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChonKhuVuc();
            }
        });
    }

    private void dialogChonKhuVuc(){
        listKhuVuc = mGetData.getListKhuVuc();
        if(listKhuVuc!=null){
            //SETUP LIST NAME KHU VUC
            int size = listKhuVuc.size();
            listNameKhuVuc = new String[size];
            for (int i=0; i<size; i++){
                listNameKhuVuc[i]=listKhuVuc.get(i).getChitietkhuvuc().getTentinh();
            }

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setCancelable(false);
            mBuilder.setTitle(getText(R.string.chonkhuvuc));
            mBuilder.setItems(listNameKhuVuc, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mKhuVucBaiViet = listKhuVuc.get(i);
                    btnchonkhuvuc.setText(mKhuVucBaiViet.getChitietkhuvuc().getTentinh());
                }
            });
            mBuilder.setPositiveButton(getText(R.string.huy), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            Dialog dialog = mBuilder.create();
            dialog.show();

        }else
            Toast.makeText(this, getText(R.string.loikhuvuc), Toast.LENGTH_SHORT).show();
    }

    private void actionLoai() {
        btnChonThanhPhan.setText(getText(R.string.chonthanhphan));
        String loai;
        if(rdoThucAn.isChecked())
            loai = "thanhphanmonan";
        else
            loai = "thanhphannuocuong";
        mIndexThanhPhan = new ArrayList<>();
        listThanhPhan = new ArrayList<>();
        listThanhPhan.clear();
        listThanhPhan =  mGetData.getListThanhPhan(loai);
        listThanhPhan.remove(0);
        int size = listThanhPhan.size();
        checkedItems = new boolean[size];
        listNameThanhPhan = new String[size];
        //KHỞI TẠO LIST NAME
        for(int i = 0 ; i<size; i++){
            listNameThanhPhan[i] = listThanhPhan.get(i).getTenthanhphan();
        }

        rdoThucAn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                actionLoai();
            }
        });
    }

    private void actionChonThanhPhan() {
        btnChonThanhPhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMultiSelectDialog();
            }
        });
    }

    private void showMultiSelectDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.chonthanhphan));

        builder.setMultiChoiceItems(listNameThanhPhan, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean checked) {
                if(checked) {
                    if (!mIndexThanhPhan.contains(position)) {
                        mIndexThanhPhan.add(position);
                    }
                }else{
                    if (mIndexThanhPhan.contains(position)) {
                        int count = mIndexThanhPhan.size();
                        for(int i=0; i<count; i++){
                            if(mIndexThanhPhan.get(i)==position) {
                                mIndexThanhPhan.remove(i);
                                break;
                            }
                        }
                    }
                }

            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton(getText(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                int count = mIndexThanhPhan.size();
                if(count>0) {
                    String tp = "";
                    for (int position = 0; position < count; position++) {
                        if (position != count - 1)
                            tp = listThanhPhan.get(mIndexThanhPhan.get(position)).getTenthanhphan() + ", " + tp;
                        else
                            tp += " " + listThanhPhan.get(mIndexThanhPhan.get(position)).getTenthanhphan();
                    }
                    btnChonThanhPhan.setText(tp);
                }else {
                    btnChonThanhPhan.setText(getText(R.string.chonthanhphan));
                }
            }
        });
        builder.setNegativeButton(getText(R.string.huy), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void actionDang() {
        btnDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtDiaChi.setError(null);
                edtGhiChu.setError(null);
                edtGiaTien.setError(null);
                edtGioiThieu.setError(null);
                edtTen.setError(null);
                mLogic.kiemtralogicdangbai(edtTen,edtGioiThieu,edtGiaTien,edtDiaChi,btnChonThanhPhan, btnchonkhuvuc, listFrm);

            }
        });
    }

    private void actionChonHinhAnh() {
        btnChonHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionSelectMultiImage();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarThemBaiViet);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mLogic = new logicDangBaiViet(this, this);

        btnChonHinhAnh = findViewById(R.id.btnchonhinhanh_baiviet);
        btnDang = findViewById(R.id.btnDang_baiviet);

        imv1 = findViewById(R.id.imv1_dangbai);
        imv2 = findViewById(R.id.imv2_dangbai);
        imv3 = findViewById(R.id.imv3_dangbai);
        imv4 = findViewById(R.id.imv4_dangbai);
        imv5 = findViewById(R.id.imv5_dangbai);

        listImv = new ArrayList<>();
        listFrm = new ArrayList<>();

        imvDel1 = findViewById(R.id.imvDelete1_dangbai);
        imvDel2 = findViewById(R.id.imvDelete2_dangbai);
        imvDel3 = findViewById(R.id.imvDelete3_dangbai);
        imvDel4 = findViewById(R.id.imvDelete4_dangbai);
        imvDel5 = findViewById(R.id.imvDelete5_dangbai);

        frm1 = findViewById(R.id.frmImv1);
        frm2 = findViewById(R.id.frmImv2);
        frm3 = findViewById(R.id.frmImv3);
        frm4 = findViewById(R.id.frmImv4);
        frm5 = findViewById(R.id.frmImv5);

        paths = new ArrayList<>();
        paths.clear();

        btnChonThanhPhan = findViewById(R.id.btnchonthanhphan_baiviet);
        btnchonkhuvuc = findViewById(R.id.btnchonkhuvuc_baiviet);
        rdoThucAn = findViewById(R.id.rdothucan_baiviet);
        rdoNuocUong = findViewById(R.id.rdonuocuong_baiviet);

        edtDiaChi = findViewById(R.id.edtdiachi_baiviet);
        edtGhiChu = findViewById(R.id.edtghichu_baiviet);
        edtGiaTien = findViewById(R.id.edtgia_baiviet);
        edtGioiThieu = findViewById(R.id.edtgioithieu_baiviet);
        edtTen = findViewById(R.id.edttenbaiviet_baiviet);

        mGetData = new getDataApp(this);

    }

    private void actionSelectMultiImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_MULTIPLE) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                if(data.getClipData() != null) {
                    paths = new ArrayList<>();
                    paths.clear();
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    if(count>5)
                        count = 5;
                    for (int i = 0; i < count; i++) {
                        paths.add(data.getClipData().getItemAt(i).getUri().toString());
                    }

                }else {
                    if(paths.size()<5)
                        paths.add(data.getData().toString());
                }

                setupImage();
            }
        }

    }


    private void setupImage() {
        int count_path = paths.size();
        if(count_path==5){
            btnChonHinhAnh.setVisibility(View.GONE);
        }else
            btnChonHinhAnh.setVisibility(View.VISIBLE);

        listImv = new ArrayList<>();
        listImv.add(imv1);
        listImv.add(imv2);
        listImv.add(imv3);
        listImv.add(imv4);
        listImv.add(imv5);


        listFrm = new ArrayList<>();
        listFrm.add(frm1);
        listFrm.add(frm2);
        listFrm.add(frm3);
        listFrm.add(frm4);
        listFrm.add(frm5);


        listRemove = new ArrayList<>();
        listRemove.add(imvDel1);
        listRemove.add(imvDel2);
        listRemove.add(imvDel3);
        listRemove.add(imvDel4);
        listRemove.add(imvDel5);

        for(int i=0; i<5; i++){
            listFrm.get(i).setVisibility(View.GONE);
        }

        for(int i=0; i<count_path; i++){
            Picasso.get().load(paths.get(i)).into(listImv.get(i));
            listFrm.get(i).setVisibility(View.VISIBLE);
        }

        for (int i=0; i<5; i++){
            final int positon = i;
            listRemove.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    paths.remove(positon);
                    setupImage();
                }
            });
        }

    }



    @Override
    public void loiten() {
        edtTen.setError(getText(R.string.loitenbaiviet));
    }

    @Override
    public void loidiachi() {
        edtDiaChi.setError(getText(R.string.loidiachibaiviet));
    }

    @Override
    public void loigioithieu() {
        edtGioiThieu.setError(getText(R.string.loigioithieubaiviet));;
    }

    @Override
    public void loithanhphan() {
        Toast.makeText(this, getText(R.string.loithanhphanbaiviet), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loigiatien() {
        edtGiaTien.setError(getText(R.string.loigiabaiviet));
    }

    @Override
    public void loichonkhuvuc() {
        Toast.makeText(this, getText(R.string.loichonkhuvucbaiviet), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loichonhinhanh() {
        Toast.makeText(this, getText(R.string.loichonhinhanhbaiviet), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void kiemtralogicthanhcong() {
        ArrayList<ImageView> lImv = new ArrayList<>();
        for(int i = 0; i<5; i++){
            if(listFrm.get(i).getVisibility() == View.VISIBLE){
                lImv.add(listImv.get(i));
            }
        }

        int count = mIndexThanhPhan.size();
        List<String> thanhphan = new ArrayList<>();
        for(int i=0; i<count; i++){
            thanhphan.add(listThanhPhan.get(mIndexThanhPhan.get(i)).getMathanhphan());
        }

        String ten, gioithieu, gia, ghichu, diachi;
        ten = edtTen.getText().toString();
        gioithieu = edtGioiThieu.getText().toString();
        gia = edtGiaTien.getText().toString();
        ghichu = edtGhiChu.getText().toString();
        diachi = edtDiaChi.getText().toString();


        objchitietbaiviet chitiet = new objchitietbaiviet();
        chitiet.setDiachi(diachi);
        chitiet.setTen(ten);
        chitiet.setGhichu(ghichu);
        chitiet.setGiathamkhao(Long.parseLong(gia));
        chitiet.setGioithieu(gioithieu);
        chitiet.setThanhphan(thanhphan);

        mLogic.dangbaiviet(lImv,chitiet,mKhuVucBaiViet,rdoThucAn.isChecked());
        btnDang.setEnabled(false);
        WaitDialog.show(this, "Loading");
    }

    @Override
    public void resultDangBaiViet(boolean isSuccess) {
        WaitDialog.dismiss();
        if(isSuccess){
            Toast.makeText(this, getText(R.string.dangbaivietthanhcong), Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            btnDang.setEnabled(true);
            Toast.makeText(this, getText(R.string.dangbaivietthatbai), Toast.LENGTH_SHORT).show();
        }
    }


}
