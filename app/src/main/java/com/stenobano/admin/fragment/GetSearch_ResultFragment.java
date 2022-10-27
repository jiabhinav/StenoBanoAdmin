package com.stenobano.admin.fragment;

import android.os.Bundle;

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
import com.stenobano.admin.adapter.GetSearch_Adapter;
import com.stenobano.admin.adapter.SearchAdapter;
import com.stenobano.admin.databinding.FragmentGetSearchResultBinding;
import com.stenobano.admin.model.GetSearchWord;
import com.stenobano.admin.model.SearchWord;
import com.stenobano.admin.retrofit.APIClient;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GetSearch_ResultFragment extends Fragment {
    FragmentGetSearchResultBinding binding;
    GetSearch_Adapter getSearch_adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_search__result, container, false);
        Bundle bundle = getArguments();
        String  title=(bundle.getString("title"));
        Log.e("v", "friendsID :" + title);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        getSearchItems(title);
        return binding.getRoot();
    }

    public void getSearchItems(String title) {
       // binding.progress.setVisibility(View.VISIBLE);
        RequestBody titles = RequestBody.create(MultipartBody.FORM, title);
        Call<GetSearchWord> call = APIClient.getInstance().getSearch_Word(titles);
        call.enqueue(new Callback<GetSearchWord>() {
            @Override
            public void onResponse(Call<GetSearchWord>call, retrofit2.Response<GetSearchWord> response) {
                Log.d("getsearch", "msg" + new Gson().toJson(response.body()));
                if(response.body().getStatus()==200){
                    getSearch_adapter =new GetSearch_Adapter(response.body().getResult(),getActivity());
                    binding.recyclerView.setAdapter(getSearch_adapter);
                    getSearch_adapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getContext()," "+"Not Found Word !!!", Toast.LENGTH_LONG).show();

                }
             //   binding.progress.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<GetSearchWord>call, Throwable t) {
               // binding.progress.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: "+t.getMessage());

            }
        });
    }

}