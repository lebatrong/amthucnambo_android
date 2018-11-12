package lbt.com.amthuc.Views.Main;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import lbt.com.amthuc.Presenters.LoginRegister.ilogin;
import lbt.com.amthuc.Presenters.LoginRegister.llogin;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.LoginRegister.MainLoginActivity;
import lbt.com.amthuc.Views.TaiKhoan.admin.AdminActivity;
import lbt.com.amthuc.Views.TaiKhoan.TaiKhoanMainActivity;
import lbt.com.amthuc.customAdapter.aViewPagerMenu;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;

public class MainActivity extends AppCompatActivity implements ilogin {

    aViewPagerMenu adapter;
    ViewPager vpMain;
    RadioButton rdoangi,rdouonggi;
    ImageView imvSearch,imvLogin,imvUser;

    llogin mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        phanquyen();
    }



    private void phanquyen() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            imvLogin.setVisibility(View.GONE);
            imvUser.setVisibility(View.VISIBLE);
            if(mLogin.getImage(mLogin.getDataNguoiDung().getAnhdaidien())!=null)
                imvUser.setImageDrawable(mLogin.getImage(mLogin.getDataNguoiDung().getAnhdaidien()));
            else
                imvUser.setImageResource(R.drawable.defaultuser);
            imvUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!mLogin.getDataNguoiDung().getNguoidung().isQuanly())
                        startActivity(new Intent(MainActivity.this,TaiKhoanMainActivity.class));
                    else
                        startActivity(new Intent(MainActivity.this,AdminActivity.class));
                }
            });
        }else{
            imvLogin.setVisibility(View.VISIBLE);
            imvUser.setVisibility(View.GONE);
            imvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,MainLoginActivity.class));
                }
            });
        }
    }


    private void initView() {
        mLogin = new llogin(this,this);

        imvLogin = findViewById(R.id.imvLogin);
        imvUser = findViewById(R.id.imvUser);





        rdoangi = findViewById(R.id.rdoangi);
        rdouonggi = findViewById(R.id.rdouonggi);

        rdoangi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    vpMain.setCurrentItem(0);
                }else
                    vpMain.setCurrentItem(1);
            }
        });

        this.vpMain = findViewById(R.id.vpMain);
        adapter = new aViewPagerMenu(getSupportFragmentManager());
        vpMain.setAdapter(adapter);

        vpMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        rdoangi.setChecked(true);
                        break;
                    case 1:
                        rdouonggi.setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        imvSearch = findViewById(R.id.imvSearch);
        imvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });
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
}
