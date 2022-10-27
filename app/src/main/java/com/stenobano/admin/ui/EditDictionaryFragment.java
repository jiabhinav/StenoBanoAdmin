package com.stenobano.admin.ui;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.SearchAdapter;
import com.stenobano.admin.adapter.User_List_Recycler;
import com.stenobano.admin.databinding.FragmentEditDictionaryBinding;
import com.stenobano.admin.fragment.GetSearch_ResultFragment;
import com.stenobano.admin.fragment.StudentDetailsFragment;
import com.stenobano.admin.model.Category_Model;
import com.stenobano.admin.model.SearchWord;
import com.stenobano.admin.model.dashboard.DashBoardModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.sqlite_databse.Search_History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EditDictionaryFragment extends Fragment implements View.OnClickListener {
  public static   FragmentEditDictionaryBinding binding;
    private ProcessingDialog processingDialog;
    private List<Category_Model>models;
    Search_History databaseHelper;
    SearchAdapter searchAdapter;
    String editable = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_dictionary, container, false);
        processingDialog=new ProcessingDialog(getActivity());
        models=new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        binding.searchRecycler.setLayoutManager(linearLayoutManager);
        binding.search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (binding.search.getText().toString().equals("")) {
                    } else {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        GetSearch_ResultFragment myFragment = new GetSearch_ResultFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", binding.search.getText().toString());
                        myFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, myFragment)
                                .addToBackStack(null).commit();
                    }

                    return true;
                }
                return false;
            }
        });
        binding.search.addTextChangedListener(new TextWatcher() {
            Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
            Runnable workRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    //Toast.makeText(SearchResultsActivity.this, "clear", Toast.LENGTH_SHORT).show();
                    models.clear();
                } else {
                    int counter = 0;
                    if (s.toString().length() >= 2) {
                        handler.removeCallbacks(workRunnable);
                        workRunnable = () -> gettext(s.toString());
                        handler.postDelayed(workRunnable, 750 /*delay*/);
                    } else {
                    }
                    //searchItems(s.toString(),curr_page,last_page);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel) {
            binding.search.setText("");
        }
    }

    private final void gettext(String str) {
        editable = str;
        searchItems(str);
        // Toast.makeText(this, "str is "+str, Toast.LENGTH_SHORT).show();
    }

    public void searchItems(final String sear) {
        binding.progress.setVisibility(View.VISIBLE);
        RequestBody title = RequestBody.create(MultipartBody.FORM, sear);
        Call<SearchWord> call = APIClient.getInstance().searchWord(title);
        call.enqueue(new Callback<SearchWord>() {
            @Override
            public void onResponse(Call<SearchWord>call, retrofit2.Response<SearchWord> response) {
                Log.d("ffwdwdwd", "msg" + new Gson().toJson(response.body()));
                if(response.body().getStatus()==200){
                    searchAdapter =new SearchAdapter(response.body().getResult(),getActivity());
                    binding.searchRecycler.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getContext(), binding.search.getText().toString()+" "+"not Found!!!", Toast.LENGTH_LONG).show();
                  }
                binding.progress.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<SearchWord>call, Throwable t) {
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: "+t.getMessage());

            }
        });
    }
    }

