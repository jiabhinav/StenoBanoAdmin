package com.stenobano.admin.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.AdminRequestAdapter;
import com.stenobano.admin.adapter.User_List_Recycler;
import com.stenobano.admin.databinding.FragmentUserRequestBinding;
import com.stenobano.admin.model.SearchModel;
import com.stenobano.admin.model.Search_Model;
import com.stenobano.admin.model.UserRequestModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class UserRequestFragment extends Fragment implements AdminRequestAdapter.clickUser{
    FragmentUserRequestBinding binding;
    private ProcessingDialog processingDialog;
    AdminRequestAdapter adminRequestAdapter;
    List<SearchModel> modellist;
    private Context context= getActivity();
    private SwipeRefreshLayout refreshLayout;
    List<UserRequestModel> model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model=new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_user_request, container, false);

        return binding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context= getActivity();
        processingDialog=new ProcessingDialog(context);
        getRequestedList();
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequestedList();
                binding.refreshLayout.setRefreshing(false);
            }
        });

        modellist=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        binding.recyclerView.setLayoutManager(layoutManager);

    }

    private void getRequestedList() {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("wait...");
        progressDialog.show();
        Map<String,String> map=new HashMap();
        map.put("key","dkjwfmewkfeoic");
        Call<List<UserRequestModel>> call = APIClient.getInstance().getUserAdminRequest(map);
        call.enqueue(new Callback<List<UserRequestModel>>() {
            @Override
            public void onResponse(Call<List<UserRequestModel>> call, retrofit2.Response<List<UserRequestModel>> response) {
                progressDialog.dismiss();
                Log.d("getUserAdminRequest", "msg" + new Gson().toJson(response.body()));
                model=response.body();
               if(model.size()>0)
               {
                adminRequestAdapter =new AdminRequestAdapter(model,getActivity(),UserRequestFragment.this);
                binding.recyclerView.setAdapter(adminRequestAdapter);
                adminRequestAdapter.notifyDataSetChanged();
               }
               else{
                   Toast.makeText(getContext(), "There is no Data", Toast.LENGTH_SHORT).show();
               }

            }
            @Override
            public void onFailure(Call<List<UserRequestModel>> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();

            }
        });
    }




    @Override
    public void click(int poss, String type) {
        alertDialog(model.get(poss).getUserId(),model.get(poss).getId(), type);
    }




    private void alertDialog(final String userid,String id, final String status)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.create();
        builder.setTitle("Please confirm!!");
        builder.setMessage("Do you want to"+" "+status+"?");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateLoginStatus(userid,id,status);
            }
        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void updateLoginStatus(String userid,String id,String status)
    {
        processingDialog.show("");
        Map<String,String> map=new HashMap();
        map.put("user_id",userid);
        map.put("status",status);
        map.put("id",id);
        Log.d("dwdddwdwdd", "updateLoginStatus: "+map.toString());
        Call<JsonObject> call = APIClient.getInstance().updateLoginRequest(map);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));
                try {
                    JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                   // Log.d("type122sdddd", "msg" + jsonObject.getString("data"));
                    Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getRequestedList();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();

            }
        });

    }
}