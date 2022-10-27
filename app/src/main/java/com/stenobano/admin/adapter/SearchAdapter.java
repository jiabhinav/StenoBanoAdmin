package com.stenobano.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.stenobano.admin.databinding.SearchListBinding;
import com.stenobano.admin.databinding.UserListListBinding;
import com.stenobano.admin.fragment.GetSearch_ResultFragment;
import com.stenobano.admin.fragment.User_View_DetailsFragment;
import com.stenobano.admin.model.Category_Model;
import com.stenobano.admin.model.SearchWord;
import com.stenobano.admin.model.Search_Model;
import com.stenobano.admin.model.SerchUserListModel;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.ui.EditDictionaryFragment;
import com.stenobano.admin.ui.home.HomeFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.HolderItem> {
   private List<SearchWord.Result> search_modelList;
    Context context;
    String p_id;
    public SearchAdapter(List<SearchWord.Result> mlistItem, Context context) {
        this.search_modelList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SearchListBinding itemBinding = SearchListBinding.inflate(layoutInflater, parent, false);
        return new HolderItem(itemBinding);

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final SearchWord.Result model = search_modelList.get(position);
        p_id=model.getId();
        holder.binding.name.setText(model.getTitle());
        holder.bind(model);
        holder.binding.imageArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  EditDictionaryFragment.binding.search.setText(model.getTitle());

            }
        });
        holder.binding.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            //Type_Searching.search.setText(model.getName());
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            GetSearch_ResultFragment myFragment = new GetSearch_ResultFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("title", model.getTitle());
                            myFragment.setArguments(bundle);
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.nav_host_fragment, myFragment)
                                    .addToBackStack(null).commit();
                    /*    }
                        else
                        {

                            Update_Delete_Word.search.setText(model.getName());
                            Intent i2 = new Intent(context, View_Update_Delete_Word.class);
                            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i2.putExtra("title", model.getName());
                            context.startActivity(i2);
                        }*/
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
        private SearchListBinding binding;
        public void bind(SearchWord.Result categoryModel) {
            binding.setCategary(categoryModel);
            binding.executePendingBindings();

        }
        public HolderItem(SearchListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}