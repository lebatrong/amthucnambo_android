package lbt.com.amthuc.Presenters.FplashScreens;


import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;

public interface igetdataapp {
    void loadkhuvuc(boolean isSuccess);
    void saveMyData(boolean isSuccess, objkhuvuc_app mdata);
    void loitaidulieu_getDataApp();
    void taidulieuthanhcong(boolean isRealtime);
}
