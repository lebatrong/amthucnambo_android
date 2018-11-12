package lbt.com.amthuc.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import lbt.com.amthuc.R;

public class CustomDialogLoading {
    private Dialog progressDialog;
    private Context context;

    public CustomDialogLoading(Context context) {
        this.context = context;
        this.progressDialog = new Dialog(context,R.style.progress_dialog);
    }

    public void showDialog(String mess){
        if(progressDialog!=null) {
            progressDialog.setContentView(R.layout.custom_dialog_layout);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText(mess);
            progressDialog.show();
        }else {
            progressDialog = new Dialog(context,R.style.progress_dialog);
            progressDialog.setContentView(R.layout.custom_dialog_layout);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText(mess);
            progressDialog.show();
        }
    }

    public void dismissDialog(){
        if(progressDialog!=null)
            progressDialog.dismiss();
    }
}
