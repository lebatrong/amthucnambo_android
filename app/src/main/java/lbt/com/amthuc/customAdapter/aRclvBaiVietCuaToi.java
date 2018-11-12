package lbt.com.amthuc.customAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lbt.com.amthuc.Presenters.Account.admin.logicAdmin;
import lbt.com.amthuc.Presenters.Account.iAccount;
import lbt.com.amthuc.Presenters.Account.lAccount;
import lbt.com.amthuc.R;
import lbt.com.amthuc.Views.TaiKhoan.admin.iViewAdmin;
import lbt.com.amthuc.Views.TaiKhoan.chinhsuabaiviet.ChinhSuaBaiVietActivity;
import lbt.com.amthuc.utils.downloadImage;
import lbt.com.amthuc.models.objectClass.app.objbaiviet_app;


public class aRclvBaiVietCuaToi extends RecyclerView.Adapter<aRclvBaiVietCuaToi.ViewHolder> {

    private Context context;
    private ArrayList<objbaiviet_app> mList;
    private downloadImage download;


    public aRclvBaiVietCuaToi(Context context, ArrayList<objbaiviet_app> mList) {
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
        View v = inflater.inflate(R.layout.row_baivietcuatoi,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.imvHinh.setImageResource(R.color.colorErrorImage);
        Picasso.get().load(mList.get(i).getChitiet().getHinh().get(0)).into(viewHolder.imvHinh);



        DecimalFormat format = new DecimalFormat("###,###");
        viewHolder.tvgia.setText(format.format(mList.get(i).getChitiet().getGiathamkhao())+" VNĐ");
        viewHolder.tvTen.setText(mList.get(i).getChitiet().getTen());
        viewHolder.tvdiachi.setText(mList.get(i).getChitiet().getDiachi());


        //Action DELETE
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final logicAdmin mAdmin = new logicAdmin(new iViewAdmin() {
                    @Override
                    public void result_baivietcuatoi(ArrayList<objbaiviet_app> list) {

                    }

                    @Override
                    public void result_delete(boolean isSuccess) {
                        if(isSuccess)
                            Toast.makeText(context, context.getText(R.string.xoathanhcong), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, context.getText(R.string.xoathatbai), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void listbaivietdafilter(ArrayList<objbaiviet_app> listBaiViet) {

                    }
                }, context);

                SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setContentText(context.getText(R.string.banmuonxoabaiviet).toString())
                        .setConfirmText(context.getText(R.string.vangxoano).toString())
                        .setTitleText(context.getText(R.string.xoa).toString())
                        .setCancelText(context.getText(R.string.huy).toString())
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                mAdmin.deletebaiviet(mList.get(i).getIdbaiviet());
                                // reuse previous dialog instance
                                sDialog.dismissWithAnimation();

                            }
                        });
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        //ACTION EDIT
        viewHolder.btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("baiviet", mList.get(i));
                Intent intent = new Intent(context,ChinhSuaBaiVietActivity.class);
                intent.putExtra("data",bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imvHinh;
        TextView tvTen, tvgia, tvdiachi;
        Button btnChinhSua, btnDelete;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            imvHinh = itemView.findViewById(R.id.imvHinhDacSan_mbv);
            tvTen = itemView.findViewById(R.id.tvtenbaiviet_mbv);
            tvgia = itemView.findViewById(R.id.tvgiaban_mbv);
            tvdiachi = itemView.findViewById(R.id.tvdiachi_mbv);

            btnChinhSua = itemView.findViewById(R.id.btnchinhsua_mbv);
            btnDelete = itemView.findViewById(R.id.btnxoa_mbv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Animation animation = AnimationUtils.loadAnimation(context,R.anim.anim_clickrecyclerview);
                    itemView.startAnimation(animation);
                    int position = getLayoutPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(view, position-1);
                }
            });
        }
    }
}