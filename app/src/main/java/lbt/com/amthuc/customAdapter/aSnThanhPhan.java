package lbt.com.amthuc.customAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.firebase.objthanhphan;

public class aSnThanhPhan extends BaseAdapter {

    Context context;
    ArrayList<objthanhphan> listthanhphan;

    public aSnThanhPhan(Context context, ArrayList<objthanhphan> listthanhphan) {
        this.context = context;
        this.listthanhphan = listthanhphan;
    }

    @Override
    public int getCount() {
        return listthanhphan.size();
    }

    @Override
    public Object getItem(int i) {
        return listthanhphan.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.row_thanhphan,null);

        TextView tvTenThanhPhan = view.findViewById(R.id.tvtenthanhphan);

        tvTenThanhPhan.setText(listthanhphan.get(i).getTenthanhphan());


        return view;
    }
}
