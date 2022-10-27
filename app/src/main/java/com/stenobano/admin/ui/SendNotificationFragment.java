package com.stenobano.admin.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.stenobano.admin.R;
import com.stenobano.admin.databinding.FragmentSendNotificationBinding;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class SendNotificationFragment extends Fragment implements View.OnClickListener{
    FragmentSendNotificationBinding binding;
    private ProcessingDialog processingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_send_notification, container, false);
        processingDialog=new ProcessingDialog(getActivity());
        binding.btnNotification.setOnClickListener(this);
        binding.deleteNotification.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_notification)
        {
            if (binding.title.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Enter Title", Toast.LENGTH_SHORT).show();
            }

            else if (binding.description.getText().toString().equals(""))
            {
                Toast.makeText(getContext(), "Enter description", Toast.LENGTH_SHORT).show();
            }
            else {
                saveNotification();
            }
        }
        else  if (v.getId()==R.id.delete_notification)
        {
            alertDelete();
        }
    }
    public String giveDate() {
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM-yy ,H:m:s");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        return thisDate;
    }
    private  void send()
    {
        send_notification();

    }

    private void send_notification() {
            processingDialog.show("wait...");
            Map<String,String> map=new HashMap();
              map.put("body",binding.title.getText().toString());
             map.put("title", binding.description.getText().toString());
            Call<String> call = APIClient.getInstance().sendNotification(map);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    processingDialog.dismiss();
                    Log.d("sendnotification", "msg" + new Gson().toJson(response.body()));
                    Toast.makeText(getContext(), ""+response.body(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("erere",t.toString());
                    processingDialog.dismiss();

                }
            });
    }


    private  void saveNotification()
    {
        send();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Notification");
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("description",binding.description.getText().toString());
        hashMap.put("title",binding.title.getText().toString());
        hashMap.put("date",giveDate());
        reference.child("Chats").push().setValue(hashMap);
        reference =FirebaseDatabase.getInstance().getReference("Notification");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    private  void deleteNotofication()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notification");
        Query applesQuery = ref.child("Chats");

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Toast.makeText(getContext(), "All Notification has been deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getContext(), "onCancelled", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void alertDelete()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("Are you want to delete all notification?");
        builder.create();
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNotofication();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}