package com.stenobano.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.stenobano.admin.R;
import com.stenobano.admin.databinding.ListHomeBinding;
import com.stenobano.admin.databinding.UserListListBinding;
import com.stenobano.admin.fragment.StudentDetailsFragment;
import com.stenobano.admin.fragment.User_View_DetailsFragment;
import com.stenobano.admin.model.SearchModel;
import com.stenobano.admin.model.dashboard.DashBoardModel;
import com.stenobano.admin.retrofit.APIClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class DashboradAdapter extends RecyclerView.Adapter<DashboradAdapter.HolderItem> {
   private List<DashBoardModel> dashBoardModelList;
    Context context;
    String pid;

    public DashboradAdapter(List<DashBoardModel> mlistItem, Context context) {
        this.dashBoardModelList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListHomeBinding itemBinding = ListHomeBinding.inflate(layoutInflater, parent, false);
        return new HolderItem(itemBinding);

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final DashBoardModel model = dashBoardModelList.get(position);
        //pid = model.getId();
      //  holder.binding.totalnumber.setText();
        holder.bind(model);

        holder.binding.llStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                StudentDetailsFragment myFragment = new StudentDetailsFragment();
                Bundle bundle=new Bundle();
                // bundle.putString("user_id", model.getId());
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, myFragment)
                        .addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dashBoardModelList.size();
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
        private ListHomeBinding binding;
        public void bind(DashBoardModel searchModel) {
            binding.setDashBoard(searchModel);
            binding.executePendingBindings();

        }
        public HolderItem(ListHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}