package lbt.com.amthuc.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import lbt.com.amthuc.R;

@SuppressLint("AppCompatCustomView")
public class CustomEditTextPassword extends EditText {

    Drawable an, hien,drawable;
    boolean visible = false;
    boolean userStrike = false;

    public CustomEditTextPassword(Context context) {
        super(context);
        khoitao(null);
    }

    public CustomEditTextPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        khoitao(attrs);
    }

    public CustomEditTextPassword(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        khoitao(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomEditTextPassword(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        khoitao(attrs);
    }

    private void khoitao(AttributeSet attr){
        if(attr !=null){
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attr,R.styleable.EditTextPassword,0,0);
            this.userStrike = typedArray.getBoolean(R.styleable.EditTextPassword_userStrike,false);
        }

        an = ContextCompat.getDrawable(getContext(), R.drawable.eyehi);
        hien = ContextCompat.getDrawable(getContext(), R.drawable.eyevi);
        caidat();
    }

    private void caidat() {
        setInputType(InputType.TYPE_CLASS_TEXT | (visible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
        Drawable []dr = getCompoundDrawables();
        drawable = (!userStrike && !visible) ? an : hien;

        setCompoundDrawablesWithIntrinsicBounds(dr[0],dr[1],drawable,dr[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP && event.getX() >= (getRight() - drawable.getBounds().width()))
        {
            visible = ! visible;
            caidat();
            invalidate();
        }
        return super.onTouchEvent(event);
    }
}


