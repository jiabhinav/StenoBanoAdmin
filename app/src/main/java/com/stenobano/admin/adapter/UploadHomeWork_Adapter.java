package com.stenobano.admin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import com.stenobano.admin.R;

import java.io.IOException;
import java.util.ArrayList;

public class UploadHomeWork_Adapter extends RecyclerView.Adapter<UploadHomeWork_Adapter.HolderView> {
    Context context;
    private ArrayList<Uri> dataList;


    public UploadHomeWork_Adapter(Context context, ArrayList<Uri> dataList){
        this.dataList = dataList;
        this.context= context;


    }
    @NonNull
    @Override
    public UploadHomeWork_Adapter.HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_homework,parent,false);
/*
        int height = ChatRooms.height / 8;
        int width =ChatRooms.width  / 4;
        view.setMinimumHeight(height);
        view.setMinimumHeight(width);
*/
        return new HolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadHomeWork_Adapter.HolderView holder, final int position) {



        try
        {
            Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),dataList.get(position));
                holder.ivImage.setImageBitmap(bitmap);

            /*    RequestOptions requestOptions = new RequestOptions();
                requestOptions.centerCrop();
                requestOptions.override(150, 150);
                requestOptions.placeholder(R.drawable.user);
                requestOptions.error(R.drawable.user);

                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions)
                        .load(dataList.get(position))
                        .into(holder.ivImage);*/



           /* Picasso.with(context).
                    load(dataList.get(position).getUrl())
                    .placeholder(R.drawable.gradient_placeholder)
                    .error(R.drawable.gradient_placeholder)
                    .into(holder.ivImage);*/


        }
        catch (Exception e)
        {
           // Log.d("qsqwdwweq",dataList.get(position).getUrl());

        }


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(position);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    public class HolderView extends RecyclerView.ViewHolder {

        ImageView ivImage,remove;
        public HolderView(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.image);
            remove = itemView.findViewById(R.id.remove);
        }
    }



}
