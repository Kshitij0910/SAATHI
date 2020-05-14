package com.example.saathi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MedicinesFragment extends Fragment {
    Button go;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_medicines, container,false);
        go=view.findViewById(R.id.goto_capture);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr3=getFragmentManager().beginTransaction();
                fr3.replace(R.id.fragment_container, new MedicinesShowFragment());
                fr3.commit();
            }
        });
        return view;
    }
}
