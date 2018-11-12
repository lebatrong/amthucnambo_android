package lbt.com.amthuc.customAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import lbt.com.amthuc.R;
import lbt.com.amthuc.models.objectClass.app.objdanhgia_app;

public class aRclvBinhLuan extends RecyclerView.Adapter<aRclvBinhLuan.ViewHolder> {

    private Context context;
    private ArrayList<objdanhgia_app> mList;

    private OnItemClickListener listener;


    public aRclvBinhLuan(Context context, ArrayList<objdanhgia_app> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_binhluan,null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String sosao = context.getText(R.string.rate1).toString();


        switch((int)mList.get(i).getChitietdanhgia().getSosao()){
            case 1:
                sosao = context.getText(R.string.rate1).toString();
                break;
            case 2:
                sosao = context.getText(R.string.rate2).toString();
                break;
            case 3:
                sosao = context.getText(R.string.rate3).toString();
                break;
            case 4:
                sosao = context.getText(R.string.rate4).toString();
                break;
            case 5:
                sosao = context.getText(R.string.rate5).toString();
                break;
        }

        viewHolder.tvsosao.setText(sosao);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mList.get(i).getChitietdanhgia().getNgaydanhgia());
        viewHolder.tvngay.setText("("+dateFormat.format(calendar.getTime())+") ");

        viewHolder.tvbinhluan.setText(mList.get(i).getChitietdanhgia().getBinhluan());
        viewHolder.tvnguoibinhluan.setText(mList.get(i).getChitietdanhgia().getTennguoidanhgia());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvbinhluan, tvngay, tvsosao, tvnguoibinhluan;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvbinhluan = itemView.findViewById(R.id.tvbinhluan_row);
            tvngay = itemView.findViewById(R.id.tvngaybinhluan_row);
            tvsosao = itemView.findViewById(R.id.tvsosaobinhluan_row);
            tvnguoibinhluan = itemView.findViewById(R.id.tvnguoidanhgia_row);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        listener.onItemClick(view,getLayoutPosition());
                    }
                }
            });
        }
    }

}
