package com.example.saathi;



import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;



public class AboutMeFragment extends Fragment  implements DatePickerDialog.OnDateSetListener {

    TextView selectDate;

    public AboutMeFragment(){

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_about_me, container, false);
        selectDate=view.findViewById(R.id.dob);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment=new DatePickerFragment();
                newFragment.setTargetFragment(AboutMeFragment. this, 0);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
        return view;



        }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month=month+1;
        StringBuilder sb=new StringBuilder().append(dayOfMonth).append("/").append(month).append("/").append(year);
        String formattedDate=sb.toString();
        selectDate.setText(formattedDate);
    }
}

