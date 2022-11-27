package com.stenobano.admin.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.User_List_Recycler;
import com.stenobano.admin.databinding.FragmentUsersListBinding;
import com.stenobano.admin.fragment.GetSearch_ResultFragment;
import com.stenobano.admin.model.Category_Model;
import com.stenobano.admin.model.SearchModel;
import com.stenobano.admin.model.Search_Model;
import com.stenobano.admin.model.SerchUserListModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.sqlite_databse.Search_History;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class UsersListFragment extends Fragment  implements View.OnClickListener,User_List_Recycler.ClickChat {
    private ProcessingDialog processingDialog;
    FragmentUsersListBinding usersListBinding;
    List<SearchModel> modellist;
    List<SerchUserListModel> serchUserListModelList;
    User_List_Recycler user_list_recycler;
    String name = "name", email = "email";
    int str = 0;
    int last = 50;
    String editable = "";
    int type = 0;
    public static String id = "0";
    List<SerchUserListModel.Result> model;
    private Context context = getActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        usersListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        modellist = new ArrayList<>();
        return usersListBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        usersListBinding.more.setOnClickListener(this);
        processingDialog = new ProcessingDialog(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(usersListBinding.recyclerUserList.getContext(),
                layoutManager.getOrientation());
        usersListBinding.recyclerUserList.addItemDecoration(dividerItemDecoration);
        usersListBinding.recyclerUserList.setLayoutManager(layoutManager);

        if (getArguments() != null) {
            id = getArguments().getString("id");
            getUserListById(String.valueOf(str), String.valueOf(last), id);
        } else {
            if (name.equals("name")) {
                getUserList(String.valueOf(str), String.valueOf(last));
            } else {
                getSearch_User(name, email);
            }
        }


        usersListBinding.search.addTextChangedListener(new TextWatcher() {
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
                    try {
                        serchUserListModelList.clear();
                    }
                    catch (Exception e)
                    {

                    }

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
        usersListBinding.search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (usersListBinding.search.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Enter search word", Toast.LENGTH_SHORT).show();
                    } else {
                        Search_History db = new Search_History(getContext());
                        Category_Model model = new Category_Model();
                        model.setName(usersListBinding.search.getText().toString());
                        db.addContact(model);
                        Intent i = new Intent(getContext(), GetSearch_ResultFragment.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("title", usersListBinding.search.getText().toString());
                        startActivity(i);
                    }

                    return true;
                }
                return false;
            }
        });
    }

    private final void gettext(String str) {
        editable = str;
        searchItems(str);
        // Toast.makeText(this, "str is "+str, Toast.LENGTH_SHORT).show();
    }

    private void searchItems(final String sear) {
        processingDialog.show("wait...");
        // RequestBody.create(MultipartBody.FORM, "stenobano");
        //Map<String, RequestBody> partMap;
        Map<String, String> map = new HashMap();
        map.put("title", sear);

        Call<SerchUserListModel> call = APIClient.getInstance().search_user(map);
        call.enqueue(new Callback<SerchUserListModel>() {
            @Override
            public void onResponse(Call<SerchUserListModel> call, retrofit2.Response<SerchUserListModel> response) {
                processingDialog.dismiss();
                SerchUserListModel modelist = response.body();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Total :" + model.size() + " " + "users");
                user_list_recycler = new User_List_Recycler(modelist.getResult(), getActivity(),UsersListFragment.this);
                usersListBinding.recyclerUserList.setAdapter(user_list_recycler);
                user_list_recycler.notifyDataSetChanged();

            }


            @Override
            public void onFailure(Call<SerchUserListModel> call, Throwable t) {
                Log.d("erere", t.toString());
                processingDialog.dismiss();

            }
        });
    }

    private void getUserList(String str, String last) {
        //todo type 0=default ,type 1=search on id base
        processingDialog.show("wait...");
        //RequestBody.create(MultipartBody.FORM, "stenobano");
        //Map<String, RequestBody> partMap;
        Map<String, String> map = new HashMap();
        map.put("str", str);
        map.put("last", last);
        map.put("type", "0");
        map.put("id", "0");
        Call<SerchUserListModel> call = APIClient.getInstance().searchUser_Model(map);
        call.enqueue(new Callback<SerchUserListModel>() {
            @Override
            public void onResponse(Call<SerchUserListModel> call, retrofit2.Response<SerchUserListModel> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));

                SerchUserListModel modelist = response.body();
                for (int i = 0; i < modelist.getResult().size(); i++) {
                    model.add(modelist.getResult().get(i));
                }

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Total :" + model.size() + " " + "users");
                user_list_recycler = new User_List_Recycler(model, getActivity(),UsersListFragment.this);
                usersListBinding.recyclerUserList.setAdapter(user_list_recycler);
                // user_list_recycler.notifyDataSetChanged();

            }


            @Override
            public void onFailure(Call<SerchUserListModel> call, Throwable t) {
                Log.d("erere", t.toString());
                processingDialog.dismiss();

            }
        });
    }

    private void getUserListById(String str, String last, String id) {
        //todo type 0=default ,type 1=search on id base
        processingDialog.show("wait...");
        //RequestBody.create(MultipartBody.FORM, "stenobano");
        //Map<String, RequestBody> partMap;
        Map<String, String> map = new HashMap();
        map.put("str", str);
        map.put("last", last);
        map.put("type", "1");
        map.put("id", id);
        Call<SerchUserListModel> call = APIClient.getInstance().searchUser_Model(map);
        call.enqueue(new Callback<SerchUserListModel>() {
            @Override
            public void onResponse(Call<SerchUserListModel> call, retrofit2.Response<SerchUserListModel> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));

                SerchUserListModel modelist = response.body();
                for (int i = 0; i < modelist.getResult().size(); i++) {
                    model.add(modelist.getResult().get(i));
                }

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Total :" + model.size() + " " + "users");
                user_list_recycler = new User_List_Recycler(model, getActivity(),UsersListFragment.this);
                usersListBinding.recyclerUserList.setAdapter(user_list_recycler);
                // user_list_recycler.notifyDataSetChanged();

            }


            @Override
            public void onFailure(Call<SerchUserListModel> call, Throwable t) {
                Log.d("erere", t.toString());
                processingDialog.dismiss();

            }
        });
    }

    private void getSearch_User(String str, String last) {
        processingDialog.show("wait...");
        // RequestBody.create(MultipartBody.FORM, "stenobano");
        //Map<String, RequestBody> partMap;
        Map<String, String> map = new HashMap();
        map.put("str", str);
        map.put("last", last);
        map.put("name", name);
        map.put("email", email);
        Call<SerchUserListModel> call = APIClient.getInstance().search_Model(map);
        call.enqueue(new Callback<SerchUserListModel>() {
            @Override
            public void onResponse(Call<SerchUserListModel> call, retrofit2.Response<SerchUserListModel> response) {
                processingDialog.dismiss();
                Log.d("searchuser", "msg" + new Gson().toJson(response.body()));
                SerchUserListModel modelist = response.body();
                for (int i = 0; i < modelist.getResult().size(); i++) {
                    model.add(modelist.getResult().get(i));
                }

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Total :" + model.size() + " " + "users");
                user_list_recycler = new User_List_Recycler(model, getActivity(),UsersListFragment.this);
                usersListBinding.recyclerUserList.setAdapter(user_list_recycler);
                user_list_recycler.notifyDataSetChanged();
            }


            @Override
            public void onFailure(Call<SerchUserListModel> call, Throwable t) {
                Log.d("erere", t.toString());
                processingDialog.dismiss();

            }
        });
    }


    //    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Intent i;
//        switch (item.getItemId()) {
//            case R.id.search:
//                Toast.makeText(getContext(), "hhhh", Toast.LENGTH_SHORT).show();
//                AppCompatActivity activity = (AppCompatActivity) getContext();
//                UserSearchFragment myFragment = new UserSearchFragment();
//                activity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_host_fragment, myFragment)
//                        .addToBackStack(null).commit();
//                return true;
//
//        }
//        return onOptionsItemSelected(item);
//    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel) {
            usersListBinding.search.setText("");
        }
        if (id == R.id.more) {
            if (type == 0) {
                str = str + 50;
                getUserList(String.valueOf(str), String.valueOf(last));
            }
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