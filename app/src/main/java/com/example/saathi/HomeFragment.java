package com.example.saathi;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);


        TextView medicines = view.findViewById(R.id.medicines);
        TextView reports = view.findViewById(R.id.reports);
        TextView map = view.findViewById(R.id.map);
        TextView yoga = view.findViewById(R.id.yoga);
        TextView eat = view.findViewById(R.id.food);
        TextView walk = view.findViewById(R.id.walk);






        medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr1 = getFragmentManager().beginTransaction();//getFragmentManager is deprecated
                fr1.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                fr1.replace(R.id.fragment_container, new MedicinesFragment());
                fr1.addToBackStack(null);
                fr1.commit();


            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr2 = getFragmentManager().beginTransaction(); //getFragmentManager is deprecated
                fr2.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                fr2.replace(R.id.fragment_container, new ReportsFragment());
                fr2.addToBackStack(null);
                fr2.commit();
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr3 = getFragmentManager().beginTransaction(); //getFragmentManager is deprecated
                fr3.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                fr3.replace(R.id.fragment_container, new GETFragment());
                fr3.addToBackStack(null);
                fr3.commit();
            }
        });

        yoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr4 = getFragmentManager().beginTransaction(); //getFragmentManager is deprecated
                fr4.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                fr4.replace(R.id.fragment_container, new YogaFragment());
                fr4.addToBackStack(null);
                fr4.commit();

            }
        });

        eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Under Development...Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Under Development...Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

}
