package com.example.saathi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ReportsViewFragment extends Fragment implements UploadAdapter2.OnItemClickListener  {
    private RecyclerView recyclerView;
    private UploadAdapter2 mAdapter2;

    private FirebaseStorage mStorage;
    private DatabaseReference mDataRef;
    private List<UploadReport> mReport;

    private ValueEventListener mDBListenerRep;

    private ProgressBar loadUpload2;
    FirebaseAuth fAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_view_reports, container, false);


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_2);


        loadUpload2 = view.findViewById(R.id.load_upload_2);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fAuth = FirebaseAuth.getInstance();


        mReport = new ArrayList<>();

        mAdapter2 = new UploadAdapter2(getActivity(), mReport);
        recyclerView.setAdapter(mAdapter2);

        mAdapter2.setOnItemClickListener(ReportsViewFragment.this);

        mStorage = FirebaseStorage.getInstance();
        mDataRef = FirebaseDatabase.getInstance().getReference("Reports/users/" + fAuth.getCurrentUser().getUid());
        Log.d("ReportsViewFragment", "onViewCreated: "+mDataRef);

        mDBListenerRep = mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mReport.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadReport uploadReport = postSnapshot.getValue(UploadReport.class);
                    uploadReport.setKey(postSnapshot.getKey());
                    Log.d("ReportsViewFragment", "onDataChange: "+uploadReport.getFileName()+","+uploadReport.getFileUrl()+", "+uploadReport.getKey());
                    mReport.add(uploadReport);
                }
                mAdapter2.notifyDataSetChanged();

                loadUpload2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadUpload2.setVisibility(View.INVISIBLE);
            }
        });

        /*mDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String fileName=dataSnapshot.getKey();
                String url=dataSnapshot.getValue(String.class);
                ((UploadAdapter2)recyclerView.getAdapter()).update(fileName, url);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        recyclerView=view.findViewById(R.id.recycler_view_2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       UploadAdapter2 uploadAdapter2=new UploadAdapter2(recyclerView, getContext(), new ArrayList<String>(), new ArrayList<String>());
       recyclerView.setAdapter(uploadAdapter2);
       loadUpload2.setVisibility(View.INVISIBLE);*/


    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(),"Tap and Hold for further actions!", Toast.LENGTH_SHORT).show();
        UploadReport selectedReport=mReport.get(position);
        Intent pdfIntent=new Intent();
        pdfIntent.setType(Intent.ACTION_VIEW);
        pdfIntent.setData(Uri.parse(selectedReport.getFileUrl()));
        pdfIntent.setDataAndType(Uri.parse(selectedReport.getFileUrl()),Intent.ACTION_VIEW);
        startActivity(pdfIntent);
    }



    @Override
    public void onDeleteClick(int position) {
        UploadReport selectedRep=mReport.get(position);
        final String selectedKeyRep=selectedRep.getKey();

        StorageReference fileRef=mStorage.getReferenceFromUrl(selectedRep.getFileUrl());
        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDataRef.child(selectedKeyRep).removeValue();
                Toast.makeText(getActivity(), "Your Medical Report has been deleted successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDataRef.removeEventListener(mDBListenerRep);
    }
}
