package lbt.com.amthuc.Views.TaiKhoan.chinhsuabaiviet;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kongzue.dialog.v2.WaitDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lbt.com.amthuc.Presenters.Account.ChinhSuaBaiViet.logicChinhSuaBaiViet;
import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.firebase.objchitietbaiviet;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;
import lbt.com.amthuc.utils.getDataApp;

public class ChinhSuaBaiVietActivity extends AppCompatActivity implements iViewChinhSuaBaiViet {


    objbaiviet_app mBaiVietEdit;

    int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<String> paths;

    Button btnChonHinhAnh, btnLuu;
    ImageView imv1,imv2,imv3,imv4,imv5, imvDel1,imvDel2,imvDel3,imvDel4,imvDel5;
    FrameLayout frm1,frm2,frm3,frm4,frm5;

    Button btnChonThanhPhan;
    EditText edtTen, edtGioiThieu, edtGhiChu, edtDiaChi, edtGiaTien;

    Toolbar toolbar;

    boolean[] checkedItems;
    ArrayList<objthanhphan> listThanhPhan;
    ArrayList<objthanhphan> listThanhPhanBaiViet;
    String[] listNameThanhPhan;
    ArrayList<Integer> mIndexThanhPhan;

    ArrayList<objkhuvuc_app> listKhuVuc;
    objkhuvuc_app mKhuVucBaiViet;


    ArrayList<ImageView> listImv;
    ArrayList<FrameLayout> listFrm;
    ArrayList<ImageView> listRemove;

