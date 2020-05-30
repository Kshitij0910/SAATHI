package com.example.saathi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ReportsFragment extends Fragment {
    ImageView img;
    View dotmenu;
    Button btnPageMedicines, btnPageReports;
    Animation imgbounce;
    Button goto_medicines, goto_reports;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_reports, container,false);





        imgbounce=AnimationUtils.loadAnimation(getActivity(), R.anim.imgbounce);


        img=view.findViewById(R.id.img_reports);
        btnPageMedicines=view.findViewById(R.id.btn_page_medicines);
        btnPageReports=view.findViewById(R.id.btn_page_report);
        dotmenu=view.findViewById(R.id.dotmenu);
        goto_reports=view.findViewById(R.id.goto_upload_reports);
        goto_medicines=view.findViewById(R.id.goto_upload_medicines);

        btnPageReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.reports);

                goto_reports.setVisibility(View.VISIBLE);
                goto_medicines.setVisibility(View.GONE);

                //pass animation
                img.startAnimation(imgbounce);


                //animation + img source
                btnPageReports.setBackgroundResource(R.drawable.reports_icon);
                btnPageMedicines.setBackgroundResource(R.drawable.medicines_disabled);


                btnPageMedicines.animate().scaleY(0.7f).scaleX(0.7f).setDuration(350).start();
                btnPageReports.animate().scaleY(1).scaleX(1).setDuration(350).start();
                dotmenu.animate().translationX(0).setDuration(350).setStartDelay(100).start();

            }
        });


        btnPageMedicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageResource(R.drawable.medicines);

                goto_reports.setVisibility(View.GONE);
                goto_medicines.setVisibility(View.VISIBLE);

                //pass animation
                img.startAnimation(imgbounce);


                //animation + img source
                btnPageMedicines.setBackgroundResource(R.drawable.medicines_icon);
                btnPageReports.setBackgroundResource(R.drawable.reports_disabled);

                btnPageReports.animate().scaleY(0.7f).scaleX(0.7f).setDuration(350).start();
                btnPageMedicines.animate().scaleY(1).scaleX(1).setDuration(350).start();
                dotmenu.animate().translationX(300).setDuration(350).setStartDelay(100).start();

            }
        });

        goto_medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr7=getFragmentManager().beginTransaction(); //getFragmentManager is deprecated
                fr7.replace(R.id.fragment_container, new MedicinesShowFragment());
                fr7.addToBackStack(null);
                fr7.commit();
            }
        });

        goto_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr8=getFragmentManager().beginTransaction(); //getFragmentManager is deprecated
                fr8.replace(R.id.fragment_container, new ReportsShowFragment());
                fr8.addToBackStack(null);
                fr8.commit();
            }
        });

        return view;
    }
}
