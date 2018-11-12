package lbt.com.amthuc.customAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import lbt.com.amthuc.Views.Main.AnGiFragment;
import lbt.com.amthuc.Views.Main.UongGiFragment;

public class aViewPagerMenu extends FragmentStatePagerAdapter {
    AnGiFragment frmAnGi;
    UongGiFragment frmUongGi;
    public aViewPagerMenu(FragmentManager fm) {
        super(fm);
        frmAnGi = new AnGiFragment();
        frmUongGi = new UongGiFragment();
    }


    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: return frmAnGi;
            case 1: return frmUongGi;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
