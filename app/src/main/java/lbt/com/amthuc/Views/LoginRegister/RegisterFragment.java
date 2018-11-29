package lbt.com.amthuc.Views.LoginRegister;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kongzue.dialog.v2.WaitDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import lbt.com.amthuc.Presenters.LoginRegister.iregister;
import lbt.com.amthuc.Presenters.LoginRegister.lregister;
import lbt.com.amthuc.R;

public class RegisterFragment extends Fragment implements iregister {

    TextInputLayout tilPwd, tilPwdNhapLai, tilHoTen, tilQueQuan,tilEmail;
    RadioButton rdoNam, rdoNu;
    Button btnDangKy, btnNgaySinh;

    lregister mDangKy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        initView(v);
        actionSelectNgaySinh();

        actionDangKy();

        return v;
    }

    private void actionDangKy() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(format.parse(btnNgaySinh.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long ngaysinh = calendar.getTimeInMillis();
                boolean gioitinh = rdoNam.isChecked();
                String email = tilEmail.getEditText().getText().toString();
                String hoten = tilHoTen.getEditText().getText().toString();
                String pwd = tilPwd.getEditText().getText().toString();
                String quequan = tilQueQuan.getEditText().getText().toString();



                if(mDangKy.checkLogic(tilEmail,tilHoTen,tilQueQuan,tilPwd,tilPwdNhapLai)){
                    mDangKy.dangkytaikhoan(email,pwd,hoten,quequan,gioitinh,ngaysinh);
                    WaitDialog.show(getContext(),getText(R.string.dangdangky).toString());
                }else{
                    Toast.makeText(getContext(), getText(R.string.dulieukhonghople), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void actionSelectNgaySinh() {
        btnNgaySinh.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                String []d = btnNgaySinh.getText().toString().split("/");
                int y = Integer.parseInt(d[2]);
                int m = Integer.parseInt(d[1]);
                int da = Integer.parseInt(d[0]);



                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int moth, int day) {
                        btnNgaySinh.setText(day+"/"+(moth+1)+"/"+year);

                    }
                },y,m-1,da);

                dialog.show();


            }
        });
    }

    private void initView(View v) {
        mDangKy = new lregister(this,getContext());


        tilEmail = v.findViewById(R.id.tilemail_res);
        tilHoTen = v.findViewById(R.id.tilhoten_res);
        tilQueQuan = v.findViewById(R.id.tilquequan_res);
        tilPwd = v.findViewById(R.id.tilmatkhau_res);
        tilPwdNhapLai = v.findViewById(R.id.tilnhaplaimatkhau_res);

        rdoNam = v.findViewById(R.id.rdonam_res);
        rdoNu = v.findViewById(R.id.rdonu_res);

        btnDangKy = v.findViewById(R.id.btndangky_res);
        btnNgaySinh = v.findViewById(R.id.btnngaysinh_res);
    }

    @Override
    public void resultDangky(boolean isSuccess) {
        WaitDialog.dismiss();
        if(isSuccess){
            getActivity().finish();
        }else{

            Toast.makeText(getContext(), getText(R.string.dangkythatbai), Toast.LENGTH_SHORT).show();
        }
    }
}