    getDataApp mGetData;
    logicChinhSuaBaiViet mLogicChinhSua;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_bai_viet);

        initView();
        getdata();
        actionChonHinhAnh();
        actionChinhSua();
        actionChonThanhPhan();
        actionLoai();
    }

    private void getdata() {
        Bundle bundle = getIntent().getBundleExtra("data");
        if(bundle!=null){
            mBaiVietEdit = (objbaiviet_app) bundle.getSerializable("baiviet");
            listKhuVuc = mGetData.getListKhuVuc();
            setData(mBaiVietEdit);
           // Log.e("Kiemtra", new Gson().toJson(mBaiVietEdit));
        }else {
            finish();
        }
    }

    private void setData(objbaiviet_app baiviet) {

        String[] decode = baiviet.getIdbaiviet().split("_");
        String type = decode[2];
        String makhuvuc = decode[3];

        for(objkhuvuc_app i : listKhuVuc){
            if(i.getMakhuvuc().matches(makhuvuc)){
                mKhuVucBaiViet = i;
                break;
            }
        }

        String loai = "";
        if(type.matches("monans"))
            loai = "thanhphanmonan";
        else
            loai = "thanhphannuocuong";
        listThanhPhan = new ArrayList<>();
        listThanhPhan.clear();
        listThanhPhan =  mGetData.getListThanhPhan(loai);
        listThanhPhan.remove(0);


        //=================================
        mIndexThanhPhan = new ArrayList<>();
        int size_all = listThanhPhan.size();
        checkedItems = new boolean[size_all];
        listNameThanhPhan = new String[size_all];
        //KHỞI TẠO LIST NAME
        for(int i = 0 ; i<size_all; i++){
            listNameThanhPhan[i] = listThanhPhan.get(i).getTenthanhphan();
        }


        //GET LIST THÀNH PHẦN CỦA BÀI VIẾT
        listThanhPhanBaiViet = new ArrayList<>();
        for(objthanhphan i : listThanhPhan){
            for(String id: mBaiVietEdit.getChitiet().getThanhphan()){
                if(i.getMathanhphan().matches(id)){
                    listThanhPhanBaiViet.add(i);
                }
            }
        }

        for(int i=0; i<size_all; i++){
            for(objthanhphan tp : listThanhPhanBaiViet){
                if(tp.getMathanhphan().matches(listThanhPhan.get(i).getMathanhphan())){
                    checkedItems[i] = true;
                }
            }
        }

        //SET PATH HÌNH
        for(String i: mBaiVietEdit.getChitiet().getHinh()){
            paths.add(i);
        }
        setupImage();

        edtTen.setText(baiviet.getChitiet().getTen());
        edtDiaChi.setText(baiviet.getChitiet().getDiachi());
        edtGhiChu.setText(baiviet.getChitiet().getGhichu());
        edtGiaTien.setText(String.valueOf(baiviet.getChitiet().getGiathamkhao()));
        edtGioiThieu.setText(baiviet.getChitiet().getGioithieu());

    }


    private void actionLoai() {

        //SETUP TÊN BUTTON
        String tp = "";
        int size_bv = listThanhPhanBaiViet.size();
        for(int i=0; i<size_bv; i++){
            if (i != size_bv - 1)
                tp = listThanhPhanBaiViet.get(i).getTenthanhphan() + ", " + tp;
            else
                tp += " " + listThanhPhanBaiViet.get(i).getTenthanhphan();
        }
        btnChonThanhPhan.setText(tp);

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

    private void actionChinhSua() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtDiaChi.setError(null);
                edtGhiChu.setError(null);
                edtGiaTien.setError(null);
                edtGioiThieu.setError(null);
                edtTen.setError(null);
                mLogicChinhSua.kiemtralogiceditbaiviet(edtTen,edtGioiThieu,edtGiaTien,edtDiaChi,btnChonThanhPhan, listFrm);
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
        toolbar = findViewById(R.id.toolbarEditBaiViet);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mGetData = new getDataApp(this);
        mLogicChinhSua = new logicChinhSuaBaiViet(this,this);

        btnChonHinhAnh = findViewById(R.id.btnchonhinhanh_editbv);
        btnLuu = findViewById(R.id.btnluu_editbv);

        imv1 = findViewById(R.id.imv1_editbv);
        imv2 = findViewById(R.id.imv2_editbv);
        imv3 = findViewById(R.id.imv3_editbv);
        imv4 = findViewById(R.id.imv4_editbv);
        imv5 = findViewById(R.id.imv5_editbv);

        listImv = new ArrayList<>();
        listFrm = new ArrayList<>();

        imvDel1 = findViewById(R.id.imvDelete1_editbv);
        imvDel2 = findViewById(R.id.imvDelete2_editbv);
        imvDel3 = findViewById(R.id.imvDelete3_editbv);
        imvDel4 = findViewById(R.id.imvDelete4_editbv);
        imvDel5 = findViewById(R.id.imvDelete5_editbv);

        frm1 = findViewById(R.id.frmImv1_editbv);
        frm2 = findViewById(R.id.frmImv2_editbv);
        frm3 = findViewById(R.id.frmImv3_editbv);
        frm4 = findViewById(R.id.frmImv4_editbv);
        frm5 = findViewById(R.id.frmImv5_editbv);

        paths = new ArrayList<>();
        paths.clear();

        btnChonThanhPhan = findViewById(R.id.btnchonthanhphan_editbv);

        edtDiaChi = findViewById(R.id.edtdiachi_editbv);
        edtGhiChu = findViewById(R.id.edtghichu_editbv);
        edtGiaTien = findViewById(R.id.edtgia_editbv);
        edtGioiThieu = findViewById(R.id.edtgioithieu_editbv);
        edtTen = findViewById(R.id.edttenbaiviet_editbv);

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

        //ẨN HẾT HÌNH
        for(int i=0; i<5; i++){
            listFrm.get(i).setVisibility(View.GONE);
        }

        //LOAD HÌNH
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
    public void loichonhinhanh() {
        Toast.makeText(this, getText(R.string.loichonhinhanhbaiviet), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void kiemtralogicthanhcong() {

        int count = mIndexThanhPhan.size();
        List<String> thanhphan = new ArrayList<>();
        if(count!=0) {
            for (int i = 0; i < count; i++) {
                thanhphan.add(listThanhPhan.get(mIndexThanhPhan.get(i)).getMathanhphan());
            }
        }else {
            thanhphan = mBaiVietEdit.getChitiet().getThanhphan();
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

        mLogicChinhSua.capnhatbaiviet(paths,mBaiVietEdit,chitiet);
        btnLuu.setEnabled(false);
        WaitDialog.show(this,"Loading");
    }

    @Override
    public void resultDangBaiViet(boolean isSuccess) {
       WaitDialog.dismiss();
        if(isSuccess){
            Toast.makeText(this, getText(R.string.chinhsuabaivietthanhcong), Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            btnLuu.setEnabled(true);
            Toast.makeText(this, getText(R.string.chinhsuabaivietthatbai), Toast.LENGTH_SHORT).show();
        }

    }
}
