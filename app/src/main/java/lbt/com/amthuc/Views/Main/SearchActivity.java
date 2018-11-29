package lbt.com.amthuc.Views.Main;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.dialog.v2.WaitDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lbt.com.amthuc.Presenters.Main.ifilter;
import lbt.com.amthuc.Presenters.Main.lfilter;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.ChiTietBaiViet.ChiTietBaiVietActivity;
import lbt.com.amthuc.customAdapter.aRclvDacSan;
import lbt.com.amthuc.customAdapter.aSnThanhPhan;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;
import lbt.com.amthuc.utils.getDataApp;

public class SearchActivity extends AppCompatActivity implements ifilter {

    Toolbar toolbar;

    TextView tvTien;
    SeekBar sbTien;
    RelativeLayout rltlHien;
    LinearLayout lnlLoc;
    ImageView imvAn;
    EditText edtTenMon;

    Button btnLoc,btnChonLaiKV;

    getDataApp mGetDataApp;

    objkhuvuc_app mObjKhuVuc;

    Spinner spinnerThanhPhan;

    ArrayList<objthanhphan> mlistThanhPhan;
    aSnThanhPhan adapterThanhPhan;


    ArrayList<objbaiviet_app> mListResultSearch;

    objthanhphan mThanhPhan;

    aRclvDacSan adapterSearch;
    RecyclerView rclvSearch;

    RadioButton rdoMonAn,rdoNuocUong;

    lfilter mFilter;


    String[] listNameKhuVuc;
    ArrayList<objkhuvuc_app> listKhuVuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();

        actionTien();
        actionAnHienLoc();
        actionChonLaiKhuVuc();
        actionLoc();
        actionLoaiTimKiem();
        setupSpinnerThanhPhan("thanhphanmonan");
    }

    private void actionLoaiTimKiem() {
        rdoMonAn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    setupSpinnerThanhPhan("thanhphanmonan");
                }else{
                    setupSpinnerThanhPhan("thanhphannuocuong");
                }
            }
        });
    }

    private void actionLoc() {
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!btnChonLaiKV.getText().toString().matches(getText(R.string.chonkhuvuc).toString())){
                    WaitDialog.show(SearchActivity.this,"Loading");
                    String tenmonan = edtTenMon.getText().toString();
                    long giatien = sbTien.getProgress();
                    String mathanhphan = mThanhPhan.getMathanhphan();
                    mFilter.timkiem(rdoMonAn.isChecked(),mObjKhuVuc.getMakhuvuc(), tenmonan, mathanhphan, giatien);
                }
                else
                    Toast.makeText(SearchActivity.this, getText(R.string.loichonkhuvucbaiviet), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setupSpinnerThanhPhan(String loai) {
        mlistThanhPhan = mGetDataApp.getListThanhPhan(loai);
        if(mlistThanhPhan!=null){

            adapterThanhPhan = new aSnThanhPhan(this,mlistThanhPhan);
            spinnerThanhPhan.setAdapter(adapterThanhPhan);

            spinnerThanhPhan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mThanhPhan = mlistThanhPhan.get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }else{
            Toast.makeText(this, getText(R.string.loitaidulieu).toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogChonKhuVuc(final Button btnChonKhuVuc){
        listKhuVuc = mGetDataApp.getListKhuVuc();
        if(listKhuVuc!=null){
            //SETUP LIST NAME KHU VUC
            int size = listKhuVuc.size();
            listNameKhuVuc = new String[size];
            for (int i=0; i<size; i++){
                listNameKhuVuc[i]=listKhuVuc.get(i).getChitietkhuvuc().getTentinh();
            }

            android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(this);
            mBuilder.setCancelable(false);
            mBuilder.setTitle(getText(R.string.chonkhuvuc));
            mBuilder.setItems(listNameKhuVuc, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mObjKhuVuc = listKhuVuc.get(i);
                    btnChonKhuVuc.setText(mObjKhuVuc.getChitietkhuvuc().getTentinh());
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

    private void initView() {
        toolbar = findViewById(R.id.toolbarSearch);
        mFilter = new lfilter(this);
        mGetDataApp = new getDataApp(this);
        tvTien = findViewById(R.id.tvgiatientoida);
        sbTien = findViewById(R.id.seekbartien);
        rltlHien = findViewById(R.id.rltlHienLoc);
        lnlLoc = findViewById(R.id.lnlLoc);
        imvAn = findViewById(R.id.imvan);
        btnChonLaiKV   = findViewById(R.id.btnchonlaikhuvuc);
        btnLoc = findViewById(R.id.btnloc);
        edtTenMon = findViewById(R.id.edtTenMonAn);
        spinnerThanhPhan = findViewById(R.id.spinnerthanhphan);
        rclvSearch = findViewById(R.id.rclvSearch);
        rdoMonAn = findViewById(R.id.rdothucan);
        rdoNuocUong = findViewById(R.id.rdonuocuong);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void actionTien() {
        sbTien.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                DecimalFormat decimalFormat = new DecimalFormat("###,###");
                tvTien.setText(decimalFormat.format(i) +" đ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void actionAnHienLoc() {
        rltlHien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnlLoc.setVisibility(View.VISIBLE);
                rltlHien.setVisibility(View.GONE);
            }
        });

        imvAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnlLoc.setVisibility(View.GONE);
                rltlHien.setVisibility(View.VISIBLE);
            }
        });


    }

    private void actionChonLaiKhuVuc() {
        btnChonLaiKV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChonKhuVuc(btnChonLaiKV);
            }
        });
    }



    @Override
    public void loitaidulieu_filter() {
        Toast.makeText(this, getText(R.string.loitaidulieu), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ketquatimkiem(ArrayList<objbaiviet_app> baiviet, boolean coketqua) {

        //TẢI XONG DỮ LIỆU DISMISS DIALOG
        WaitDialog.dismiss();

        if(coketqua) {
            rclvSearch.setVisibility(View.VISIBLE);
            lnlLoc.setVisibility(View.GONE);
            rltlHien.setVisibility(View.VISIBLE);

            mListResultSearch = baiviet;
            adapterSearch = new aRclvDacSan(this, baiviet);

            LinearLayoutManager manager = new LinearLayoutManager((Context) this, LinearLayoutManager.VERTICAL, false);
            rclvSearch.setHasFixedSize(true);
            rclvSearch.setLayoutManager(manager);

            rclvSearch.setAdapter(adapterSearch);
            adapterSearch.notifyDataSetChanged();

            adapterSearch.setOnItemClickListener(new aRclvDacSan.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("baiviet",mListResultSearch.get(position));
                    if(rdoMonAn.isChecked())
                        bundle.putString("loai","monan");
                    else
                        bundle.putString("loai","nuocuong");
                    Intent intent = new Intent(SearchActivity.this,ChiTietBaiVietActivity.class);
                    intent.putExtra("data",bundle);
                    startActivity(intent);
                }
            });


        }else{
            rclvSearch.setVisibility(View.GONE);
            Toast.makeText(this, getText(R.string.khongcodulieu), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
