 package com.stenobano.admin.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.stenobano.admin.databinding.ListUserrequestBinding;
import com.stenobano.admin.model.Search_Model;
import com.stenobano.admin.model.UserRequestModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AdminRequestAdapter extends RecyclerView.Adapter<AdminRequestAdapter.HolderItem> {
   private List<UserRequestModel> search_modelList;
    Context context;
    public  String finalDayl;

    clickUser clickUser;
    public AdminRequestAdapter(List<UserRequestModel> mlistItem, Context context, clickUser clickUser) {
        this.search_modelList = mlistItem;
        this.context = context;
        this.clickUser=clickUser;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListUserrequestBinding itemBinding = ListUserrequestBinding.inflate(layoutInflater, parent, false);
        return new HolderItem(itemBinding);

    }

    @Override
    public void onBindViewHolder(final HolderItem holder, final int position) {
        final UserRequestModel model = search_modelList.get(position);
        try {
            holder.binding.date.setText(getDate(model.getCreatedAt()));
            if (model.getStatus().equals("Pending"))
            {
                holder.binding.status.setSelection(0);
            }
            else if (model.getStatus().equals("Approved"))
            {
                holder.binding.status.setSelection(1);
            }
            else
            {
                holder.binding.status.setSelection(2);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        //Log.d("xcscsvc", "onBindViewHolder: "+model.getCreatedAt());


        holder.binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  alertDialog(model.getId(),holder.binding.status.getSelectedItem().toString(),v);
                clickUser.click(position,holder.binding.status.getSelectedItem().toString());
            }
        });
        holder.bind(model);

    }

    @Override
    public int getItemCount() {
        return search_modelList.size();
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
        private ListUserrequestBinding binding;

        public void bind(UserRequestModel searchModel) {
            binding.setUser(searchModel);
            binding.executePendingBindings();

        }
        public HolderItem(ListUserrequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    public interface  clickUser
    {
        public void click(int poss,String type);

    }


    private String getDate(String da)
    {

        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd H:m:s");
        Date dt1= null;
        try {
            dt1 = format1.parse(da);
            DateFormat format2=new SimpleDateFormat("EE dd-MMM-yy H:m:s");
            finalDayl=format2.format(dt1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDayl;
    }


}