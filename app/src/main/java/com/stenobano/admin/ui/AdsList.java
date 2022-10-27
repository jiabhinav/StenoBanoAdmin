package com.stenobano.admin.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stenobano.admin.R;
import com.stenobano.admin.activity.EditAds;
import com.stenobano.admin.activity.GetDataNew;
import com.stenobano.admin.adapter.AdsBannerAdapter;
import com.stenobano.admin.config.Constant;
import com.stenobano.admin.databinding.AlertAdsBinding;
import com.stenobano.admin.databinding.FragmentAdsListBinding;
import com.stenobano.admin.model.ModelAds;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;


public class AdsList extends Fragment implements View.OnClickListener, AdsBannerAdapter.BottomMenu {

   private FragmentAdsListBinding binding;
    private ProcessingDialog processingDialog;
    List<ModelAds.Result> modelListAds;
    AlertAdsBinding alertbinding;
    AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processingDialog=new ProcessingDialog(getActivity());
        modelListAds=new ArrayList<>();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_ads_list, container, false);
        getAds();
        return binding.getRoot();
    }
    private void getAds()
    {
        processingDialog.show("Wait...");
        RequestBody key = RequestBody.create(MultipartBody.FORM, "stenobano");
        Call<ModelAds> call = APIClient.getInstance().getadstoAdmin(key);
        call.enqueue(new Callback<ModelAds>() {
            @Override
            public void onResponse(Call<ModelAds> call, retrofit2.Response<ModelAds> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));
                ModelAds model=response.body();
                modelListAds=model.getResult();

                if (modelListAds.size()>0 && model.getStatus()==200)
                {
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                    binding.recyclerUserList.setLayoutManager(linearLayoutManager);
                    AdsBannerAdapter adapter=new AdsBannerAdapter(getActivity(),AdsList.this,modelListAds);
                    binding.recyclerUserList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ModelAds> call, Throwable t) {
                Log.d("erere",t.toString());

            }
        });
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void clickBanner(int position) {
        //todo 0=desable,1=enable,2=delete
      int type;
        alertbinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.alert_ads, null, false);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        if (modelListAds.get(position).getStatus().equals("1"))
        {

            alertbinding.disable.setText("Diactive");
            type=0;
        }
        else
        {
            alertbinding.disable.setText("Active");
            type=1;
        }


        builder.setView(alertbinding.getRoot());
         alertDialog=builder.create();

        alertDialog.show();
        alertbinding.disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAds(type,modelListAds.get(position).getId(),modelListAds.get(position).getImage());
            }
        });

        alertbinding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constant.resultlist=modelListAds.get(position);
                Intent intent=new Intent(getActivity(), EditAds.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent,101);
                alertDialog.dismiss();
            }
        });
        alertbinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to remove ads?");
                builder.setPositiveButton("Remove Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateAds(2,modelListAds.get(position).getId(),modelListAds.get(position).getImage());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
    }



    private void updateAds(int type,String id,String imageurl)
    {
        processingDialog.show("Wait...");
        RequestBody types = RequestBody.create(MultipartBody.FORM, String.valueOf(type));
        RequestBody ids = RequestBody.create(MultipartBody.FORM, id);
        RequestBody imageurs = RequestBody.create(MultipartBody.FORM, imageurl);
        Call<ModelAds> call = APIClient.getInstance().update_ads(types,ids,imageurs);
        call.enqueue(new Callback<ModelAds>() {
            @Override
            public void onResponse(Call<ModelAds> call, retrofit2.Response<ModelAds> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));

                if (response.body().getStatus()==200)
                {
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getAds();
                }
                else
                {
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ModelAds> call, Throwable t) {
                Log.d("erere",t.toString());

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101 && resultCode==RESULT_OK)
        {
            getAds();
        }
    }
}