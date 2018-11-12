package lbt.com.amthuc.customAdapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.transformer.DepthPageTransformer;
import com.view.jameson.library.CardAdapterHelper;

import java.util.ArrayList;
import java.util.List;

import lbt.com.amthuc.R;

public class aRclvHinhChiTiet extends RecyclerView.Adapter<aRclvHinhChiTiet.ViewHolder> {

    private List<String> mList;
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private Activity activity;

    public aRclvHinhChiTiet(List<String> mList, Activity activity) {
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hinhchitiet, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Picasso.get().load(mList.get(position)).into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView_hinhchitiet);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setEnabled(false);
                    ArrayList<AdInfo> advList = new ArrayList<>();
                    for(String position: mList){
                        AdInfo adInfo = new AdInfo();
                        adInfo.setActivityImg(position);
                        advList.add(adInfo);
                    }

                    AdManager adManager = new AdManager(activity, advList);
                    adManager.setOverScreen(true)
                            .setOnCloseClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    itemView.setEnabled(true);
                                }
                            })
                            .setPageTransformer(new DepthPageTransformer());

                    adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);
                }
            });
        }

    }

}
