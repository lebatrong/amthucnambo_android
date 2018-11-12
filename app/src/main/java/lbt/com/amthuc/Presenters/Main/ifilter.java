package lbt.com.amthuc.Presenters.Main;

import java.util.ArrayList;

import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;

public interface ifilter {
    void loitaidulieu_filter();
    void ketquatimkiem(ArrayList<objbaiviet_app> baiviet, boolean coketqua);
}
