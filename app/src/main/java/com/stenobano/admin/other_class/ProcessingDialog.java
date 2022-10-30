package com.stenobano.admin.other_class;

import android.app.AlertDialog;
import android.content.Context;


import com.stenobano.admin.R;

import dmax.dialog.SpotsDialog;

public class ProcessingDialog
{
    Context context;
    static SpotsDialog progressDialog;

    public ProcessingDialog(Context context)
    {
        this.context=context;
    }


    public void show(String mesg)
    {
        progressDialog=new SpotsDialog(context,mesg, R.style.CustomDialog);
       /* if (mesg!=null)
        {
            progressDialog.setMessage(mesg);
        }
        else
        {
            progressDialog.setMessage("Loading");
        }*/

        progressDialog.show();
    }

    public void dismiss()
    {
        progressDialog.cancel();
    }


}
