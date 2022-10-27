package com.stenobano.admin.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.gson.Gson;
import com.stenobano.admin.Interface.AlertDialogInterFace;
import com.stenobano.admin.Interface.Download;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.CategoryDetailsAdapter;
import com.stenobano.admin.databinding.ActivityGetDataNewBinding;
import com.stenobano.admin.model.ModelCategoryDetail;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GetDataNew extends AppCompatActivity {
    public static AlertDialog.Builder dialogBuilder;
    public static List<ModelCategoryDetail.Result> modellist;
    String type_id;
    ProcessingDialog processingDialog;
    LinearLayoutManager mManager;
    CategoryDetailsAdapter adapter2;
    public static TextView text1,text2,text3;
    private ActivityGetDataNewBinding binding;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_get_data_new);
        processingDialog=new ProcessingDialog(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EnableRuntimePermission();
        modellist = new ArrayList<>();
        Intent intent = getIntent();
        type_id = intent.getStringExtra("type_id");
        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        binding.recyclerlist.setLayoutManager(layoutManager);
        if (!isOnline()) {
            checkConnection();
        } else {

            fetch();
        }


        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = binding.search.getText().toString().toLowerCase(Locale.getDefault());
                //adapter.getFilter().filter(s);
                filter(s.toString());


            }

        });

    }


    void filter(String text){
        List<ModelCategoryDetail.Result> temp = new ArrayList();
        for(ModelCategoryDetail.Result d: modellist){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getTitle().toUpperCase().contains(text)|| d.getTitle().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        try
        {
            adapter2.updateList(temp);
        }
        catch(NullPointerException e)
        {

        }

    }





    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection() {

        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isOnline())
                {
                    alert_online();
                }
                else
                {
                    fetch();
                }


            }
        }).setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void alert_online() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setCancelable(false);
        builder.setMessage("Please turn on internet connection to continue");
        builder.setPositiveButton("Retry!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isOnline())
                {
                    alert_online();
                }
                else
                {
                    fetch();
                }
            }
        }).setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(GetDataNew.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(GetDataNew.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(GetDataNew.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
    }




    private void fetch()
    {
        processingDialog.show("wait...");

        RequestBody key = RequestBody.create(MultipartBody.FORM, type_id);
        Call<ModelCategoryDetail> call = APIClient.getInstance().getDataAudio_Image(key);
        call.enqueue(new Callback<ModelCategoryDetail>() {
            @Override
            public void onResponse(Call<ModelCategoryDetail> call, retrofit2.Response<ModelCategoryDetail> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));
                ModelCategoryDetail model=response.body();
                modellist=model.getResult();
                adapter2=new CategoryDetailsAdapter(modellist, GetDataNew.this);
                binding.recyclerlist.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ModelCategoryDetail> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();
            }
        });
    }



}
