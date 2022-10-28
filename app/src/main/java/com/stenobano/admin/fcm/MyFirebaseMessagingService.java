package com.stenobano.admin.fcm;

import static androidx.core.app.NotificationCompat.BADGE_ICON_SMALL;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.stenobano.admin.MainActivity;
import com.stenobano.admin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    // private Handler handler;
    boolean isCalling = false;
    private MediaPlayer mp;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("FCM TOKEN", s);
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

      /*      RemoteMessage.Notification notification = (RemoteMessage.Notification) remoteMessage.getData();
        Log.d("receivedMessagis", "onMessageReceived:=="+ new Gson().toJson(notification));
        try {
            JSONObject jsonObject=new JSONObject(notification.getBody());
            JSONObject body=jsonObject.getJSONObject("data");
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        //sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        sendNotificationOnCAll(remoteMessage.getData());


    }

    private void sendNotification(String title, String body) {
        try {
            //   ShortcutBadger.applyCount(getApplicationContext(), count);'
            mp = MediaPlayer.create(getApplicationContext(), R.raw.notification);
            mp.setLooping(false);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
       // Bundle b = new Bundle();
        //b.putParcelable("data", response);
        //intent.putExtras(b);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = null;
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
//      notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.stenobano_icon);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            notificationBuilder.setColor(getResources().getColor(R.color.transparent_color));
            notificationBuilder.setLargeIcon(bitmap);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        try {
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText(body);
        } catch (Exception e) {
            e.printStackTrace();
        }

        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setLights(Color.WHITE, 1000, 5000);
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        notificationBuilder.setVibrate(pattern);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        notificationBuilder.setBadgeIconType(BADGE_ICON_SMALL) ;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        notificationBuilder.setChannelId(CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.enableLights(true);
            mChannel.enableVibration(true);

            mChannel.setShowBadge(true);
            mChannel.setSound(defaultSoundUri, attributes);
            mChannel.setLightColor(Color.WHITE);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(uniqueInt, notificationBuilder.build());
    }

    private void sendNotificationOnCAll(Map<String,String> data) {

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        //int uniqueInt = (int) (12345 & 0xfffffff);
        Log.d("vdvdvdvdvv", "sendNotification: "+data.get("token")+"==="+data.get("channel"));
        String CHANNEL_ID = "my_channel_01";
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), uniqueInt, intent,PendingIntent.FLAG_MUTABLE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            notificationBuilder.setColor(getResources().getColor(R.color.black));
            notificationBuilder.setLargeIcon(bitmap);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        try {
            notificationBuilder.setContentTitle(data.get("title"));
            notificationBuilder.setContentText(data.get("body"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setLights(Color.WHITE, 1000, 5000);
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        notificationBuilder.setVibrate(pattern);

        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("body")));
        notificationBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE) ;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        // notificationBuilder.setChannelId(CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.enableLights(true);
            mChannel.enableVibration(true);

            mChannel.setShowBadge(true);
            mChannel.setSound(defaultSoundUri, attributes);
            mChannel.setLightColor(Color.WHITE);

            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(uniqueInt, notificationBuilder.build());
    }




    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }





}

