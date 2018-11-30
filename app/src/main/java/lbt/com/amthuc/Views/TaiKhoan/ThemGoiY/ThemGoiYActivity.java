package lbt.com.amthuc.Views.TaiKhoan.ThemGoiY;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kongzue.dialog.v2.WaitDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lbt.com.amthuc.Presenters.Account.GoiY.LogicGoiY;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.TaiKhoan.admin.AdminActivity;
import lbt.com.amthuc.Views.TaiKhoan.thanhphan.DanhSachThanhPhanActivity;
import lbt.com.amthuc.customAdapter.aRclvGoiY;
import lbt.com.amthuc.models.objectClass.app.objgoiy_app;
import lbt.com.amthuc.models.objectClass.app.objgoiychitiet_app;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;
import lbt.com.amthuc.utils.getDataApp;

public class ThemGoiYActivity extends AppCompatActivity implements iViewThemGoiY {

    Toolbar toolbar;
    RecyclerView rclvThanhPhan;
    LinearLayout lnlThemGoiY;

    LogicGoiY mLogic;
    getDataApp mGetData;

    aRclvGoiY adapter;
    ArrayList<objgoiychitiet_app> listgoiy;


    boolean[] checkedItems;
    ArrayList<objthanhphan> listThanhPhan;
    String[] listNameThanhPhan;
    ArrayList<Integer> mIndexThanhPhan;

