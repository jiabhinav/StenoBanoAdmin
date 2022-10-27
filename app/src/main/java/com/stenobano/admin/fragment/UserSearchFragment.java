package com.stenobano.admin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.User_List_Recycler;
import com.stenobano.admin.databinding.FragmentUserSearchBinding;
import com.stenobano.admin.model.Category_Model;
import com.stenobano.admin.model.SearchModel;
import com.stenobano.admin.model.Search_Model;
import com.stenobano.admin.model.SerchUserListModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.sqlite_databse.Search_History;
import com.stenobano.admin.ui.UsersListFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class UserSearchFragment extends Fragment implements View.OnClickListener,User_List_Recycler.ClickChat{
    FragmentUserSearchBinding binding;
    private ProcessingDialog processingDialog;
    String editable="";
    List<SerchUserListModel> modellist;
    User_List_Recycler user_list_recycler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processingDialog=new ProcessingDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_search, container, false);
        binding.cancel.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // ((AppCompatActivity)getActivity()).getSupportActionBar(binding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                } else {
                    int counter = 0;
                    if (s.toString().length() >= 2) {
                        handler.removeCallbacks(workRunnable);
                      //  workRunnable = () -> gettext(s.toString());
                        handler.postDelayed(workRunnable, 750 /*delay*/);
                    } else {
                    }
                    //searchItems(s.toString(),curr_page,last_page);

                }

            }

        });
        binding.search.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (binding.search.getText().toString().equals(""))
                    {
                        Toast.makeText(getContext(), "Enter search word", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Search_History db = new Search_History(getContext());
                        Category_Model model = new Category_Model();
                        model.setName(binding.search.getText().toString());
                        db.addContact(model);
                        Intent i=new Intent(getContext(),GetSearch_ResultFragment.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("title",binding.search.getText().toString());
                        startActivity(i);
                    }

                    return true;
                }
                return false;
            }
        });

    }

    private void searchItems(final String sear)
    {
        processingDialog.show("wait...");
        // RequestBody.create(MultipartBody.FORM, "stenobano");
        //Map<String, RequestBody> partMap;
        Map<String,String> map=new HashMap();
        map.put("title", sear);

        Call<SerchUserListModel> call = APIClient.getInstance().search_user(map);
        call.enqueue(new Callback<SerchUserListModel>() {
            @Override
            public void onResponse(Call<SerchUserListModel> call, retrofit2.Response<SerchUserListModel> response) {
                processingDialog.dismiss();
                Log.d("searchuser", "msg" + new Gson().toJson(response.body()));
                List<SerchUserListModel.Result> model=response.body().getResult();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Total :"+model.size()+" "+"users");
                user_list_recycler =new User_List_Recycler(model,getActivity(), UserSearchFragment.this);
                binding.searchRecycler.setAdapter(user_list_recycler);

                user_list_recycler.notifyDataSetChanged();
            }


            @Override
            public void onFailure(Call<SerchUserListModel> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();

            }
        });
    }
    private final void gettext(String str) {
        editable=str;
       searchItems(str);

        //  Toast.makeText(this, "str is "+str, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();

        if (id==R.id.cancel)
        {
            binding.search.setText("");
        }
    }

    @Override
    public void clickChat(String id) {
        NavHostFragment navHostFragment = (NavHostFragment)getActivity().getSupportFragmentManager().getPrimaryNavigationFragment();
        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
        Fragment loginFragment = fragmentManager.getPrimaryNavigationFragment();
        Bundle bundle=new Bundle();
        bundle.putString("user_id",id);
        NavHostFragment.findNavController(loginFragment).navigate(R.id.user_detail,bundle);
    }
}