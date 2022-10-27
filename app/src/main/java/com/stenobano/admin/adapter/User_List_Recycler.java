package com.stenobano.admin.adapter;
import android.app.AlertDialog;
import android.content.Context;;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.stenobano.admin.R;
import com.stenobano.admin.databinding.UserListListBinding;
import com.stenobano.admin.fragment.User_View_DetailsFragment;
import com.stenobano.admin.model.SearchModel;
import com.stenobano.admin.model.Search_Model;
import com.stenobano.admin.model.SerchUserListModel;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.ui.UsersListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class User_List_Recycler extends RecyclerView.Adapter<com.stenobano.admin.adapter.User_List_Recycler.HolderItem> {
   private List<SerchUserListModel.Result> search_modelList;
    Context context;
    String pid;
    ClickChat clickChat;

    public User_List_Recycler(List<SerchUserListModel.Result> mlistItem, Context context, ClickChat clickChat) {
        this.search_modelList = mlistItem;
        this.context = context;
        this.clickChat=clickChat;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UserListListBinding itemBinding = UserListListBinding.inflate(layoutInflater, parent, false);
        return new HolderItem(itemBinding);

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final SerchUserListModel.Result model = search_modelList.get(position);
        pid = model.getId();
        holder.binding.id.setText(position+1+"");
        holder.bind(model);
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do you want sure delete"+" ' "+model.getName()+" ' " +"user?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {
                                delete_user(model.getId(),position);
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });
        holder.binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickChat.clickChat( model.getId());
                    /*AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    User_View_DetailsFragment myFragment = new User_View_DetailsFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("user_id", model.getId());
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, myFragment)
                            .addToBackStack(null).commit();*/




            }
        });
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
        private UserListListBinding binding;
        public void bind(SerchUserListModel.Result searchModel) {
            binding.setUser(searchModel);
            binding.executePendingBindings();

        }
        public HolderItem(UserListListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public interface ClickChat
    {
        void clickChat(String id);
    }



    private void  delete_user(String uid, final int i) {
        {
            Map<String,String> map=new HashMap();
            map.put("id", uid);
            Call<List<SerchUserListModel>> call = APIClient.getInstance().delete_user(map);
            call.enqueue(new Callback<List<SerchUserListModel>>() {
                @Override
                public void onResponse(Call<List<SerchUserListModel>> call, retrofit2.Response<List<SerchUserListModel>> response) {
                    Log.d("delete", "msg" + new Gson().toJson(response.body()));
                    Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                    search_modelList.remove(i);
                    notifyDataSetChanged();
                }
                @Override
                public void onFailure(Call<List<SerchUserListModel>> call, Throwable t) {
                    Log.d("erere",t.toString());

                }
            });
        }
    }
}