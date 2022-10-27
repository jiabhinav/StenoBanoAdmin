package com.stenobano.admin.chat.Audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.stenobano.admin.R;
import com.stenobano.admin.Sessionmanager;
import com.stenobano.admin.chat.GroupChat_Fragmnet;
import com.stenobano.admin.config.BaseUrl;
import com.stenobano.admin.other_class.GetData;
import com.stenobano.admin.session.SesssionManager;

/**
 * Created by AQEEL on 12/5/2018.
 */


// this class will send a voice message to other user

public class SendAudio {


    DatabaseReference rootref;
    String senderid = "";
    String Receiverid = "";
    String Receiver_name="";
    String Receiver_pic="null";
    Context context;
    String school_id="";


    private static String mFileName = null;
    private MediaRecorder mRecorder = null;

    private DatabaseReference Adduser_to_inbox;

    EditText message_field;


    public SendAudio(Context context, EditText message_field,
                     DatabaseReference rootref, DatabaseReference adduser_to_inbox
            , String senderid, String receiverid, String receiver_name, String receiver_pic, String school_id) {

        this.context=context;
        this.message_field=message_field;
        this.rootref=rootref;
        this.Adduser_to_inbox=adduser_to_inbox;
        this.senderid=senderid;
        this.Receiverid=receiverid;
        this.Receiver_name=receiver_name;
        this.Receiver_pic=receiver_pic;
        mFileName = context.getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.mp3";
        this.school_id=school_id;

    }



