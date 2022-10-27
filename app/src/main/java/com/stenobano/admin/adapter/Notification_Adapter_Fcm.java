package com.stenobano.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.stenobano.admin.R;
import com.stenobano.admin.databinding.FcmNotificationBinding;
import com.stenobano.admin.databinding.UserListListBinding;
import com.stenobano.admin.fragment.User_View_DetailsFragment;
import com.stenobano.admin.model.Chat;
import com.stenobano.admin.model.SearchModel;
import com.stenobano.admin.model.Search_Model;

import java.util.ArrayList;
import java.util.List;

public class Notification_Adapter_Fcm extends RecyclerView.Adapter<Notification_Adapter_Fcm.HolderItem> {
    private List<Chat> userModelArrayList;
    Context context;
    String pid;

   private ArrayList<Search_Model> arraylist;

    public Notification_Adapter_Fcm(List<Chat> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FcmNotificationBinding itemBinding = FcmNotificationBinding.inflate(layoutInflater, parent, false);
        return new HolderItem(itemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        final Chat  model = userModelArrayList.get(position);
        holder.binding.title.setText(model.getTitle());
        holder.binding.subject.setText(model.getSubject());
        holder.binding.teacher.setText(model.getDescription());
        holder.binding.date.setText(model.getDate());
        holder.bind(model);
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
        private FcmNotificationBinding binding;

        public void bind(Chat chat) {
            binding.setChat(chat);
            binding.executePendingBindings();

        }
        public HolderItem(FcmNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }

}