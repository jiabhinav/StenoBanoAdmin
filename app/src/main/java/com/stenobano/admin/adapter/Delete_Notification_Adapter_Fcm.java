package com.stenobano.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.stenobano.admin.Interface.Delete_Noti;
import com.stenobano.admin.R;
import com.stenobano.admin.databinding.DeletreFcmNotificationListBinding;
import com.stenobano.admin.databinding.FcmNotificationBinding;
import com.stenobano.admin.model.Chat;
import com.stenobano.admin.model.Search_Model;
import java.util.ArrayList;
import java.util.List;

public class Delete_Notification_Adapter_Fcm extends RecyclerView.Adapter<Delete_Notification_Adapter_Fcm.HolderItem> {
   private List<Chat> userModelArrayList;
    Context context;
    Delete_Noti delete_noti;
    private ArrayList<Search_Model> arraylist;

    public Delete_Notification_Adapter_Fcm(List<Chat> mlistItem, Context context, Delete_Noti delete_noti) {
        this.userModelArrayList = mlistItem;
        this.context = context;
        this.delete_noti=delete_noti;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DeletreFcmNotificationListBinding binding = DeletreFcmNotificationListBinding.inflate(layoutInflater, parent, false);
        return new HolderItem(binding);
    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final Chat model = userModelArrayList.get(position);
        holder.binding.title.setText(model.getTitle());
        holder.binding.teacher.setText(model.getDescription());
        holder.binding.date.setText(model.getDate());

        final String id=model.getKey();
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<userModelArrayList.size();i++)
                {
                    if (userModelArrayList.get(i).getKey().equals(id))
                    {
                        // Toast.makeText(context, ""+model.getKey(), Toast.LENGTH_SHORT).show();
                        delete_noti.deleteNotification(model.getKey());
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
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
        private DeletreFcmNotificationListBinding binding;

        public HolderItem(DeletreFcmNotificationListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}