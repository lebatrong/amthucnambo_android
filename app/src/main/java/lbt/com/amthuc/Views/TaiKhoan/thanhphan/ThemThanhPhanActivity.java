package lbt.com.amthuc.Views.TaiKhoan.thanhphan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lbt.com.amthuc.Presenters.Account.ThanhPhan.logicThanhPhan;
import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;
import lbt.com.amthuc.utils.CustomDialogLoading;

public class ThemThanhPhanActivity extends AppCompatActivity implements iViewThanhPhan {

    Toolbar toolbar;

    Spinner spnSoLuong;

    LinearLayout lnlMain;

    Button btnThem;

    logicThanhPhan mLogic;

    ArrayList<EditText> arrEditText;

    RadioButton rdoThucAn;

    CustomDialogLoading mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_thanh_phan);

        initView();

        setupSpiner();

        themthanhphan();

    }

    private void themthanhphan() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> listTen = new ArrayList<>();
                for (EditText edt : arrEditText){
                    listTen.add(edt.getText().toString());
                }

                mLogic.themthanhphan(listTen,rdoThucAn.isChecked());
                mDialog.showDialog(getText(R.string.loading).toString());
            }
        });
    }

    private void setupSpiner() {

        final ArrayList<Integer> arrSL = new ArrayList();
        for (int i=0; i<5; i++){
            arrSL.add(i+1);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, arrSL);

        spnSoLuong.setAdapter(adapter);
        spnSoLuong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                addView(arrSL.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void addView(int soluong){
        arrEditText = new ArrayList<>();
        lnlMain.removeAllViews();
        for (int i = 0; i < soluong; i++) {
            EditText edttentp = new EditText(this);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            edttentp.setLayoutParams(p);
            edttentp.setInputType(InputType.TYPE_CLASS_TEXT);
            edttentp.setBackgroundColor(getResources().getColor(R.color.transparent));
            edttentp.setPadding(15,25,5,25);
            edttentp.setHint(getText(R.string.nhaptenthanhphan)+ " " + (i + 1));
            edttentp.setTextSize(17.0f);
            edttentp.setTextColor(getResources().getColor(R.color.colorTextMain));

            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            v.setBackgroundColor(getResources().getColor(R.color.colorLine));
            v.setPadding(0,5,0,5);


            TextView tvten = new TextView(this);
            tvten.setText(getText(R.string.tenthanhphan) + " " + (i + 1) + " :");
            tvten.setLayoutParams(p);
            tvten.setPadding(0,5,0,0);
            tvten.setTextSize(18.0f);
            tvten.setTextColor(getResources().getColor(R.color.colorBlack));

            lnlMain.addView(tvten);
            lnlMain.addView(edttentp);
            lnlMain.addView(v);
            arrEditText.add(edttentp);
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar_themthanhphan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spnSoLuong = findViewById(R.id.spnsoluong_themtp);
        lnlMain = findViewById(R.id.lnlMain_themtp);
        mLogic = new logicThanhPhan(this);

        btnThem = findViewById(R.id.btnthemthanhphan);
        rdoThucAn = findViewById(R.id.rdoThucAn_ThemTP);

        mDialog = new CustomDialogLoading(this);
    }

    @Override
    public void result_themthanhphan(boolean isSuccess) {
        mDialog.dismissDialog();
        setupSpiner();
        if(isSuccess){
            Toast.makeText(this, getText(R.string.themthanhphanthanhcong), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, getText(R.string.themthanhphanthatbai), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void result_danhsachthanhphan(ArrayList<objthanhphan> list) {

    }

    @Override
    public void loitenthanhphan() {

    }

    @Override
    public void CapNhatThanhPhanThanhCong() {

    }
}
