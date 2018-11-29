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

import lbt.com.amthuc.Presenters.FplashScreens.igetdataapp;
import lbt.com.amthuc.Presenters.Main.iuonggi;
import lbt.com.amthuc.Presenters.FplashScreens.lgetdataapp;
import lbt.com.amthuc.Presenters.Main.luonggi;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.ChiTietBaiViet.ChiTietBaiVietActivity;
import lbt.com.amthuc.Views.FplashScreens.IndexActivity;
import lbt.com.amthuc.customAdapter.aRclvChung;
import lbt.com.amthuc.customAdapter.aRclvDacSan;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;


public class UongGiFragment extends Fragment implements igetdataapp,iuonggi {

    lgetdataapp mDataApp;

    objkhuvuc_app mObjKhuVuc;

    RecyclerView rclvChung,rclvDatBiet;
    aRclvChung adapterChungUong;
    aRclvDacSan adapterDacSanUong;

    luonggi mUongGi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_uong_gi, container, false);

        initView(v);
        getData();
        WaitDialog.show(getContext(),"Loading");


        return v;
    }



    private void initView(View v) {
        mDataApp = new lgetdataapp(getContext(),this);

        rclvChung = v.findViewById(R.id.rclvUongChung);
        rclvDatBiet = v.findViewById(R.id.rclvUongDatBiet);

        mUongGi = new luonggi(this);

    }

    private void getData(){
        mObjKhuVuc = mDataApp.getmykhuvuc();
        if(mObjKhuVuc == null){
            Intent intent = new Intent(getActivity(),IndexActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        mUongGi.getBaiVietDatSan(mObjKhuVuc.getMakhuvuc());
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
    public void taidulieuthanhcong(boolean isRealtime) {

    }

    @Override
    public void loitaidulieu_UongGi() {
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
    public void danhsachbaivietchung_uonggi(final ArrayList<objbaiviet_app> list) {

    }

    @Override
    public void danhsachbaivietdacsan_uonggi(final ArrayList<objbaiviet_app> list) {
        WaitDialog.dismiss();
        if(list!=null) {
            adapterDacSanUong = new aRclvDacSan(getActivity(), list);

            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rclvDatBiet.setHasFixedSize(true);
            rclvDatBiet.setLayoutManager(manager);

            rclvDatBiet.setAdapter(adapterDacSanUong);

            adapterDacSanUong.setOnItemClickListener(new aRclvDacSan.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("baiviet", list.get(position));
                    bundle.putString("loai", "nuocuong");
                    Intent intent = new Intent(getContext(), ChiTietBaiVietActivity.class);
                    intent.putExtra("data", bundle);
                    getActivity().startActivity(intent);
                }
            });
        }

    }

    @Override
    public void danhsachbaivietphobien_uonggi(final ArrayList<objbaiviet_app> list) {
        WaitDialog.dismiss();
        if(list!=null) {
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rclvChung.setHasFixedSize(true);
            rclvChung.setLayoutManager(manager);

            adapterChungUong = new aRclvChung(getContext(), list);
            rclvChung.setAdapter(adapterChungUong);

            adapterChungUong.setOnClickListener(new aRclvChung.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("baiviet", list.get(position));
                    bundle.putString("loai", "nuocuong");
                    Intent intent = new Intent(getContext(), ChiTietBaiVietActivity.class);
                    intent.putExtra("data", bundle);
                    getActivity().startActivity(intent);
                }
            });
        }
    }
}
