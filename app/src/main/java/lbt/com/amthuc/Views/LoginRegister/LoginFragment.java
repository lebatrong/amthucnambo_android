package lbt.com.amthuc.Views.LoginRegister;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.kongzue.dialog.v2.WaitDialog;

import lbt.com.amthuc.Presenters.LoginRegister.ilogin;
import lbt.com.amthuc.Presenters.LoginRegister.llogin;
import lbt.com.amthuc.R;
import lbt.com.amthuc.utils.CountryData;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;



public class LoginFragment extends Fragment implements ilogin{

    Button btnLogin,btnverry,btnloginsdt, btnLoginWithAccount,btnLoginWithPhone, btnguilai;
    TextInputLayout tilEmail,tilPwd, tilsdt,tilcode;

    LinearLayout lnlVerryCode,lnlLoginAccount,lnlLoginPhone, lnlnhapsdt;

    private Spinner spinner;

    String codeSent;

    TextView tvCodePhone;

    llogin mLogin;

    FirebaseAuth mAuth;
    CountDownTimer cdt;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        initView(v);

        actionLogin();
        actionloginsdt();
        setupSpinner();
        actionLoginPhoneOrAccount();
        return v;
    }

    private void actionLoginPhoneOrAccount() {
        btnLoginWithAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnlLoginAccount.setVisibility(View.VISIBLE);
                lnlLoginPhone.setVisibility(View.GONE);
            }
        });

        btnLoginWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnlLoginAccount.setVisibility(View.GONE);
                lnlLoginPhone.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupSpinner() {
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String code = CountryData.countryAreaCodes[i];
                tvCodePhone.setText("+"+code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void actionloginsdt() {
        btnloginsdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
            }
        });



        btnguilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
            }
        });

        btnverry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!codeSent.matches("")) {
                    if (!tilcode.getEditText().getText().toString().isEmpty()) {
                        if (tilcode.getEditText().getText().toString().length() == 6) {
                            tilcode.setErrorEnabled(false);
                            WaitDialog.show(getContext(), "Loading");
                            VerryCode();
                        } else {
                            tilcode.setFocusable(true);
                            tilcode.setErrorEnabled(true);
                            tilcode.setError(getText(R.string.codekhonghople));
                        }
                    } else {
                        tilcode.setFocusable(true);
                        tilcode.setErrorEnabled(true);
                        tilcode.setError(getText(R.string.codekhongduoctrong));
                    }
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cdt.cancel();
    }

    private void getCode(){
        String code = tvCodePhone.getText().toString();
        String number = tilsdt.getEditText().getText().toString().trim();
        if (number.isEmpty()) {
            tilsdt.setError(getText(R.string.sodienthoaikhonghople));
            tilsdt.requestFocus();
        }else {
            WaitDialog.show(getContext(), "Loading");
            tilsdt.setErrorEnabled(false);
            tilsdt.setError("");
            String phoneNumber = "+" + code + number;
            mLogin.getCodeWithPhone(phoneNumber);

            delay60second();
            lnlVerryCode.setVisibility(View.VISIBLE);
            lnlnhapsdt.setVisibility(View.GONE);
        }
    }

    private void delay60second(){
        btnguilai.setEnabled(false);
        cdt.start();
    }




    private void actionLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = tilEmail.getEditText().getText().toString();
                String pwd = tilPwd.getEditText().getText().toString();

                if(mLogin.checkLogic(tilEmail,tilPwd)){
                    WaitDialog.show(getContext(),getText(R.string.dangdangnhap).toString());
                    tilEmail.setEnabled(false);
                    tilPwd.setEnabled(false);
                    mLogin.loginWithEmailPassword(email,pwd);
                }else{
                    Toast.makeText(getContext(), getText(R.string.dulieukhonghople), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void VerryCode(){
        String myCode = tilcode.getEditText().getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,myCode);
        mLogin.signInWithCredential(credential);
    }





    private void initView(View v) {
        btnLogin = v.findViewById(R.id.btnlogin_login);
        btnguilai = v.findViewById(R.id.btnguilaima_login);
        btnloginsdt = v.findViewById(R.id.btnloginsdt_login);
        btnLoginWithAccount = v.findViewById(R.id.btnLoginWithAccount);
        btnLoginWithPhone = v.findViewById(R.id.btnLoginWithPhone);
        btnverry = v.findViewById(R.id.btnloginsdtverycode_login);
        tilEmail = v.findViewById(R.id.tilemail_login);
        tilPwd = v.findViewById(R.id.tilpwd_login);
        tilcode = v.findViewById(R.id.tilcode_login);
        tilsdt = v.findViewById(R.id.tilsdt_login);
        spinner = v.findViewById(R.id.spinnerCountries);
        lnlVerryCode = v.findViewById(R.id.lnlverrycode);
        lnlLoginAccount = v.findViewById(R.id.lnldangnhapemail);
        lnlLoginPhone = v.findViewById(R.id.lnldangnhapsdt);
        lnlnhapsdt = v.findViewById(R.id.lnlnhapsdt);
        tvCodePhone = v.findViewById(R.id.tvcodePhone);



        mLogin = new llogin(this,getContext());
        mAuth = FirebaseAuth.getInstance();

        cdt = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long l) {
                btnguilai.setText(getText(R.string.guilai) + "(" + (l/1000) + ")");
            }

            @Override
            public void onFinish() {
                btnguilai.setText(getText(R.string.guilai));
                btnguilai.setEnabled(true);
            }
        };


    }

    @Override
    public void ResultLogin(boolean isSuccess) {
        tilEmail.setEnabled(true);
        tilPwd.setEnabled(true);
        WaitDialog.dismiss();
        if(isSuccess){
            getActivity().finish();
        }else
            Toast.makeText(getContext(), getText(R.string.dangnhapthatbai), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void result_listenner_values_user(objnguoidung_app muser) {

    }




    @Override
    public void code(String code) {
        WaitDialog.dismiss();
        codeSent = code;
    }


}
