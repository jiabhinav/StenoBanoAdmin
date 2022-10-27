package com.stenobano.admin.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.stenobano.admin.MainActivity;
import com.stenobano.admin.activity.LoginActivity;
import com.stenobano.admin.activity.SplashActivity;
import com.stenobano.admin.ui.home.HomeFragment;

import java.util.HashMap;

import static com.stenobano.admin.config.BaseUrl.SP;
import static com.stenobano.admin.config.BaseUrl.TOKEN;


public class SesssionManager {

    SharedPreferences sp_login;
    SharedPreferences.Editor sp_editor;
    Context context;
    int PRIVATE_MODE = 0;
    int bal=0;
    public SesssionManager(Context context) {
        this.context = context;
        sp_login = context.getSharedPreferences(SP, PRIVATE_MODE);
        sp_editor = sp_login.edit();

    }
    public static String ID="id";
    public static String NAME="name";
    public static String EMAIL="email";
    public static String MOBILE="mobile";
    public static String STATUS="active";
    public static String IMAGE="image";

    public void sessionLogin(String id, String name, String email, String mobile, String status,String image)
    {
        sp_editor.putString(ID, id);
        sp_editor.putString(EMAIL, email);
        sp_editor.putString(NAME, name);
        sp_editor.putString(MOBILE, mobile);
        sp_editor.putString(STATUS, status);
        sp_editor.putString(IMAGE, image);
        sp_editor.commit();

    }




    public String getName()
    {
        return sp_login.getString(NAME, null);
    }
    public String getImage()
    {
        return sp_login.getString(IMAGE, "0");
    }
    public String getToken()
    {
        return sp_login.getString(TOKEN, "0");
    }

    public void checkLogin() {

        if (isLoggedIn()==null) {
            Intent loginsucces = new Intent(context, LoginActivity.class);
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(loginsucces);
        }
        else
        {
            Intent loginsucces = new Intent(context, MainActivity.class);
                loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(loginsucces);
            }



    }


    public void updateToken(String token) {
        sp_editor.putString(TOKEN, token);
        sp_editor.apply();

    }

    public String isLoggedIn() {
        return sp_login.getString(ID, null);
    }

    public void logoutSession() {
        sp_editor.clear();
        sp_editor.commit();
        Intent logout = new Intent(context, LoginActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);;
        context.startActivity(logout);
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(ID, sp_login.getString(ID, null));
        user.put(NAME, sp_login.getString(NAME, null));
        user.put(EMAIL, sp_login.getString(EMAIL, null));
        user.put(MOBILE, sp_login.getString(MOBILE, null));
        user.put(STATUS, sp_login.getString(STATUS, null));
        user.put(IMAGE, sp_login.getString(IMAGE, null));
        return user;
    }


    public String userID()
    {
        return sp_login.getString(ID, null);
    }
    public String userMobile()
    {
        return sp_login.getString(MOBILE, null);
    }
    public String userEmail()
    {
        return sp_login.getString(EMAIL, null);
    }





    public void updateKYCStatus(String kyc) {
        //sp_editor.putString(KYC, kyc);
        sp_editor.apply();

    }

    public void updateImage(String image) {
      //  sp_editor.putString(IMAGE, image);
        sp_editor.apply();

    }
    public void updateKYC() {
       // sp_editor.putString(KYC, "0");
        sp_editor.apply();

    }
    public void updateUserId(String id, String status) {
        sp_editor.putString(ID, id);
      //  sp_editor.putString(STATUS, status);
        sp_editor.commit();
    }


}
