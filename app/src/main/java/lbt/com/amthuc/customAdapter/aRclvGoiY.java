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
import lbt.com.amthuc.models.objectClass.app.objgoiychitiet_app;

public class aRclvGoiY extends RecyclerView.Adapter<aRclvGoiY.ViewHolder> {

    private Context context;
    private ArrayList<objgoiychitiet_app> mList;

    public void setmList(ArrayList<objgoiychitiet_app> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public aRclvGoiY(Context context, ArrayList<objgoiychitiet_app> mList) {
        this.context = context;
        this.mList = mList;
        }

    private OnItemClickListener listener;

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_goiy,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvTen.setText(mList.get(i).getTenthanhphan());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView tvTen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTen = itemView.findViewById(R.id.tvtengoiy_rowgoiy);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(view, position);
                }
            });
        }


    }
}