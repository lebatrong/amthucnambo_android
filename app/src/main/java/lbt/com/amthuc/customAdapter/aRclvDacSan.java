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
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import lbt.com.amthuc.Presenters.Main.ichitietbaiviet;
import lbt.com.amthuc.Presenters.Main.lchitietbaiviet;
import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;
import lbt.com.amthuc.models.objectClass.app.objdanhgia_app;

public class aRclvDacSan extends RecyclerView.Adapter<aRclvDacSan.ViewHolder> {

    private Context context;
    private ArrayList<objbaiviet_app> mList;

    private lchitietbaiviet mChitiet;

    public aRclvDacSan(Context context, ArrayList<objbaiviet_app> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setmList(ArrayList<objbaiviet_app> mList) {
        this.mList = mList;
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_monan,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        //GÁN IMAGE CHO VIEWFLIPPER
        for (String urlHinh : mList.get(i).getChitiet().getHinh()) {
            ImageView imv = new ImageView(context);
            Picasso.get().load(urlHinh).into(imv);
            imv.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.vfpHinh.addView(imv);
        }

        Random random = new Random();
        int time = random.nextInt(2000+1)+3500;

        viewHolder.vfpHinh.setFlipInterval(time);
        viewHolder.vfpHinh.setAutoStart(true);
        Animation animation_in = AnimationUtils.loadAnimation(context,R.anim.amin_in_viewflipper);
        Animation animation_out = AnimationUtils.loadAnimation(context,R.anim.amin_out_viewflipper);
        viewHolder.vfpHinh.setInAnimation(animation_in);
        viewHolder.vfpHinh.setOutAnimation(animation_out);

        DecimalFormat format = new DecimalFormat("###,###");
        viewHolder.tvgia.setText(format.format(mList.get(i).getChitiet().getGiathamkhao())+" VNĐ");
        viewHolder.tvTen.setText(mList.get(i).getChitiet().getTen());
        viewHolder.tvdiachi.setText(mList.get(i).getChitiet().getDiachi());

        mChitiet = new lchitietbaiviet(new ichitietbaiviet() {
            @Override
            public void loitaidulieu_chitietbaiviet() {

            }

            @Override
            public void danhsachbinhluan(ArrayList<objdanhgia_app> list) {

            }

            @Override
            public void danhsachbaivietgoiy(ArrayList<objbaiviet_app> list, boolean cobaivietgoiy) {

            }

            @Override
            public void chuacodanhgia() {

            }

            @Override
            public void thongkedanhgia(float tongsao, float tongsaodanhgia, String rate) {
                viewHolder.tvsosao.setText(rate);
                viewHolder.tvthongkesosao.setText("("+(int)tongsaodanhgia+"/"+(int)tongsao+")");
            }

            @Override
            public void baivietdaluu() {

            }

            @Override
            public void baivietchualuu() {

            }

            @Override
            public void results_luubaiviet(boolean isSuccess) {

            }

            @Override
            public void results_xoabaiviet(boolean isSuccess) {

            }

            @Override
            public void results_danhgiabaiviet(boolean isSuccess) {

            }
        }, context);
        mChitiet.getdanhgia(mList.get(i).getIdbaiviet(),true);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewFlipper vfpHinh;
        TextView tvTen,tvsosao,tvthongkesosao, tvgia, tvdiachi;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            vfpHinh = itemView.findViewById(R.id.vfpHinhDacSan_row);
            tvTen = itemView.findViewById(R.id.tvtenbaiviet_row);
            tvgia = itemView.findViewById(R.id.tvgiaban_row);
            tvthongkesosao = itemView.findViewById(R.id.tvthongkesosao_row);
            tvsosao = itemView.findViewById(R.id.tvsosao_row);
            tvdiachi = itemView.findViewById(R.id.tvdiachi_row);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Animation animation = AnimationUtils.loadAnimation(context,R.anim.anim_clickrecyclerview);
                    itemView.startAnimation(animation);
                    int position = getLayoutPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(view, position);
                }
            });
        }
    }
}
