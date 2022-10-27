package com.stenobano.admin.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.CategoryAapter;
import com.stenobano.admin.databinding.FragmentViewCategoryBinding;
import com.stenobano.admin.model.HomeModelCategory;
import com.stenobano.admin.model.ModelCategory;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class ViewCategory extends Fragment {

    private ProcessingDialog processingDialog;
    private FragmentViewCategoryBinding binding;
    List<HomeModelCategory.Result> modellist;
    CategoryAapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processingDialog=new ProcessingDialog(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_view_category, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        modellist=new ArrayList<>();
        LinearLayoutManager layoutManager=new GridLayoutManager(getActivity(),3,LinearLayoutManager.VERTICAL,false);

        binding.recyclerView.setLayoutManager(layoutManager);

        getAdminDashboardData();
    }

    private void getAdminDashboardData()
    {
        processingDialog.show("wait...");

        RequestBody key = RequestBody.create(MultipartBody.FORM, "stenobano");
        Call<HomeModelCategory> call = APIClient.getInstance().homegetCategory(key);
        call.enqueue(new Callback<HomeModelCategory>() {
            @Override
            public void onResponse(Call<HomeModelCategory> call, retrofit2.Response<HomeModelCategory> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));
                HomeModelCategory model=response.body();
                modellist=model.getResult();
                adapter=new CategoryAapter(modellist,getActivity());
                binding.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<HomeModelCategory> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();
            }
        });
    }

}