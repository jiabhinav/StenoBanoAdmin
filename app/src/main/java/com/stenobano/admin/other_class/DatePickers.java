package com.stenobano.admin.other_class;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class DatePickers extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    Context context;
    TextView textView;
    public DatePickers(Context context, TextView textView) {
        this.context=context;
        this.textView=textView;
    }

    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
        }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Log.d("dateisis", "onDateSet: "+year + "-" + month+1 + "-" + dayOfMonth+"");
        try {
            textView.setText(year + "-" + month+1 + "-" + dayOfMonth);
        }
        catch (Exception e)
        {

        }

    }
}