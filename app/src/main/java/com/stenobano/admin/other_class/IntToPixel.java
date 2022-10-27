package com.stenobano.admin.other_class;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by K6 on 1/5/2018.
 */

public class IntToPixel {
    public static float getDP(int pixel, Resources resources) {
        Resources r = resources;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, r.getDisplayMetrics());
    }

    public static int[] screenHight(Activity getActivity) {
        int[] height_width = new int[2];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height_width[0] = displayMetrics.widthPixels;
        height_width[1] = displayMetrics.heightPixels;
        return height_width;
    }
    public static String timeChange(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(calendar.getTime());
    }

    public static String timeChange1(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        return df.format(calendar.getTime());
    }

    public static String timeChangeFull(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    public static String EEE_dd_MMM_yyy(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yy");
        return df.format(calendar.getTime());
    }

    public static String yyyy_MM_dd(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(calendar.getTime());
    }

    public static String datetime(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        return df.format(calendar.getTime());
    }
    public static String datetime1(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");
        return df.format(calendar.getTime());
    }

    public static String datetime2(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        return df.format(calendar.getTime());
    }

    public static String datetime3(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("d MMM yy");
        return df.format(calendar.getTime());
    }

    public static String datetime4(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("MMM yy");
        return df.format(calendar.getTime());
    }
    public static String timeAndDate(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a, dd MMMM");

        return df.format(calendar.getTime());
    }

    public static String dateAndDay(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat( "dd EEE");

        return df.format(calendar.getTime());
    }

    public static String datetimeStamp(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yy hh:mm a");
        return df.format(calendar.getTime());
    }

    public static String DateFormater(String date) {
        SimpleDateFormat WronrFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat Format = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat day = new SimpleDateFormat("EEE");
        SimpleDateFormat month = new SimpleDateFormat("MMM yy");

        try {
            date = Format.format(WronrFormat.parse(date));
        } catch (Exception e) {
            Log.d("EXCEPTION DATE", "" + e);
        }
        return date;
    }

    public static String Month(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("MMMM yy");
        return df.format(calendar.getTime());
    }

    public static String Year(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return df.format(calendar.getTime());
    }
    public static String Year2(Calendar calendar) {
        SimpleDateFormat df = new SimpleDateFormat("yy");
        return df.format(calendar.getTime());
    }

    public static String date_month(String date, String formatneeded) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat right = new SimpleDateFormat(formatneeded);
            date = right.format(df.parse(date));
        } catch (Exception e) {

        }
        return date;
    }

    public static String YourDate(String result, String oldFormat, String newFormat) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(oldFormat);
            SimpleDateFormat right = new SimpleDateFormat(newFormat);
            return right.format(df.parse(result));
        } catch (Exception e) {
            return result;
        }
    }

    public static long createRandomInteger(int aStart, long aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = aEnd - (long) aStart + 1;
        //logger.info("range>>>>>>>>>>>"+range);
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        //logger.info("fraction>>>>>>>>>>>>>>>>>>>>"+fraction);
        long randomNumber = fraction + (long) aStart;
        //logger.info("Generated : " + randomNumber);
        return randomNumber;
    }

    public static String getAddress(float MyLat, float MyLong, Context context) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
//            String cityName = addresses.get(0).getAddressLine(0);
            String cityName = addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
//            String stateName = addresses.get(0).getAddressLine(1);
//            String countryName = addresses.get(0).getAddressLine(2);
            return " near " + cityName;
        } catch (Exception e) {
//            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    public static void hideSoftKey(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view=activity.getCurrentFocus();
        if (view==null){
            view=new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public static SQLiteDatabase getDatabase(Context context){
        return context.openOrCreateDatabase("SalesKormoan", Context.MODE_PRIVATE,null);
    }
    public static String toArrayRep(ArrayList in) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < in.size(); i++) {
            if (i != 0) {
                result.append(",");
            }
            result.append("'" + in.get(i) + "'");
        }
        return result.toString();
    }


    public static String getTimeDifference(String currentDate, Cursor cursor, String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(currentDate);
            Date date2 = sdf.parse(cursor.getString(cursor.getColumnIndex("dueDate")));

            long difference = date1.getTime() - date2.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String dayDifference = (Long.toString(differenceDates).equals("1")) ? "Yesterday" : Long.toString(differenceDates) + " days ago";


            if (difference > 0)
                return removeAt12(getStringProper(cursor.getString(cursor.getColumnIndex("dueDate")), cursor.getString(cursor.getColumnIndex("owner")))) + " - " + dayDifference;
            else
                return removeAt12(getStringProper(cursor.getString(cursor.getColumnIndex("dueDate")), cursor.getString(cursor.getColumnIndex("owner"))));
        } catch (ParseException e) {
            return null;
        }
    }

    public static String removeAt12(String s) {
        String time = "At 12:00 AM b";
        if (s.contains(time)) {
            return s.replaceAll(time, "B");
        } else {
            return s;
        }
    }
    public static String getStringProper(String dueDate, String owner) {
        String s = "At " + YourDate(dueDate, "yyyy-MM-dd HH:mm:ss", "hh:mm aa");
        s += " by " + owner;
        return s;
    }

    private static char[] c = new char[]{'k', 'm', 'b', 't'};


    public static String coolFormat(double n, int iteration) {
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration])
                : coolFormat(d, iteration+1));

    }

    public static String convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();

            Log.e("Byte array", ">" + byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

}
