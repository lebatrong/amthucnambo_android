package lbt.com.amthuc.Presenters.LoginRegister;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;
import lbt.com.amthuc.models.objectClass.firebase.objnguoidungs;

public class lregister {

    public static String TAG = "kiemtra_lregister";

    private iregister mInterface;
    private Context context;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;

    private objnguoidung_app nguoidung;

    public lregister(iregister mInterface, Context context) {
        this.mInterface = mInterface;
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean checkMatKhau(TextInputLayout matkhau, TextInputLayout nhaplaimatkhau){
        String strmatkhau = matkhau.getEditText().getText().toString();
        String strnhaplaimatkhau = nhaplaimatkhau.getEditText().getText().toString();

        if(!strmatkhau.isEmpty() && !strnhaplaimatkhau.isEmpty()){
            if(!strmatkhau.matches(strnhaplaimatkhau)){
                matkhau.setErrorEnabled(true);
                nhaplaimatkhau.setErrorEnabled(true);
                matkhau.setError(context.getText(R.string.matkhaukhongkhop));
                nhaplaimatkhau.setError(context.getText(R.string.matkhaukhongkhop));
                return false;
            }else{
                matkhau.setErrorEnabled(false);
                nhaplaimatkhau.setErrorEnabled(false);
                matkhau.setError("");
                nhaplaimatkhau.setError("");
                return true;
            }
        }else{
            matkhau.setErrorEnabled(false);
            nhaplaimatkhau.setErrorEnabled(false);
            if(strmatkhau.isEmpty()){
                matkhau.setErrorEnabled(true);
                matkhau.setError(context.getText(R.string.matkhaukhongtrong));
            }

            if(strnhaplaimatkhau.isEmpty()){
                nhaplaimatkhau.setErrorEnabled(true);
                nhaplaimatkhau.setError(context.getText(R.string.nhaplaimatkhaukhongtrong));
            }

            return false;
        }
    }

    public boolean checkLogic(TextInputLayout email, TextInputLayout hoten, TextInputLayout quequan,TextInputLayout matkhau, TextInputLayout nhaplaimatkhau ){

        if(checkMatKhau(matkhau,nhaplaimatkhau) &&
                !email.getEditText().getText().toString().isEmpty()&&
                !hoten.getEditText().getText().toString().isEmpty() &&
                !quequan.getEditText().getText().toString().isEmpty()){

            email.setErrorEnabled(false);
            hoten.setErrorEnabled(false);
            quequan.setErrorEnabled(false);


            return true;

        }else{
            email.setErrorEnabled(false);
            hoten.setErrorEnabled(false);
            quequan.setErrorEnabled(false);

            if(email.getEditText().getText().toString().isEmpty()){
                email.setErrorEnabled(true);
                email.setError(context.getText(R.string.emailkhongtrong));
            }

            if(hoten.getEditText().getText().toString().isEmpty()){
                hoten.setErrorEnabled(true);
                hoten.setError(context.getText(R.string.hotenkhongtrong));
            }

            if(quequan.getEditText().getText().toString().isEmpty()){
                quequan.setErrorEnabled(true);
                quequan.setError(context.getText(R.string.quequankhongtrong));
            }

            return false;
        }
    }

    public void dangkytaikhoan(final String email, final String pwd, final String hoten, final String quequan, final boolean gioitinh, final long ngaysinh){
        mAuth.createUserWithEmailAndPassword(email,pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(final AuthResult authResult) {

                nguoidung = new objnguoidung_app();
                objnguoidungs mUser = new objnguoidungs();
                mUser.setAvatar("");
                mUser.setEmail(email);
                mUser.setGioitinh(gioitinh);
                mUser.setNgaysinh(ngaysinh);
                mUser.setQuanly(false);
                mUser.setQuequan(quequan);
                mUser.setHoten(hoten);
                mUser.setLoaidangnhap("email");
                nguoidung.setNguoidung(mUser);
                nguoidung.setPassword(pwd);
                nguoidung.setAnhdaidien("");
                DatabaseReference mRef = mDatabase.getReference().child("nguoidungs")
                        .child(authResult.getUser().getUid());

                mRef.setValue(mUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                       saveDataUser(nguoidung);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, e.toString());
                                mInterface.resultDangky(false);
                            }
                        });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
                mInterface.resultDangky(false);
            }
        });
    }

    private void saveDataUser(objnguoidung_app user){
        SharedPreferences spf = context.getSharedPreferences("dataUser",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.clear();
        Gson gson = new Gson();
        editor.putString("user",gson.toJson(user));
        editor.commit();
        mInterface.resultDangky(true);
    }
}
