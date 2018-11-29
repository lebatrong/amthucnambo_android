package lbt.com.amthuc.Views.Main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kongzue.dialog.v2.WaitDialog;

import java.util.ArrayList;

import lbt.com.amthuc.Presenters.Main.iangi;
import lbt.com.amthuc.Presenters.FplashScreens.igetdataapp;
import lbt.com.amthuc.Presenters.Main.langi;
import lbt.com.amthuc.Presenters.FplashScreens.lgetdataapp;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.ChiTietBaiViet.ChiTietBaiVietActivity;
import lbt.com.amthuc.Views.FplashScreens.IndexActivity;
import lbt.com.amthuc.customAdapter.aRclvChung;
import lbt.com.amthuc.customAdapter.aRclvDacSan;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;


public class AnGiFragment extends Fragment implements igetdataapp, iangi {

    lgetdataapp mDataApp;

    objkhuvuc_app mObjKhuVuc;

    RecyclerView rclvChungAn,rclvDatBietAn;
    aRclvChung adapterChungAn;
    aRclvDacSan adapterDacSanAn;

    langi mAnGi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_an_gi, container, false);

        initView(v);
        getData();
        WaitDialog.show(getContext(),"Loading");


        return v;
    }


    private void initView(View v) {
        mDataApp = new lgetdataapp(getContext(),this);

        rclvChungAn = v.findViewById(R.id.rclvAnChung);
        rclvDatBietAn = v.findViewById(R.id.rclvAnDatBiet);

        mAnGi = new langi(this);




    }

    private void getData(){
        mObjKhuVuc = mDataApp.getmykhuvuc();
        if(mObjKhuVuc == null){
            Intent intent = new Intent(getActivity(),IndexActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        mAnGi.getDacSan(mObjKhuVuc.getMakhuvuc());

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
    public void loitaidulieu_AnGi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getText(R.string.loitaidulieukhoidonglai));
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                System.exit(0);
            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void danhsachbaivietchung_AnGi(final ArrayList<objbaiviet_app> list) {


    }

    @Override
    public void danhsachbaivietdacsan_AnGi(final ArrayList<objbaiviet_app> list) {
        WaitDialog.dismiss();
        if(list!=null) {
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rclvDatBietAn.setHasFixedSize(true);
            rclvDatBietAn.setLayoutManager(manager);
            adapterDacSanAn = new aRclvDacSan(getActivity(), list);
            rclvDatBietAn.setAdapter(adapterDacSanAn);

            adapterDacSanAn.setOnItemClickListener(new aRclvDacSan.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("baiviet", list.get(position));
                    bundle.putString("loai", "monan");
                    Intent intent = new Intent(getContext(), ChiTietBaiVietActivity.class);
                    intent.putExtra("data", bundle);
                    getActivity().startActivity(intent);
                }
            });
        }

    }

    @Override
    public void danhsachbaivietphobien_AnGi(final ArrayList<objbaiviet_app> list) {
        //TẢI XONG DỮ LIỆU DISMISS DIALOG
        WaitDialog.dismiss();
        if(list!=null) {
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rclvChungAn.setHasFixedSize(true);
            rclvChungAn.setLayoutManager(manager);
            adapterChungAn = new aRclvChung(getActivity(), list);
            rclvChungAn.setAdapter(adapterChungAn);

            adapterChungAn.setOnClickListener(new aRclvChung.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("baiviet", list.get(position));
                    bundle.putString("loai", "monan");
                    Intent intent = new Intent(getContext(), ChiTietBaiVietActivity.class);
                    intent.putExtra("data", bundle);
                    getActivity().startActivity(intent);
                }
            });
        }

    }

    @Override
    public void taidulieuthanhcong(boolean i) {

    }
}
