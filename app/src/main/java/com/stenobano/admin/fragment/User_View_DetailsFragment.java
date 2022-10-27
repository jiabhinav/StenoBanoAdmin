package com.stenobano.admin.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stenobano.admin.R;
import com.stenobano.admin.databinding.FragmentUserViewDetailsBinding;
import com.stenobano.admin.databinding.FragmentUsersListBinding;
import com.stenobano.admin.model.DeleteNews;
import com.stenobano.admin.model.SearchModel;
import com.stenobano.admin.model.UserDetailsModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

public class User_View_DetailsFragment extends Fragment implements View.OnClickListener {
    private ProcessingDialog processingDialog;
    FragmentUserViewDetailsBinding binding;
    Bundle bundle = this.getArguments();
    ArrayList<UserDetailsModel> userDetailsModel = new ArrayList<UserDetailsModel>();
     AlertDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          processingDialog=new ProcessingDialog(getActivity());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getUserList(this.getArguments().getString("user_id"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user__view__details, container, false);
        getUserList(this.getArguments().getString("user_id"));
        binding.edit.setOnClickListener(this);
        binding.call.setOnClickListener(this);
        binding.renew.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getUserList(this.getArguments().getString("user_id"));
    }

    private void getUserList(String id) {
         processingDialog.show("Wait for details...");
        Map<String, String> map = new HashMap();
        map.put("id", id);
        Call<List<UserDetailsModel>> call = APIClient.getInstance().get_User(map);
        call.enqueue(new Callback<List<UserDetailsModel>>() {
            @Override
            public void onResponse(Call<List<UserDetailsModel>> call, retrofit2.Response<List<UserDetailsModel>> response) {
                Log.d("userget", "msg" + new Gson().toJson(response.body()));
                //  Toast.makeText(getContext(), "userget", Toast.LENGTH_SHORT).show();
                processingDialog.dismiss();
                try {
                    userDetailsModel.addAll(response.body());
                    Log.v(TAG, "LOGS" + userDetailsModel.size());
                    for (int i = 0; i < userDetailsModel.size(); i++) {
                        binding.id.setText(userDetailsModel.get(i).getId());
                        binding.name.setText(userDetailsModel.get(i).getName());
                        binding.email.setText(userDetailsModel.get(i).getEmail());
                        binding.mobile.setText(userDetailsModel.get(i).getMobile());
                        binding.password.setText(userDetailsModel.get(i).getPassword());
                        binding.createdAt.setText(userDetailsModel.get(i).getCreatedAt());
                        binding.expireDate.setText(userDetailsModel.get(i).getValid());
                        binding.status.setText(userDetailsModel.get(i).getStatus());
                        binding.type.setText(userDetailsModel.get(i).getType());
                        if (userDetailsModel.get(i).getLogin().equals("1")) {
                            binding.login.setText("Yes");
                        } else {
                            binding.login.setText("No");
                        }

                        if (userDetailsModel.get(i).getStatus().equals("active")) {

                            binding.status.setTextColor(Color.parseColor("#0c7800"));
                        } else {

                            binding.status.setTextColor(Color.parseColor("#f90404"));
                        }

                    }

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<List<UserDetailsModel>> call, Throwable t) {
                Log.d("erere", t.toString());
                processingDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit) {
            //Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
           /* AppCompatActivity activity = (AppCompatActivity) v.getContext();
            UpdateEdit_UserFragment myFragment = new UpdateEdit_UserFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", binding.id.getText().toString());
            bundle.putString("name", binding.name.getText().toString());
            bundle.putString("email", binding.email.getText().toString());
            bundle.putString("mobile", binding.mobile.getText().toString());
            bundle.putString("password", binding.password.getText().toString());
            bundle.putString("register", binding.createdAt.getText().toString());
            bundle.putString("expire", binding.expireDate.getText().toString());
            bundle.putString("login", binding.login.getText().toString());
            myFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, myFragment)
                    .addToBackStack(null).commit();*/

            NavHostFragment navHostFragment = (NavHostFragment)getActivity().getSupportFragmentManager().getPrimaryNavigationFragment();
            FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
            Fragment loginFragment = fragmentManager.getPrimaryNavigationFragment();
            Bundle bundle=new Bundle();
            bundle.putString("id", binding.id.getText().toString());
            bundle.putString("name", binding.name.getText().toString());
            bundle.putString("email", binding.email.getText().toString());
            bundle.putString("mobile", binding.mobile.getText().toString());
            bundle.putString("password", binding.password.getText().toString());
            bundle.putString("register", binding.createdAt.getText().toString());
            bundle.putString("expire", binding.expireDate.getText().toString());
            bundle.putString("login", binding.login.getText().toString());
            NavHostFragment.findNavController(loginFragment).navigate(R.id.user_edit,bundle);




        } else if (v.getId() == R.id.call) {
            Toast.makeText(getContext(), "call", Toast.LENGTH_SHORT).show();
            String phone = binding.mobile.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        } else if (v.getId() == R.id.renew) {
            Toast.makeText(getContext(), "renew", Toast.LENGTH_SHORT).show();
            displayDialogWindow();

        }
    }

    public void displayDialogWindow() {
        final AlertDialog.Builder loginDialog = new AlertDialog.Builder(getActivity());
        loginDialog.setCancelable(false);
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View f = factory.inflate(R.layout.customrenew, null);

        final EditText amount = (EditText) f.findViewById(R.id.amount);
        final EditText days = (EditText) f.findViewById(R.id.days);
        TextView Back =  f.findViewById(R.id.button_Back);
        TextView Go = f.findViewById(R.id.button_Go);
        loginDialog.setView(f);
        dialog = loginDialog.create();
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getActivity().finish();
                dialog.dismiss();
            }
        });

        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Enter Amount", Toast.LENGTH_SHORT).show();
                }
                else if (days.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Enter Days", Toast.LENGTH_SHORT).show();
                }
                else
                {
                     reNewUser(amount.getText().toString(),days.getText().toString());
                }
            }
        });
        loginDialog.setCancelable(false);
        dialog.show();

    }

    private void reNewUser(String amount,String days)

        {
            processingDialog.show("wait for user...");
            Map<String,String> map=new HashMap();
            map.put("id",userDetailsModel.get(0).getId());
            map.put("mobile",userDetailsModel.get(0).getMobile());
            map.put("email",userDetailsModel.get(0).getEmail());
            map.put("amount",amount);
            map.put("days",days);
            Call<JsonObject> call = APIClient.getInstance().renew_User(map);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    processingDialog.dismiss();
                    Log.d("deletenews", response.body()+"");
                    String jsonObject=new Gson().toJson(response.body());
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject);
                        if (jsonObject1.getInt("status")==200)
                        {
                            Toast.makeText(getContext(), jsonObject1.getString("result"), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            getUserList(getArguments().getString("user_id"));
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getContext(), jsonObject1.getString("result"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                  }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("erere",t.toString());
                    processingDialog.dismiss();

                }
            });
        }



}
