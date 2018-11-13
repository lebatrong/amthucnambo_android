package lbt.com.amthuc.Views.TaiKhoan.thanhphan;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.InputDialog;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lbt.com.amthuc.Presenters.Account.ThanhPhan.logicThanhPhan;
import lbt.com.amthuc.R;
import lbt.com.amthuc.customAdapter.aRclvThanhPhan;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;
import lbt.com.amthuc.utils.CustomDialogLoading;

import static com.kongzue.dialog.v2.DialogSettings.TYPE_IOS;

public class DanhSachThanhPhanActivity extends AppCompatActivity implements iViewThanhPhan {

    logicThanhPhan mLogic;

    aRclvThanhPhan mAdapter;
    RecyclerView rclvDanhSach;
    NiceSpinner spnThanhPhan_ds;
    CustomDialogLoading mDialog;
    ArrayList<objthanhphan> listThanhPhan;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_thanh_phan);
        initView();
        setupSpinner();
    }

    private void setupSpinner() {
        mDialog.showDialog(getText(R.string.loading).toString());
        mLogic.danhsachthanhphan(true);
        List<String> dataset = new LinkedList<>(Arrays.asList("Món ăn", "Nước uống"));
        spnThanhPhan_ds.attachDataSource(dataset);
        spnThanhPhan_ds.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mDialog.showDialog(getText(R.string.loading).toString());
                if(i==0)
                    mLogic.danhsachthanhphan(true);
                else
                    mLogic.danhsachthanhphan(false);
            }
        });

    }

    private void initView() {
        mLogic = new logicThanhPhan(this);
        spnThanhPhan_ds = findViewById(R.id.nice_spinnerthanhphan);
        rclvDanhSach = findViewById(R.id.rclvThanhPhan_tp);
        mDialog = new CustomDialogLoading(this);

        toolbar = findViewById(R.id.toolbar_dsthanhphan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Setting dialog
        DialogSettings.type = TYPE_IOS;
        DialogSettings.use_blur = true;
        DialogSettings.blur_alpha = 200;
    }

    @Override
    public void result_themthanhphan(boolean isSuccess) {

    }

    @Override
    public void result_danhsachthanhphan(ArrayList<objthanhphan> list) {
        listThanhPhan = new ArrayList<>();
        mDialog.dismissDialog();
        if(list!=null){
            listThanhPhan = list;
            mAdapter = new aRclvThanhPhan(this,list);
            LinearLayoutManager manager = new LinearLayoutManager((Context) this, LinearLayoutManager.VERTICAL, false);
            rclvDanhSach.setHasFixedSize(true);
            rclvDanhSach.setLayoutManager(manager);
            rclvDanhSach.setAdapter(mAdapter);

            mAdapter.setOnClickListener(new aRclvThanhPhan.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, final int position) {

                    InputDialog.show(DanhSachThanhPhanActivity.this,
                            getText(R.string.capnhatlaiten).toString() +" "+listThanhPhan.get(position).getTenthanhphan(),
                            null,
                            getText(R.string.ok).toString(),
                            new InputDialogOkButtonClickListener() {
                        @Override
                        public void onClick(Dialog dialog, String inputText) {
                            boolean isThucAn = true;
                            if(spnThanhPhan_ds.getSelectedIndex()==0)
                                isThucAn = true;
                            else
                                isThucAn = false;
                            mLogic.capnhatthanhphan(listThanhPhan.get(position),inputText, position,isThucAn );
                            dialog.dismiss();
                        }
                    }, getText(R.string.huy).toString(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


                }
            });

        }else {
            Toast.makeText(this, getText(R.string.loitaidulieu), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loitenthanhphan() {
        Toast.makeText(this, getText(R.string.loitenthanhphan), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void CapNhatThanhPhanThanhCong() {
        if(spnThanhPhan_ds.getSelectedIndex()==0)
            mLogic.danhsachthanhphan(true);
        else
            mLogic.danhsachthanhphan(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogSettings.unloadAllDialog();
    }
}
