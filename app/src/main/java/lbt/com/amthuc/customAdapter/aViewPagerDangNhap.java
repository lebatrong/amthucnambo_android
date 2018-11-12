package lbt.com.amthuc.customAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import lbt.com.amthuc.Views.LoginRegister.LoginFragment;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.LoginRegister.RegisterFragment;

public class aViewPagerDangNhap  extends FragmentPagerAdapter {

    Context context;
    public aViewPagerDangNhap(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                LoginFragment login_fragment = new LoginFragment();
                return login_fragment;
            case 1:
                RegisterFragment register_fragment = new RegisterFragment();
                return register_fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getText(R.string.dangnhap);
            case 1:
                return context.getText(R.string.dangky);
        }
        return null;
    }
}
