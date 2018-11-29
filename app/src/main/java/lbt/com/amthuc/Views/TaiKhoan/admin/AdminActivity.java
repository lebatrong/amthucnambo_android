package lbt.com.amthuc.Views.TaiKhoan.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.SelectDialog;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import lbt.com.amthuc.Presenters.Account.admin.logicAdmin;
import lbt.com.amthuc.Presenters.LoginRegister.ilogin;
import lbt.com.amthuc.Presenters.LoginRegister.llogin;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.TaiKhoan.TaiKhoanMainActivity;
import lbt.com.amthuc.Views.TaiKhoan.ThemGoiY.ThemGoiYActivity;
import lbt.com.amthuc.Views.TaiKhoan.thanhphan.DanhSachThanhPhanActivity;
import lbt.com.amthuc.Views.TaiKhoan.thanhphan.ThemThanhPhanActivity;
import lbt.com.amthuc.Views.ChiTietBaiViet.ChiTietBaiVietActivity;
import lbt.com.amthuc.Views.Main.SettingActivity;
import lbt.com.amthuc.Views.TaiKhoan.BaiVietDaLuuActivity;
import lbt.com.amthuc.Views.TaiKhoan.CapNhatTaiKhoanActivity;
import lbt.com.amthuc.Views.TaiKhoan.dangbaiviet.DangBaiVietActivity;
import lbt.com.amthuc.customAdapter.aRclvBaiVietCuaToi;
import lbt.com.amthuc.customView.EndDrawerToggle;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;
import lbt.com.amthuc.models.objectClass.firebase.objtinhthanh;
import lbt.com.amthuc.utils.getDataApp;

