package com.example.saathi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MedicinesViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private UploadAdapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUpload;
    private List<Prescription> mPrescription;
    private ProgressBar loadUpload;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_view_medicines, container, false);

        mRecyclerView=view.findViewById(R.id.recycler_view);

        loadUpload=view.findViewById(R.id.load_upload);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUpload=new ArrayList<>();
        mPrescription=new ArrayList<>();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Prescription/users/");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Upload upload=postSnapshot.getValue(Upload.class);
                    mUpload.add(upload);
                    Prescription prescription=postSnapshot.getValue(Prescription.class);
                    mPrescription.add(prescription);

                }
                mAdapter=new UploadAdapter(getActivity(), mUpload, mPrescription);
                mRecyclerView.setAdapter(mAdapter);
                loadUpload.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadUpload.setVisibility(View.INVISIBLE);
            }
        });



















        return view;
    }
}
