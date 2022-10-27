package com.stenobano.admin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stenobano.admin.MainActivity;
import com.stenobano.admin.R;
import com.stenobano.admin.databinding.ActivityLoginBinding;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.session.SesssionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityLoginBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        SesssionManager sesssionManager=new SesssionManager(getApplicationContext());
        try {
            if (sesssionManager.userID().equals("")) {
                Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("a", "0");
                startActivity(i);
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        // setContentView(R.layout.activity_login);
        binding.btnLogin.setOnClickListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

    }

    private void login() {

        if (!isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        } else {

            if (binding.editTextMobileEmail.getText().toString().equals("")) {
                Toast.makeText(this, "Enter Email address", Toast.LENGTH_SHORT).show();
            } else if (binding.editTextPass.getText().toString().equals(" ")) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Login...", true);
                progressDialog.setCancelable(false);

                    RequestBody email = RequestBody.create(MultipartBody.FORM, binding.editTextMobileEmail.getText().toString());
                    RequestBody password = RequestBody.create(MultipartBody.FORM, binding.editTextPass.getText().toString());
                    RequestBody key = RequestBody.create(MultipartBody.FORM, "wdffdfdf5434664bdfqwg");

                    Call<JsonObject> call = APIClient.getInstance().loginAdmin(email,password,key);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                            progressDialog.dismiss();
                            Log.d("upload", "msg" + new Gson().toJson(response.body()));
                            try {
                                JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                                Log.d("upload", "reha");
                                if(jsonObject.getInt("status")==200){
                                    Log.d("upload", "reha1");
                                    JSONArray array = jsonObject.getJSONArray("result");
                                    Log.d("upload", "reha2");
                                        JSONObject jsonObject1 = array.getJSONObject(0);
                                        SesssionManager sesssionManager=new SesssionManager(LoginActivity.this);
                                    sesssionManager.sessionLogin(jsonObject1.getString("id"),jsonObject1.getString("name"),
                                            jsonObject1.getString("email"), jsonObject1.getString("mobile"), jsonObject1.getString("status"),jsonObject1.getString("image"));
                                    Log.d("upload", "reha3");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    Toast.makeText(LoginActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onFailure: "+t.getMessage());
                            progressDialog.dismiss();

                        }
                    });
                }


        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnLogin)
        {
            login();
        }
    }
}