public class AdminActivity extends AppCompatActivity implements iViewAdmin, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private EndDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigation;

    AlertView mAlertView;

    llogin mlogin;
    logicAdmin mLogicAdmin;

    objnguoidung_app mCTTK;

    TextView tvTen, tvEmail, tvThemBV;
    ImageView imvanhdaidien, imvFilter;

    XRecyclerView rclvBaiVietCuaToi;
    aRclvBaiVietCuaToi adapterRclv;
    ArrayList<objbaiviet_app> listBaiVietFilter, listBaiVietDefault, listLoadmore;

    getDataApp mGetData;

    String[] listNameKhuVuc;
    ArrayList<objkhuvuc_app> listKhuVuc;
    objkhuvuc_app mKhuVucFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initView();
        getData();
        actionGetImage();
        mLogicAdmin.getlistbaivietcuatoi();

        actionLoc();
    }

    private void actionLoc() {
        imvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialog = DialogPlus.newDialog(AdminActivity.this)
                        .setGravity(Gravity.TOP)
                        .setContentHolder(new ViewHolder(R.layout.dialog_filter_baivietcuatoi))
                        .setExpanded(false)
                        .setPadding(10,40,10,10)
                        .setCancelable(true)
                        .create();
                dialog.show();

                //INIT VIEW
                View v = dialog.getHolderView();
                final SwitchButton sbLoaiLoc = v.findViewById(R.id.sbkieuloc_filter);
                final Button btnchonkhuvuc =  v.findViewById(R.id.btnChonKhuVuc_dialogfilter);
                final TextView tvKieuLoc = v.findViewById(R.id.tvkieuloc_filter);
                final LinearLayout lnlCustomLoc = v.findViewById(R.id.lnlCustomLoc_filter);
                Button btnLoc = v.findViewById(R.id.btnLoc_dialogfilter);
                final RadioButton rdoThucAn = v.findViewById(R.id.rodthucan_dialogfilter);

                btnLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        mLogicAdmin.locbaiviet(!sbLoaiLoc.isChecked(),rdoThucAn.isChecked(),mKhuVucFilter,listBaiVietDefault);
                    }
                });


                btnchonkhuvuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogChonKhuVuc(btnchonkhuvuc);
                    }
                });

                if(!sbLoaiLoc.isChecked()){
                    tvKieuLoc.setText(getText(R.string.loctatcafilter));
                    lnlCustomLoc.setVisibility(View.GONE);
                }else {
                    tvKieuLoc.setText(getText(R.string.loctuychonfilter));
                    lnlCustomLoc.setVisibility(View.VISIBLE);
                }

                sbLoaiLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(!b){
                            tvKieuLoc.setText(getText(R.string.loctatcafilter));
                            lnlCustomLoc.setVisibility(View.GONE);
                        }else {
                            tvKieuLoc.setText(getText(R.string.loctuychonfilter));
                            lnlCustomLoc.setVisibility(View.VISIBLE);
                            mKhuVucFilter = new objkhuvuc_app("null",new objtinhthanh("Tất cả",0,0));
                            btnchonkhuvuc.setText("Tất cả");
                        }
                    }
                });



            }
        });
    }

    private void dialogChonKhuVuc(final Button btnChonKhuVuc){
        listKhuVuc = new ArrayList<>();
        listKhuVuc.add(new objkhuvuc_app("null",new objtinhthanh("Tất cả",0,0)));
        listKhuVuc.addAll(mGetData.getListKhuVuc());
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
                    mKhuVucFilter = listKhuVuc.get(i);
                    btnChonKhuVuc.setText(mKhuVucFilter.getChitietkhuvuc().getTentinh());
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

    private void actionGetImage() {
        tvThemBV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this,DangBaiVietActivity.class));
            }
        });
    }



    private void initView() {
        toolbar = findViewById(R.id.toolbaradmin);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getText(R.string.trangcanhan));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imvFilter = findViewById(R.id.imvFilter);

        drawerLayout = findViewById(R.id.drawarlayoutadmin);
        toggle = new EndDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);

        navigation = findViewById(R.id.navigationviewadmin);
        if (navigation != null) {
            navigation.setNavigationItemSelectedListener(this);
        }


        mLogicAdmin = new logicAdmin(this,this);

        View header = navigation.getHeaderView(0);

        tvTen = header.findViewById(R.id.tvten_admin);
        tvEmail = header.findViewById(R.id.tvemai_admin);
        imvanhdaidien = header.findViewById(R.id.imvanhdaidien_admin);

        rclvBaiVietCuaToi = findViewById(R.id.rclvBaiVietCuaToi);

        tvThemBV = findViewById(R.id.tvThembaivietmoi);

        mGetData = new getDataApp(this);

        DialogSettings.blur_alpha = 200;
        DialogSettings.use_blur = true;
        DialogSettings.type = DialogSettings.TYPE_IOS;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogSettings.unloadAllDialog();
    }

    private void getData() {
        mlogin = new llogin( new ilogin() {
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
                }
            }



            @Override
            public void code(String code) {

            }
        },this);
        mCTTK = mlogin.getDataNguoiDung();
        if(mCTTK!=null) {
            mlogin.listenerValuesUser();
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


    private void dialogDangXuat(){
        SelectDialog.show(this,
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


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }



    @Override
    public void result_baivietcuatoi(final ArrayList<objbaiviet_app> list) {
        if(list!=null) {
            int size = list.size();
            listLoadmore = new ArrayList<>();
            if(size>10){
                for(int i=0; i<10; i++){
                    listLoadmore.add(list.get(i));
                }
            }else{
                listLoadmore.addAll(list);
            }
            rclvBaiVietCuaToi.refreshComplete();
            listBaiVietFilter = list;
            listBaiVietDefault = list;
            LinearLayoutManager manager = new LinearLayoutManager((Context) this, LinearLayoutManager.VERTICAL, false);
            rclvBaiVietCuaToi.setHasFixedSize(true);
            rclvBaiVietCuaToi.setLayoutManager(manager);
            adapterRclv = new aRclvBaiVietCuaToi(this, listLoadmore);
            rclvBaiVietCuaToi.setAdapter(adapterRclv);

            rclvBaiVietCuaToi.setArrowImageView(R.drawable.iconfont_downgrey);
            rclvBaiVietCuaToi.getDefaultFootView().setLoadingHint(getText(R.string.dangtai).toString());
            rclvBaiVietCuaToi.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
            rclvBaiVietCuaToi.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);

            //LOAD MORE
            final int itemLimit = 10;
            rclvBaiVietCuaToi.setNoMore(false);
            rclvBaiVietCuaToi.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    mLogicAdmin.getlistbaivietcuatoi();
                }

                @Override
                public void onLoadMore() {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            int count = listLoadmore.size();
                            for(int i = 0; i < itemLimit ;i++){
                                if((i+count)<list.size()){
                                    listLoadmore.add(list.get(i+count));
                                }
                                else
                                    break;
                            }

                            if(listLoadmore.size()==list.size()) {
                                rclvBaiVietCuaToi.setNoMore(true);
                                Toast.makeText(AdminActivity.this, getText(R.string.dataixong), Toast.LENGTH_SHORT).show();
                            }
                            adapterRclv.notifyDataSetChanged();
                            rclvBaiVietCuaToi.loadMoreComplete();
                        }
                    }, 1000);
                }
            });


            adapterRclv.setOnItemClickListener(new aRclvBaiVietCuaToi.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    Log.e("kiemtra",position + " ");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("baiviet", listLoadmore.get(position));
                    bundle.putString("loai", "monan");
                    Intent intent = new Intent(AdminActivity.this, ChiTietBaiVietActivity.class);
                    intent.putExtra("data", bundle);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void result_delete(boolean isSuccess) {

    }

    @Override
    public void listbaivietdafilter(final ArrayList<objbaiviet_app> list) {
        if(list!=null) {
            this.listBaiVietFilter = list;
            Log.e("kiemtra",listBaiVietFilter.size()+ " listBaiVietFilter");
            int size = listBaiVietFilter.size();
            listLoadmore = new ArrayList<>();
            if(size>10){
                for(int i=0; i<10; i++){
                    listLoadmore.add(listBaiVietFilter.get(i));
                }
            }else{
                listLoadmore.addAll(listBaiVietFilter);
            }

            LinearLayoutManager manager = new LinearLayoutManager((Context) this, LinearLayoutManager.VERTICAL, false);
            rclvBaiVietCuaToi.setHasFixedSize(true);
            rclvBaiVietCuaToi.setLayoutManager(manager);
            adapterRclv = new aRclvBaiVietCuaToi(this, listLoadmore);
            rclvBaiVietCuaToi.setAdapter(adapterRclv);

            rclvBaiVietCuaToi.setArrowImageView(R.drawable.iconfont_downgrey);
            rclvBaiVietCuaToi.getDefaultFootView().setLoadingHint(getText(R.string.dangtai).toString());
            rclvBaiVietCuaToi.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
            rclvBaiVietCuaToi.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);

            //LOAD MORE
            final int itemLimit = 10;
            rclvBaiVietCuaToi.setNoMore(false);
            rclvBaiVietCuaToi.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    mLogicAdmin.getlistbaivietcuatoi();
                }

                @Override
                public void onLoadMore() {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            int count = listLoadmore.size();
                            for(int i = 0; i < itemLimit ;i++){
                                if((i+count)<listBaiVietFilter.size()){
                                    listLoadmore.add(listBaiVietFilter.get(i+count));
                                }
                                else
                                    break;
                            }
                            if(listLoadmore.size()==listBaiVietFilter.size()) {
                                rclvBaiVietCuaToi.setNoMore(true);
                                Toast.makeText(AdminActivity.this, getText(R.string.dataixong), Toast.LENGTH_SHORT).show();
                            }

                            adapterRclv.notifyDataSetChanged();
                            rclvBaiVietCuaToi.loadMoreComplete();
                        }
                    }, 1000);
                }
            });


            adapterRclv.setOnItemClickListener(new aRclvBaiVietCuaToi.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("baiviet", listLoadmore.get(position));
                    bundle.putString("loai", "monan");
                    Intent intent = new Intent(AdminActivity.this, ChiTietBaiVietActivity.class);
                    intent.putExtra("data", bundle);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.itdangxuat:
                dialogDangXuat();
                break;
            case R.id.itcapnhatthongtin:
                startActivity(new Intent(this,CapNhatTaiKhoanActivity.class));
                break;
            case R.id.itcaidat:
                startActivity(new Intent(this,SettingActivity.class));
                break;
            case R.id.itbaivietdaluu:
                startActivity(new Intent(this,BaiVietDaLuuActivity.class));
                break;
            case R.id.itthemgoiy:
                startActivity(new Intent(this,ThemGoiYActivity.class));
                break;
            case R.id.itthemthanhphan:
                startActivity(new Intent(this,ThemThanhPhanActivity.class));
                break;
             default:
                    startActivity(new Intent(this,DanhSachThanhPhanActivity.class));
                    break;
        }
        drawerLayout.closeDrawers();
        return true;
    }
}