    // this function will start the recrding
    private void startRecording() {

        if(mRecorder!=null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder=null;
        }

        mRecorder = new MediaRecorder();

        if(mRecorder!=null)
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        if(mRecorder!=null)
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        if(mRecorder!=null)
            mRecorder.setOutputFile(mFileName);

        if(mRecorder!=null)
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            if(mRecorder!=null)
                mRecorder.prepare();
        } catch (IOException e) {
            Log.e("resp", "prepare() failed");
        }
        if(mRecorder!=null)
            mRecorder.start();


    }



    // stop the recording and then call a function to upload the audio file into database

    public void stopRecording() {
        stop_timer_without_recoder();
        if(mRecorder!=null ) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder=null;
            Runbeep("stop");
            UploadAudio();
        }
    }

    Handler handler;
    Runnable runnable;
    public void Runbeep(final String action){

        // within 700 milisecond the timer will be start
        handler=new Handler();
        if(action.equals("start")) {
            message_field.setText("00:00");
            runnable = new Runnable() {
                @Override
                public void run() {
                    start_timer();
                }
            };

            handler.postDelayed(runnable, 700);
        }


        // this will run a beep sound
        final MediaPlayer beep = MediaPlayer.create(context, R.raw.sound);
        beep.setVolume(100,100);
        beep.start();
        beep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                beep.release();

                // if our action is start a recording the recording will start
                if(action.equals("start"))
                    startRecording();
            }
        });
    }


    // this method will upload audio  in firebase database
    public void UploadAudio(){

        Date c = Calendar.getInstance().getTime();
        final String formattedDate = GetData.df.format(c);

        StorageReference reference= FirebaseStorage.getInstance().getReference();
        DatabaseReference dref=rootref.child("chat").child(school_id).child(senderid+"-"+Receiverid).push();
        final String key=dref.getKey();
        GroupChat_Fragmnet.uploading_Audio_id=key;
        final String current_user_ref = "chat" +"/"+school_id+ "/" + senderid + "-" + Receiverid;
        final String chat_user_ref = "chat" +"/"+school_id+ "/" + Receiverid + "-" + senderid;

        HashMap my_dummi_pic_map = new HashMap<>();
        my_dummi_pic_map.put("receiver_id", Receiverid);
        my_dummi_pic_map.put("sender_id", senderid);
        my_dummi_pic_map.put("chat_id",key);
        my_dummi_pic_map.put("text", "");
        my_dummi_pic_map.put("type","audio");
        my_dummi_pic_map.put("pic_url","none");
        my_dummi_pic_map.put("status", "0");
        my_dummi_pic_map.put("time", "");
        my_dummi_pic_map.put("sender_name", new SesssionManager(context).getName());
        my_dummi_pic_map.put("timestamp", formattedDate);

        HashMap dummy_push = new HashMap<>();
        dummy_push.put(current_user_ref + "/" + key, my_dummi_pic_map);
        rootref.updateChildren(dummy_push);


        Uri uri = Uri.fromFile(new File(mFileName));
        Log.d("mFileName",mFileName);
        Log.d("mFileName",key+".mp3");

        final StorageReference filepath=reference.child("Audio").child(school_id).child(key+".mp3");

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("uriisss",uri.toString());
                        GroupChat_Fragmnet.uploading_Audio_id="none";
                        HashMap message_user_map = new HashMap<>();
                        message_user_map.put("receiver_id", Receiverid);
                        message_user_map.put("sender_id", senderid);
                        message_user_map.put("chat_id",key);
                        message_user_map.put("text", "");
                        message_user_map.put("type","audio");
                        message_user_map.put("pic_url",uri.toString());
                        message_user_map.put("status", "0");
                        message_user_map.put("time", "");
                        message_user_map.put("sender_name", new SesssionManager(context).getName());
                        message_user_map.put("timestamp", formattedDate);

                        HashMap user_map = new HashMap<>();

                        user_map.put(current_user_ref + "/" + key, message_user_map);
                        user_map.put(chat_user_ref + "/" + key, message_user_map);

                        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                String inbox_sender_ref = "Inbox" +"/"+school_id+ "/" + senderid + "/" + Receiverid;
                                String inbox_receiver_ref = "Inbox" +"/"+school_id+ "/" + Receiverid + "/" + senderid;

                                HashMap sendermap=new HashMap<>();
                                sendermap.put("rid",senderid);
                                sendermap.put("name",new SesssionManager(context).getName());
                                sendermap.put("pic",new SesssionManager(context).getImage());
                                sendermap.put("msg","Send an Audio...");
                                sendermap.put("status","0");
                                sendermap.put("date",formattedDate);
                                sendermap.put("timestamp", -1* System.currentTimeMillis());

                                HashMap receivermap=new HashMap<>();
                                receivermap.put("rid",Receiverid);
                                receivermap.put("name",Receiver_name);
                                receivermap.put("pic",Receiver_pic);
                                receivermap.put("msg","Send an Audio...");
                                receivermap.put("status","1");
                                receivermap.put("date",formattedDate);
                                receivermap.put("timestamp", -1* System.currentTimeMillis());

                                HashMap both_user_map = new HashMap<>();
                                both_user_map.put(inbox_sender_ref , receivermap);
                                both_user_map.put(inbox_receiver_ref , sendermap);

                                Adduser_to_inbox.updateChildren(both_user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        GroupChat_Fragmnet.SendPushNotification(new SesssionManager(context).getToken(),GroupChat_Fragmnet.token_reciever);

                                    }
                                });


                            }
                });

                    }
                });
            }
        });

    }



    private void uploadImage(Uri imageUri)
    {
      StorageReference storageReference= FirebaseStorage.getInstance().getReference("Audio").child(school_id);
        if (imageUri!=null)
        {
            final StorageReference fileReference =storageReference.child(System.currentTimeMillis()+
                    "."+"mp3");
            Log.d("dddddwdw",fileReference.toString());
            StorageTask uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw  task.getException();
                    }
                    return fileReference.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Log.d("succresup0load",task.getResult().toString());
                        Uri dowloadUri=task.getResult();
                       /* String mUri=dowloadUri.toString();
                        reference= FirebaseDatabase.getInstance().getReference("Users").child(sp.getString("phone",null));
                        HashMap<String,Object>map=new HashMap<>();
                        map.put("imageURL",mUri);
                        reference.updateChildren(map);*/


                    }
                    else
                    {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else
        {
            Toast.makeText(context, "No image selected!!", Toast.LENGTH_SHORT).show();
        }
    }



    CountDownTimer timer;
    public void start_timer() {

        timer=new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time=30000-millisUntilFinished;

                int min = (int) (time/1000) / 60;
                int sec = (int) (time/1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
                message_field.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                stopRecording();
                message_field.setText(null);
            }
        };

        timer.start();
    }


    // this  will stop timer when audio file have some data
    public void stop_timer(){

        if(mRecorder!=null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder=null;
        }

        if(handler!=null && runnable!=null)
            handler.removeCallbacks(runnable);

        message_field.setText(null);

        if(timer!=null){
            timer.cancel();
            message_field.setText(null);
        }

    }


    // this will stop timer when audio file does not have data
    public void stop_timer_without_recoder(){

        if(handler!=null && runnable!=null)
            handler.removeCallbacks(runnable);

        message_field.setText(null);

        if(timer!=null){
            timer.cancel();
            message_field.setText(null);
        }

    }

}