    objthanhphan mThanhPhanAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_goi_y);
        initView();
        setupRclv();

    }

    private void setupRclv() {
        listgoiy = new ArrayList<>();
        adapter = new aRclvGoiY(this, listgoiy);
        LinearLayoutManager manager = new LinearLayoutManager((Context) this,LinearLayoutManager.VERTICAL,false);
        rclvThanhPhan.setLayoutManager(manager);
        rclvThanhPhan.setHasFixedSize(true);
        rclvThanhPhan.setAdapter(adapter);
        mLogic.danhsachgoiy();

        adapter.setOnClickListener(new aRclvGoiY.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                showDialogEdit(listgoiy.get(position));
            }
        });
    }

    private void showDialogEdit(final objgoiychitiet_app goiy){

        listThanhPhan = new ArrayList<>();
        listThanhPhan.clear();
        listThanhPhan =  mGetData.getAllListThanhPhan();
        listThanhPhan.remove(0);


        //=================================
        mIndexThanhPhan = new ArrayList<>();
        final int size_all = listThanhPhan.size();
        checkedItems = new boolean[size_all];
        listNameThanhPhan = new String[size_all];
        //KHỞI TẠO LIST NAME
        for(int i = 0 ; i<size_all; i++){
            listNameThanhPhan[i] = listThanhPhan.get(i).getTenthanhphan();
        }

        for(int i=0; i<size_all; i++){
            for(objthanhphan tp : goiy.getListgoiy()){
                if(tp.getMathanhphan().matches(listThanhPhan.get(i).getMathanhphan())){
                    checkedItems[i] = true;
                }
            }
        }

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.BOTTOM)
                .setContentHolder(new ViewHolder(R.layout.dialog_edit_goiy))
                .setExpanded(false)
                .setPadding(10,50,10,10)
                .setCancelable(true)
                .create();
        dialog.show();

        View v = dialog.getHolderView();
        TextView tvTenThanhPhanGoiY = v.findViewById(R.id.tvtenthanhphan_dialogedit);
        final TextView tvDSThanhPhanGoiY = v.findViewById(R.id.tvgoiy_dialogedit);

        //CAP NHAT GOI Y
        v.findViewById(R.id.btnCapNhatTP_dialogedit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objgoiy_app goiyUpdate = new objgoiy_app();
                goiyUpdate.setMathanhphan(goiy.getMathanhphan());
                List<String> listgoiy = new ArrayList<>();
                for(int index : mIndexThanhPhan){
                    listgoiy.add(listThanhPhan.get(index).getMathanhphan());
                }
                goiyUpdate.setListgoiy(listgoiy);
                mLogic.capnhatgoiy(goiyUpdate);
                dialog.dismiss();
            }
        });

        tvTenThanhPhanGoiY.setText(goiy.getTenthanhphan());
        int size = goiy.getListgoiy().size();
        String strthanhphan  = "";
        for(int i=0; i<size; i++){
            if(i==size-1){
                strthanhphan += goiy.getListgoiy().get(i).getTenthanhphan();
            }else {
                strthanhphan =goiy.getListgoiy().get(i).getTenthanhphan() +", " + strthanhphan;
            }
        }
        tvDSThanhPhanGoiY.setText(strthanhphan);

        v.findViewById(R.id.btnChonThanhPhan_dialogdiet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMultiSelectDialogEdit(tvDSThanhPhanGoiY);
            }
        });

    }

    private void showMultiSelectDialogEdit(final TextView tvDSThanhPhanGoiY){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.chonthanhphan));

        builder.setMultiChoiceItems(listNameThanhPhan, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean checked) {

            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton(getText(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //lấy giá trị index trong list checkedItems
                int size = checkedItems.length;
                mIndexThanhPhan.clear();
                for(int j=0; j<size; j++){
                    if(checkedItems[j])
                        mIndexThanhPhan.add(j);
                }

                //======

                int count = mIndexThanhPhan.size();
                if(count>0) {
                    String tp = "";
                    for (int position = 0; position < count; position++) {
                        if (position != count - 1)
                            tp = listThanhPhan.get(mIndexThanhPhan.get(position)).getTenthanhphan() + ", " + tp;
                        else
                            tp += " " + listThanhPhan.get(mIndexThanhPhan.get(position)).getTenthanhphan();
                    }
                    tvDSThanhPhanGoiY.setText(tp);
                }else {
                    tvDSThanhPhanGoiY.setText(getText(R.string.chonthanhphan));
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

    private void showDialogAdd(){

        //Khởi tạo dialog
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.BOTTOM)
                .setContentHolder(new ViewHolder(R.layout.dialog_themgoiy))
                .setExpanded(false)
                .setPadding(10,50,10,10)
                .setCancelable(true)
                .create();
        dialog.show();


        listThanhPhan = new ArrayList<>();
        listThanhPhan.clear();
        listThanhPhan =  mGetData.getAllListThanhPhan();
        listThanhPhan.remove(0);


        //=================================
        mIndexThanhPhan = new ArrayList<>();
        final int size_all = listThanhPhan.size();
        checkedItems = new boolean[size_all];
        listNameThanhPhan = new String[size_all];
        //KHỞI TẠO LIST NAME
        for(int i = 0 ; i<size_all; i++){
            listNameThanhPhan[i] = listThanhPhan.get(i).getTenthanhphan();
        }

        mThanhPhanAdd = new objthanhphan();
        View v = dialog.getHolderView();
        Spinner spinner = v.findViewById(R.id.nice_spinnerthanhphan_dialogAdd);
        ArrayAdapter<Integer> adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, listNameThanhPhan);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mThanhPhanAdd = listThanhPhan.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final TextView tvDSThanhPhanGoiY = v.findViewById(R.id.tvdanhsachgoiy_dialog_goiy);
        tvDSThanhPhanGoiY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMultiSelectDialogAdd(tvDSThanhPhanGoiY);
            }
        });

        //Thêm thành phần gợi ý
        v.findViewById(R.id.btnThemTP_dialog_goiy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objgoiy_app goiyAdd = new objgoiy_app();
                goiyAdd.setMathanhphan(mThanhPhanAdd.getMathanhphan());
                List<String> listgoiy = new ArrayList<>();
                for(int index : mIndexThanhPhan){
                    listgoiy.add(listThanhPhan.get(index).getMathanhphan());
                }
                goiyAdd.setListgoiy(listgoiy);
                mLogic.capnhatgoiy(goiyAdd);
                dialog.dismiss();
            }
        });


    }

    private void showMultiSelectDialogAdd(final TextView tvDSThanhPhanGoiY){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getText(R.string.chonthanhphan));

        builder.setMultiChoiceItems(listNameThanhPhan, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean checked) {

            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton(getText(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //lấy giá trị index trong list checkedItems
                int size = checkedItems.length;
                mIndexThanhPhan.clear();
                for(int j=0; j<size; j++){
                    if(checkedItems[j])
                        mIndexThanhPhan.add(j);
                }

                //======

                int count = mIndexThanhPhan.size();
                if(count>0) {
                    String tp = "";
                    for (int position = 0; position < count; position++) {
                        if (position != count - 1)
                            tp = listThanhPhan.get(mIndexThanhPhan.get(position)).getTenthanhphan() + ", " + tp;
                        else
                            tp += " " + listThanhPhan.get(mIndexThanhPhan.get(position)).getTenthanhphan();
                    }
                    tvDSThanhPhanGoiY.setText(tp);
                }else {
                    tvDSThanhPhanGoiY.setText(getText(R.string.chonthanhphan));
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

    private void initView() {
        toolbar = findViewById(R.id.toolbarthemgoiy);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rclvThanhPhan = findViewById(R.id.rclvListGoiY);
        lnlThemGoiY = findViewById(R.id.lnlThemGoiY);

        mLogic = new LogicGoiY(this,this);
        mGetData = new getDataApp(this);

        findViewById(R.id.lnlThemGoiY).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd();
            }
        });
    }

    @Override
    public void resultgoiy(ArrayList<objgoiychitiet_app> mlist) {
        WaitDialog.dismiss();
        listgoiy.clear();
        listgoiy = mlist;
        adapter.setmList(listgoiy);

    }

    @Override
    public void resultCapNhatGoiY(boolean isSuccess) {
        if(isSuccess){
            Toast.makeText(this, getText(R.string.capnhatthanhcong), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, getText(R.string.capnhatthatbai), Toast.LENGTH_SHORT).show();
        }
    }
}
