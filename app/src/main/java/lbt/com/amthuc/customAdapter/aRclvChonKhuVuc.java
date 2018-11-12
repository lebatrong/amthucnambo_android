package lbt.com.amthuc.customAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.app.objkhuvuc_app;

public class aRclvChonKhuVuc extends RecyclerView.Adapter<aRclvChonKhuVuc.ViewHolder> {

    private Context context;
    private ArrayList<objkhuvuc_app> mList;

    public aRclvChonKhuVuc(Context context, ArrayList<objkhuvuc_app> mList) {
        this.context = context;
        this.mList = mList;
    }

    public static OnClickListener listener;

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    public interface OnClickListener{
        void OnClick(View itemView, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_chonkhuvuc,viewGroup,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvtenkhuvuc.setText(mList.get(i).getChitietkhuvuc().getTentinh());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvtenkhuvuc;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvtenkhuvuc = itemView.findViewById(R.id.tvtentinh_row);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                        listener.OnClick(itemView,getAdapterPosition());
                }
            });
        }
    }
}
