package lbt.com.amthuc.customAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lbt.com.amthuc.R;
import lbt.com.amthuc.utils.downloadImage;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;

public class aRclvChung extends RecyclerView.Adapter<aRclvChung.ViewHolder> {

    private Context context;
    private ArrayList<objbaiviet_app> mList;
    private downloadImage download;

    public aRclvChung(Context context, ArrayList<objbaiviet_app> mList) {
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
        View v = inflater.inflate(R.layout.row_chung,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picasso.get().load(mList.get(i).getChitiet().getHinh().get(0)).into(viewHolder.imvChung);
        viewHolder.tvTen.setText(mList.get(i).getChitiet().getTen());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView imvChung;
        TextView tvTen;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            imvChung = itemView.findViewById(R.id.imvHinhMonAnChung);
            tvTen = itemView.findViewById(R.id.tvTenMonAnChung);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation animation = AnimationUtils.loadAnimation(context,R.anim.anim_clickrecyclerview);
                    view.startAnimation(animation);
                    int position = getLayoutPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(view, position);
                }
            });
        }


    }
}
