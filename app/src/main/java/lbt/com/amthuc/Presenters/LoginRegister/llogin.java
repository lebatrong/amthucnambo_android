package lbt.com.amthuc.Presenters.LoginRegister;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.app.objnguoidung_app;
import lbt.com.amthuc.models.objectClass.firebase.objnguoidungs;

public class llogin {
    private String TAG = "kiemtra_llogin";
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    ilogin mInterface;
    Context context;

    public llogin(ilogin mInterface, Context context) {
        this.mInterface = mInterface;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void loginWithEmailPassword(String email, final String pwd){
        mAuth.signInWithEmailAndPassword(email,pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                DatabaseReference mRef = mDatabase.getReference().child("nguoidungs").child(authResult.getUser().getUid());
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        objnguoidungs obj = dataSnapshot.getValue(objnguoidungs.class);
                        objnguoidung_app muser = new objnguoidung_app();
                        muser.setNguoidung(obj);
                        muser.setPassword(pwd);
                        downloadAvatar(muser, false);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG,databaseError.toString());
                        mInterface.ResultLogin(false);
                        mAuth.signOut();
                        SharedPreferences spf = context.getSharedPreferences("dataUser",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = spf.edit();
                        editor.clear();
                        editor.commit();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,e.toString());
                mInterface.ResultLogin(false);

            }
        });
    }

    public void listenerValuesUser(){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            DatabaseReference mRef = mDatabase.getReference()
                    .child("nguoidungs")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    objnguoidungs obj = dataSnapshot.getValue(objnguoidungs.class);
                    objnguoidung_app muser = getDataNguoiDung();
                    muser.setNguoidung(obj);
                    downloadAvatar(muser, true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, databaseError.toString());
                    mAuth.signOut();
                    SharedPreferences spf = context.getSharedPreferences("dataUser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.clear();
                    editor.commit();
                }
            });
        }
    }

    public void downloadAvatar(final objnguoidung_app muser, final boolean isListenner){
        if(!muser.getNguoidung().getAvatar().matches("")) {
            try {
                StorageReference sRef = FirebaseStorage
                        .getInstance("gs://thucannambo.appspot.com")
                        .getReference().child(muser.getNguoidung().getAvatar());
                final long ONE_MEGABYTE = 1024 * 1024;
                sRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        muser.setAnhdaidien(Base64.encodeToString(bytes, Base64.DEFAULT));
                        saveDataUser(muser, isListenner);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        muser.setAnhdaidien("");
                        saveDataUser(muser, isListenner);
                        Log.e(TAG, e.toString());
                    }
                });
            } catch (Exception e) {
                muser.setAnhdaidien("");
                saveDataUser(muser, isListenner);
                Log.e(TAG, e.toString());
            }
        }else {
            muser.setAnhdaidien("");
            saveDataUser(muser, isListenner);
        }
    }

    public void getCodeWithPhone(String phoneNumber){
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mInterface.ResultLogin(false);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mInterface.code(s);
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    public void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final DatabaseReference mRef = mDatabase.getReference().child("nguoidungs")
                                    .child(task.getResult().getUser().getUid());

                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //Có người dùng.
                                    if(dataSnapshot.getValue()!=null){
                                        objnguoidungs obj = dataSnapshot.getValue(objnguoidungs.class);
                                        objnguoidung_app muser = new objnguoidung_app();
                                        muser.setNguoidung(obj);
                                        muser.setPassword("");
                                        downloadAvatar(muser, false);
                                    }else { // Chưa có người dùng
                                        final objnguoidung_app nguoidung = new objnguoidung_app();
                                        objnguoidungs mUser = new objnguoidungs();
                                        mUser.setAvatar("");
                                        mUser.setEmail("");
                                        mUser.setGioitinh(true);
                                        mUser.setNgaysinh(Long.parseLong("957718800000"));
                                        mUser.setQuanly(false);
                                        mUser.setQuequan("");
                                        mUser.setHoten(task.getResult().getUser().getUid());
                                        mUser.setLoaidangnhap("sdt");
                                        nguoidung.setNguoidung(mUser);
                                        nguoidung.setPassword("");
                                        nguoidung.setAnhdaidien("");
                                        mRef.setValue(mUser)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        saveDataUser(nguoidung,false);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, e.toString());
                                                mInterface.ResultLogin(false);
                                                mAuth.signOut();
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    mAuth.signOut();
                                    mInterface.ResultLogin(false);
                                }
                            });
                        } else {
                            mInterface.ResultLogin(false);
                        }
                    }
                });
    }



    private void saveDataUser(objnguoidung_app user, boolean isListenner){
        SharedPreferences spf = context.getSharedPreferences("dataUser",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.clear();
        Gson gson = new Gson();
        editor.putString("user",gson.toJson(user));
        Log.e("kiemtra","save: "+gson.toJson(user));
        editor.commit();
        if(!isListenner)
            mInterface.ResultLogin(true);
        else
            mInterface.result_listenner_values_user(user);
    }

    public objnguoidung_app getDataNguoiDung(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences("dataUser",Context.MODE_PRIVATE);
        String strUser = sharedPreferences.getString("user","");
        if(strUser.matches("")) {
            FirebaseAuth.getInstance().signOut();
            return null;
        }
        else{
            return gson.fromJson(strUser,objnguoidung_app.class);
        }
    }

    public Drawable getImage(String bytes_imv){
        Drawable drawable = null;
        if(!bytes_imv.isEmpty()){
            byte[] bytes = Base64.decode(bytes_imv, Base64.DEFAULT);
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            drawable = new BitmapDrawable(Resources.getSystem(), bm);
        }
        return drawable;
    }

    public boolean checkLogic(TextInputLayout tilEmail, TextInputLayout tilPwd){
        String email = tilEmail.getEditText().getText().toString();
        String pwd = tilPwd.getEditText().getText().toString();

        if(!email.isEmpty() && !pwd.isEmpty()){
            tilEmail.setErrorEnabled(false);
            tilPwd.setErrorEnabled(false);
            tilEmail.setError("");
            tilPwd.setError("");
            return true;
        }else{
            tilEmail.setErrorEnabled(false);
            tilPwd.setErrorEnabled(false);
            if(email.isEmpty()){
                tilEmail.setErrorEnabled(true);
                tilEmail.setError(context.getText(R.string.emailkhongtrong));
            }

            if(pwd.isEmpty()){
                tilPwd.setErrorEnabled(true);
                tilPwd.setError(context.getText(R.string.matkhaukhongtrong));
            }

            return false;
        }
    }
}
