package lbt.com.amthuc.Views.LoginRegister;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import lbt.com.amthuc.R;
import lbt.com.amthuc.customAdapter.aViewPagerDangNhap;

public class MainLoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;

    private aViewPagerDangNhap viewPagerDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        initView();

        actionToolbar();
        actionViewPager();
    }

    private void actionViewPager() {
        viewPagerDangNhap = new aViewPagerDangNhap(getSupportFragmentManager(),this);
        viewPager.setAdapter(viewPagerDangNhap);
        viewPagerDangNhap.notifyDataSetChanged();

        //SET VIEW PAGER
        tabLayout.setupWithViewPager(viewPager);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tablayoutlogin);
        toolbar = (Toolbar) findViewById(R.id.toolbarlogin);
        viewPager = (ViewPager) findViewById(R.id.viewpagerLogin);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
