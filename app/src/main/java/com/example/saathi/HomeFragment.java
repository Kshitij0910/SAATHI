package com.example.saathi;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_home, container, false);
        TextView medicines=view.findViewById(R.id.medicines);
        TextView reports=view.findViewById(R.id.reports);
        TextView map=view.findViewById(R.id.map);




        medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr=getFragmentManager().beginTransaction();//getFragmentManager is deprecated
                fr.replace(R.id.fragment_container, new MedicinesFragment());
                fr.commit();


            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr_= getFragmentManager().beginTransaction(); //getFragmentManager is deprecated
                fr_.replace(R.id.fragment_container, new ReportsFragment());
                fr_.commit();
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr__= getFragmentManager().beginTransaction(); //getFragmentManager is deprecated
                fr__.replace(R.id.fragment_container, new GETFragment());
                fr__.commit();
            }
        });
        return view;
    }

}
