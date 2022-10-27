package com.stenobano.admin.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stenobano.admin.R;
import com.stenobano.admin.databinding.FragmentUpdateEditUserBinding;
import com.stenobano.admin.model.UserDetailsModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

public class UpdateEdit_UserFragment extends Fragment implements View.OnClickListener{

FragmentUpdateEditUserBinding binding;
    private ProcessingDialog processingDialog;
    String user="update";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processingDialog=new ProcessingDialog(getActivity());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_update_edit__user, container, false);
        binding.name.setText( this.getArguments().getString("name"));
        binding.email.setText( this.getArguments().getString("email"));
        binding.mobile.setText( this.getArguments().getString("mobile"));
        binding.password.setText( this.getArguments().getString("password"));
        binding.register.setText( this.getArguments().getString("register"));
        binding.expire.setText( this.getArguments().getString("expire"));
        String log= this.getArguments().getString("login");
        if (log.equals("yes"))
        {
            binding.login.setText("1");
        }
        else
        {
            binding.login.setText("0");
        }
        String st[]={"active","deactive"};
        String tp[]={"user"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,st);
        binding.status.setAdapter(adapter);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,tp);
        binding.type.setAdapter(adapter2);
        binding.newUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    user="user";
                    // Toast.makeText(Update_Edit_User.this, ""+user, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    user="update";
                    //  Toast.makeText(Update_Edit_User.this, ""+user, Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.edit.setOnClickListener(this);
        return binding.getRoot();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.edit)
        {
            if (binding.name.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Enter name", Toast.LENGTH_SHORT).show();
            }
            else if (binding.email.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();

            }
            else if (binding.mobile.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Enter mobile", Toast.LENGTH_SHORT).show();

            }
            else if (binding.password.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();

            }
            else if (binding.register.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Enter register", Toast.LENGTH_SHORT).show();

            }
            else if (binding.expire.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Enter expire date", Toast.LENGTH_SHORT).show();

            }
            else if (!binding.login.getText().toString().equals("1")&& !binding.login.getText().toString().equals("0"))
            {
                Toast.makeText(getContext(), "Enter 0 or 1 Value for login status ", Toast.LENGTH_SHORT).show();

            }

            else
            {
                userUpdate(this.getArguments().getString("id")
                );
            }

        }
    }

    private void  userUpdate(String id)
    {
         processingDialog.show("wait for user...");
        Map<String,String> map=new HashMap();
        map.put("id", id);
        map.put("user", user);
        map.put("name", binding.name.getText().toString());
        map.put("mobile", binding.mobile.getText().toString());
        map.put("email", binding.email.getText().toString());
        map.put("password",binding.password.getText().toString());
        map.put("register", binding.register.getText().toString());
        map.put("expiry_date", binding.expire.getText().toString());
        map.put("status",binding.status.getSelectedItem().toString());
        map.put("type",binding.type.getSelectedItem().toString());
        map.put("login",binding.login.getText().toString());
        Call<String> call = APIClient.getInstance().userUpdate(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.d("userget", "msg" + new Gson().toJson(response.body()));
               Toast.makeText(getContext(), "Update succesfully", Toast.LENGTH_SHORT).show();
                processingDialog.dismiss();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();

            }
        });
    }
}