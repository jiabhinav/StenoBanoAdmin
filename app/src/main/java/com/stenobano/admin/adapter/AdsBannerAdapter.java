package com.stenobano.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.stenobano.admin.R;
import com.stenobano.admin.config.BaseUrl;
import com.stenobano.admin.config.Constant;
import com.stenobano.admin.databinding.ListAdsBinding;
import com.stenobano.admin.model.ModelAds;

import java.util.List;

public class AdsBannerAdapter extends RecyclerView.Adapter<AdsBannerAdapter.HolderItem> {
    Context context;
    List<ModelAds.Result> modelList;
    BottomMenu bottomMenu;
    PopupMenu popup;
    ViewDataBinding viewDataBinding;
    //todo 0=home category,1=all category
    View layout;
    public AdsBannerAdapter(Context context, BottomMenu bottomMenu, List<ModelAds.Result> modelList) {
        this.context = context;
        this.bottomMenu=bottomMenu;
        this.modelList=modelList;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.list_ads, parent, false);
        return new HolderItem((ListAdsBinding) viewDataBinding);
    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        ModelAds.Result model=modelList.get(position);
        holder.binding.desc.setText(model.getMessage()+"");
        if (model.getStatus().equals("0"))
        {
            holder.binding.llads.setAlpha(.5f);
        }
        Picasso.with(context).load(BaseUrl.ADS_URL +model.getImage()).fit()
                .placeholder(R.drawable.stenobano_icon)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(holder.binding.postImage);


        holder.binding.llads.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bottomMenu.clickBanner(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class HolderItem extends RecyclerView.ViewHolder {


        ListAdsBinding binding;

        public HolderItem(ListAdsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            Constant.setImageWidthHeight(binding.postImage,100,50);
           // binding.catimage.getLayoutParams().width = Constant.screen_width*12/100;
            //binding.catimage.getLayoutParams().height = Constant.screen_width*12/100;

        }

    }

   public interface BottomMenu
    {
        //todo 1=desable,2=enable,3=delete
        void clickBanner(int position);
    }

}