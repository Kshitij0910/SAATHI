package com.example.saathi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MedicinesViewFragment extends Fragment implements UploadAdapter.OnItemClickListener, TimePickerDialog.OnTimeSetListener {



    private RecyclerView mRecyclerView;
    private UploadAdapter mAdapter;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUpload;

    private ValueEventListener mDBListener;

    private ProgressBar loadUpload;
    FirebaseAuth fAuth;

    FloatingActionButton notification, notificationCancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_view_medicines, container, false);




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notification=view.findViewById(R.id.notification);
        notificationCancel=view.findViewById(R.id.notification_cancel);

        mRecyclerView = view.findViewById(R.id.recycler_view);

        loadUpload = view.findViewById(R.id.load_upload);

        fAuth = FirebaseAuth.getInstance();

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUpload = new ArrayList<>();

        mAdapter = new UploadAdapter(getActivity(), mUpload);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(MedicinesViewFragment.this);

        mStorage=FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Prescription/users/" + fAuth.getCurrentUser().getUid());

        mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUpload.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUpload.add(upload);


                }
                mAdapter.notifyDataSetChanged();

                loadUpload.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadUpload.setVisibility(View.INVISIBLE);
            }
        });





        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newTimeFragment=new TimePickerFragment();
                newTimeFragment.setTargetFragment(MedicinesViewFragment.this, 0);
                newTimeFragment.show(getActivity().getSupportFragmentManager(), "timePicker");


            }
        });

        notificationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(),"Tap and Hold for further actions!", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem=mUpload.get(position);
        final String selectedKey=selectedItem.getKey();

        StorageReference imgRef=mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(getActivity(), "Your Medicine has been deleted successfully!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        Toast.makeText(getActivity(), "REMINDER SET FOR: "+DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()), Toast.LENGTH_SHORT).show();

        startAlarm(c);
    }


    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void cancelAlarm(){
        AlarmManager alarmManager=(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(getActivity(), "REMINDER CANCELLED!", Toast.LENGTH_SHORT).show();



    }






}

