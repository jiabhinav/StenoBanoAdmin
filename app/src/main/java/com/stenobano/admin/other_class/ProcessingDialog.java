package com.stenobano.admin.other_class;

import android.app.AlertDialog;
import android.content.Context;


import com.stenobano.admin.R;

import dmax.dialog.SpotsDialog;

public class ProcessingDialog
{
    Context context;
    String mesg;
    static AlertDialog progressDialog;

    public ProcessingDialog(Context context)
    {
        this.context=context;
        this.mesg="";
    }


    public void show(String mesg)
    {
        progressDialog=new SpotsDialog(context, R.style.CustomDialog);
        progressDialog.setMessage(mesg);
        progressDialog.show();
    }

    public void dismiss()
    {
        progressDialog.cancel();
    }


}
