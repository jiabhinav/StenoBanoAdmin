package com.stenobano.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;


import static com.stenobano.admin.config.BaseUrl.IMAGE;
import static com.stenobano.admin.config.BaseUrl.MY_SESSION;
import static com.stenobano.admin.config.BaseUrl.SP;
import static com.stenobano.admin.config.BaseUrl.SP_MY_SESSION;
import static com.stenobano.admin.config.BaseUrl.SP_SCHOOL_INFO;
import static com.stenobano.admin.config.BaseUrl.STUDENT_SESSION;


public class SesssionManager {
    SharedPreferences sp_login,sp_school,my_session,student_session;
    SharedPreferences.Editor sp_editor,school_editor,my_session_editor,edit_student_session;
    Context context;
    int PRIVATE_MODE = 0;
    int bal=0;
    String student_name;
    public SesssionManager(Context context) {
        this.context = context;
        sp_login = context.getSharedPreferences(SP, PRIVATE_MODE);
        sp_editor = sp_login.edit();

        sp_school = context.getSharedPreferences(SP_SCHOOL_INFO, PRIVATE_MODE);
        school_editor = sp_school.edit();
        my_session = context.getSharedPreferences(SP_MY_SESSION, PRIVATE_MODE);
        my_session_editor = my_session.edit();


        student_session = context.getSharedPreferences(STUDENT_SESSION, PRIVATE_MODE);
        edit_student_session = student_session.edit();

    }








    public void setMySession(String date)
    {
        my_session_editor.putString(MY_SESSION,date);
        my_session_editor.commit();
    }
    public String getMySession() {
        return my_session.getString(MY_SESSION, "");
    }


    public void setImageUri(Uri imageUri) {
        sp_editor.putString("getImageUri", imageUri.toString());
        sp_editor.commit();
    }

    public void setCropImagePath(String cropImagePath) {
        sp_editor.putString("getCropImagePath", cropImagePath);
        sp_editor.commit();
    }

    public String getCropImagePath() {
        return sp_login.getString("getCropImagePath", "");
    }


    public String getImagePath() {
        return sp_login.getString("getImagePath", "");
    }

    public void setImagePath(String imagePath) {

        sp_editor.putString("getImagePath", imagePath);
        sp_editor.commit();
    }

    public Uri getImageUri() {
        String imageUri = sp_login.getString("getImageUri", "");
        if (imageUri == null || imageUri.equals(""))
            return null;
        return Uri.parse(imageUri);
    }


     public String getImage()
    {
        return sp_login.getString(IMAGE, null);
    }


}
