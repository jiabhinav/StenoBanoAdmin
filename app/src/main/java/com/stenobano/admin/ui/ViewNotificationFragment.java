package com.stenobano.admin.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.Notification_Adapter_Fcm;
import com.stenobano.admin.databinding.FragmentViewNotificationBinding;
import com.stenobano.admin.model.Chat;
import com.stenobano.admin.other_class.ProcessingDialog;

import java.util.ArrayList;
import java.util.List;

public class ViewNotificationFragment extends Fragment {
    FragmentViewNotificationBinding binding;
    private ProcessingDialog processingDialog;
    private List<Chat> mChat;
    Notification_Adapter_Fcm adapter_fcm;
    DatabaseReference reference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_view_notification, container, false);
        readMessage();
        return binding.getRoot();
    }
    private void readMessage()
    {
        mChat=new ArrayList<>();
        final ProgressDialog dialog=new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        reference= FirebaseDatabase.getInstance().getReference("Notification").child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    //Toast.makeText(ResultNotification.this, ""+snapshot.getKey(), Toast.LENGTH_SHORT).show();
                    Chat chat=snapshot.getValue(Chat.class);
                    mChat.add(chat);
                }
                dialog.dismiss();
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
                linearLayoutManager.setStackFromEnd(true);
                binding.recyclerView.setLayoutManager(linearLayoutManager);
                adapter_fcm=new Notification_Adapter_Fcm(mChat,getContext());
                binding.recyclerView.setAdapter(adapter_fcm);
                adapter_fcm.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });

    }
}