package com.stenobano.admin.other_class;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class TimePickers extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {
    Context context;
    TextView textView;
    public TimePickers(Context context, TextView textView) {
        this.context=context;
        this.textView=textView;
    }

    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(),this,hour,minute,false);
        }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("dateisis", "onDateSet: "+hourOfDay + "-" + minute + "");
        try {
            textView.setText(hourOfDay+":"+ minute);
        }
        catch (Exception e)
        {

        }
    }

}