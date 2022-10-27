package com.stenobano.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stenobano.admin.activity.LoginActivity;
import com.stenobano.admin.chat.Inbox_F;
import com.stenobano.admin.config.BaseUrl;
import com.stenobano.admin.config.Constant;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.session.SesssionManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.stenobano.admin.session.SesssionManager.ID;
import static com.stenobano.admin.session.SesssionManager.MOBILE;
import static com.stenobano.admin.session.SesssionManager.NAME;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    Map<String,String> map;
    private String currentVersion,mobile2,user_id;
    private TextView name, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        map=new HashMap<>();
        map=new SesssionManager(this).getUserDetails();
        BaseUrl.SCHOOL_ID="01012021";
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String name2 =map.get(NAME);
        mobile2 = map.get(MOBILE);
        user_id = map.get(ID);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        name = navigationView.getHeaderView(0).findViewById(R.id.name);
        mobile = navigationView.getHeaderView(0).findViewById(R.id.mobile);
        name.setText(name2);
        mobile.setText(mobile2);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        //,R.id.nav_upload_audio,R.id.nav_adddictionary,R.id.nav_editdictionary,R.id.nav_uploaded_file,R.id.nav_user_list,R.id.news_details,R.id.purchae_amount,
        //                R.id.send_notification,R.id.user_request,R.id.view_notification,R.id.delete_notification,R.id.reset_PinFragment,R.id.logout_Fragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getToken();
        screenwidth_and_Heights();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.chat)
        {
            /*Inbox_F frag = new Inbox_F();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.drawer_layout,frag,"Inbox");
            transaction.addToBackStack(null);
            transaction.commit();*/
           // ProductListDirections.ViewProductDetails directions = ProductListDirections.navigateToProductDetail(product.getId());
           // NavHostFragment.findNavController(new Inbox_F()).navigate(R.id.nav_inbox);

            NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().getPrimaryNavigationFragment();
            FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
            Fragment loginFragment = fragmentManager.getPrimaryNavigationFragment();

            NavHostFragment.findNavController(loginFragment).navigate(R.id.nav_inbox);

        }
        return false;
    }



    private void getToken()
    {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            // Log.w("sucesssss", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token2 = task.getResult().getToken();

                        // Log and toast
                        Log.d("tokenis2", token2);
                        new SesssionManager(MainActivity.this).updateToken(token2);
                        updateToken(token2);
                    }
                });
    }

    private void updateToken(String token)
    {
        RequestBody user_id = RequestBody.create(MultipartBody.FORM, new SesssionManager(this).userID());
        RequestBody tokens = RequestBody.create(MultipartBody.FORM, token);
        RequestBody type = RequestBody.create(MultipartBody.FORM, "admin");
        Call<JsonObject> call = APIClient.getInstance().update_token(user_id,tokens,type);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                 Log.d("tokenuopdate", new Gson().toJson(response.body()));
                try {
                    JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.getInt("status")==200)
                    {
                        new SesssionManager(MainActivity.this).updateToken(token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void screenwidth_and_Heights()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constant.screen_width = displayMetrics.widthPixels;
        Constant.screen_height = displayMetrics.heightPixels;
    }

}