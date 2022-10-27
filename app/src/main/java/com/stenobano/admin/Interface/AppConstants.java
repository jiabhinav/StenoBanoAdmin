package com.stenobano.admin.Interface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public interface AppConstants {
    int ACTIVITY_RESULT = 1001, ACTIVITY_FINISH = 1002,
            GALLERY = 111, CAMERA = 112, CROP = 113;

    String SHOW_ERROR = "-1", HIDE_ERROR = "-1", SUCCESS_1 = "1", SUCCESS_0 = "0", SUCCESS_TRUE = "true", SUCCESS_UNKNOWN = "Whoops! Unknown sucess value";
    String WORK_IN_PROGRESS = "WORK IN PROGRESS";
    String UNEXPEXTED_ERROR = "Whoops! Something is happen unexpectedly. Please try again.";
    String UNEXPECTED_RESPONSE = "Whoops! Something is happen unexpectedly. Response is not in proper format.";
    String PARSING_ERROR = "Whoops! Something is happen unexpectedly. Exception in data parsing.";
    String EXCEPTION = "Whoops! Something is happen unexpectedly. Exception in data processing.";




    String SETTINGS_SERVICE_NAME_STORE = "store/settings";
    String DASHBOARD_SERVICE_NAME = "store/dashboard_list";
    String PROFILE_INFO = "store/profile_info";

    /* SimpleDateFormat */
    SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss", Locale.getDefault());

    SimpleDateFormat MMM_DD_YYYY_HH_MM_AM_PM = new SimpleDateFormat(
            "MMMdd yyyy,hh:mm a", Locale.getDefault());
    SimpleDateFormat DD_MMM_YYYY = new SimpleDateFormat(
            "dd MMM yyyy", Locale.getDefault());
    String IMAGE_DIRECTORY = "/DCIM/PICTURES";
    String IMAGE_DIRECTORY_CROP = "/DCIM/CROP_PICTURES";
    String VIDEO_DIRECTORY = "/DCIM/VIDEOS";




}
