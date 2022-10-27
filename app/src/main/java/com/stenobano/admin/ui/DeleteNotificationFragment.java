package com.stenobano.admin.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.stenobano.admin.Interface.Delete_Noti;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.Delete_Notification_Adapter_Fcm;
import com.stenobano.admin.databinding.FragmentDeleteNotificationBinding;
import com.stenobano.admin.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class DeleteNotificationFragment extends Fragment implements Delete_Noti {
    DatabaseReference reference;
    private List<Chat> mChat;
    Delete_Notification_Adapter_Fcm adapter_fcm;
    FragmentDeleteNotificationBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_delete_notification, container, false);
        readMessage();
        return binding.getRoot();
    }

    private void readMessage()
    {
        mChat=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Notification").child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String title=(String) snapshot.child("title").getValue();
                    String description=(String) snapshot.child("description").getValue();
                    String date=(String) snapshot.child("date").getValue();
                    String key=(String) snapshot.getKey();

                    //Toast.makeText(DeleteNotification.this, ""+title+":"+description+":"+date, Toast.LENGTH_LONG).show();
                    // Chat chat=snapshot.getValue(Chat.class);
                    Chat chat=new Chat();
                    chat.setTitle(title);
                    chat.setDescription(description);
                    chat.setDate(date);
                    chat.setKey(key);
                    mChat.add(chat);
                }
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
                linearLayoutManager.setStackFromEnd(true);
                binding.recyclerView.setLayoutManager(linearLayoutManager);
                adapter_fcm=new Delete_Notification_Adapter_Fcm(mChat,getContext(),DeleteNotificationFragment.this);
                binding.recyclerView.setAdapter(adapter_fcm);
                adapter_fcm.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private  void deleteNotoficationMesg(String key)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notification");
        Query applesQuery = ref.child("Chats").child(key);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getContext(), "onCancelled", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void deleteNotification(final String key) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("Are you want to ure delete Notification?");
        builder.create();
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNotoficationMesg(key);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}