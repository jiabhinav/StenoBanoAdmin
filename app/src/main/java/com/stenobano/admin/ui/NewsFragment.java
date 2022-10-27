package com.stenobano.admin.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stenobano.admin.R;
import com.stenobano.admin.databinding.FragmentNewsBinding;
import com.stenobano.admin.fragment.UpdateEdit_UserFragment;
import com.stenobano.admin.model.AddNews;
import com.stenobano.admin.model.DeleteNews;
import com.stenobano.admin.model.UpdateNews;
import com.stenobano.admin.model.news.NewsDetailsModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class NewsFragment extends Fragment implements View.OnClickListener{
    FragmentNewsBinding binding;
    private ProcessingDialog processingDialog;
    private Context context= getActivity();

    ArrayList<NewsDetailsModel.News> newsList, data;
    public static ArrayList<NewsDetailsModel.News> mData = new ArrayList<>();
    Type listType;
    String id= "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_news, container, false);
        binding.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    binding.layoutUrl.setVisibility(View.VISIBLE);                }
                else{
                    binding.layoutUrl.setVisibility(View.GONE);                }
            }
        });
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context= getActivity();
        processingDialog=new ProcessingDialog(context);
        newsList = new ArrayList<>();
        data = new ArrayList<>();
        listType = new TypeToken<NewsDetailsModel>() {
        }.getType();


        binding.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    binding.layoutUrl.setVisibility(View.VISIBLE);                }
                else{
                    binding.layoutUrl.setVisibility(View.GONE);                }
            }
        });
//        if(binding.checkbox.isChecked()==true){
//            binding.url.setVisibility(View.VISIBLE);
//        }else{
//            binding.url.setVisibility(View.GONE);
//        }

        getNewsDetails();
        binding.updateButton.setOnClickListener(this);
        binding.deleteButtton.setOnClickListener(this);
        binding.addbutton.setOnClickListener(this);
    }

    private void  getNewsDetails()
    {
        processingDialog.show("wait for user...");
        Call<NewsDetailsModel> call = APIClient.getInstance().getNewsDetails();
        call.enqueue(new Callback<NewsDetailsModel>() {
            @Override
            public void onResponse(Call<NewsDetailsModel> call, retrofit2.Response<NewsDetailsModel> response) {
                Log.d("userget", new Gson().toJson(response.body()+""));
                // Toast.makeText(getContext(), "userget", Toast.LENGTH_SHORT).show();
                 processingDialog.dismiss();
                try {
                    NewsDetailsModel model=response.body();
                    if (model.getStatus()==200) {
                        id = model.getNews().get(0).getId();
                        binding.message.setText(model.getNews().get(0).getMessage());
                        if(!model.getNews().equals(null)) {
                            binding.buttons.setVisibility(View.VISIBLE);
                            binding.addbutton.setVisibility(View.GONE);
                            if (model.getNews().get(0).getClickable().equals("1")) {
                                binding.checkbox.setChecked(true);
                                binding.layoutUrl.setVisibility(View.VISIBLE);
                                binding.url.setText(model.getNews().get(0).getUrl());
                            } else {
                                binding.checkbox.setChecked(false);
                                binding.layoutUrl.setVisibility(View.GONE);
                            }
                            if (model.getNews().get(0).getStatus().equals("1")) {
                                binding.active.setChecked(true);
                                binding.deactive.setChecked(false);

                            } else {
                                binding.active.setChecked(false);
                                binding.deactive.setChecked(true);

                            }
                    }else{
                        binding.addbutton.setVisibility(View.VISIBLE);
                            binding.buttons.setVisibility(View.GONE);
                    }
                    }
                } catch (Exception e) {
                    Log.e("Exce", e.toString());

                }
            }
            @Override
            public void onFailure(Call<NewsDetailsModel> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();

            }
        });
    }

    private void  getUpdateNews()
    {
        processingDialog.show("wait for user...");
        Map<String,String> map=new HashMap();
        map.put("message", binding.message.getText().toString());
        if(binding.checkbox.isChecked()){
            map.put("click", "1");
            map.put("url", binding.url.getText().toString());
        }else{
            map.put("click", "0");
        }
        if(binding.active.isChecked()){
            map.put("status","1");
        }else{
            map.put("status","0");
        }
        map.put("id",id);
        Call<UpdateNews> call = APIClient.getInstance().updatNews(map);
        call.enqueue(new Callback<UpdateNews>() {
            @Override
            public void onResponse(Call<UpdateNews> call, retrofit2.Response<UpdateNews> response) {
               processingDialog.dismiss();
                UpdateNews model=response.body();
                if(model.getStatus()==200){
                    Log.d("updatenews", response.body()+"");
                Toast.makeText(getContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
                getNewsDetails();
               }
            }
            @Override
            public void onFailure(Call<UpdateNews> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();
            }
        });
    }
    private void  getAddNews()
    {
         processingDialog.show("wait for user...");
        Map<String,String> map=new HashMap();
        map.put("message", binding.message.getText().toString());
        if(binding.checkbox.isChecked()){
            map.put("click", "1");
            map.put("url", binding.url.getText().toString());
        }else{
            map.put("click", "0");
        }
        if(binding.active.isChecked()){
            map.put("status","1");
        }else{
            map.put("status","0");
        }
        map.put("id",id);
        Call<AddNews> call = APIClient.getInstance().addNews(map);
        call.enqueue(new Callback<AddNews>() {
            @Override
            public void onResponse(Call<AddNews> call, retrofit2.Response<AddNews> response) {
                 processingDialog.dismiss();
                AddNews model=response.body();
                if(model.getStatus()==200){
                Log.d("updatenews", response.body()+"");
                Toast.makeText(getContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
                getNewsDetails();
            }}
            @Override
            public void onFailure(Call<AddNews> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();
            }
        });
    }
    private void  getDeleteNews()
    {
        processingDialog.show("wait for user...");
        Map<String,String> map=new HashMap();
        map.put("id",id);
        Call<DeleteNews> call = APIClient.getInstance().deleteNews(map);
        call.enqueue(new Callback<DeleteNews>() {
            @Override
            public void onResponse(Call<DeleteNews> call, retrofit2.Response<DeleteNews> response) {
                processingDialog.dismiss();
                Log.d("deletenews", response.body()+"");
                DeleteNews model = response.body();
                if(model.getStatus()==200){
                Toast.makeText(getContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
                getNewsDetails();

            }}
            @Override
            public void onFailure(Call<DeleteNews> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();

            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.updateButton)
        {
            getUpdateNews();
        }
        else if (v.getId()==R.id.deleteButtton)
        {
            getDeleteNews();
        }
        else if (v.getId()==R.id.addbutton )
        {
            getAddNews();
        }
    }
}