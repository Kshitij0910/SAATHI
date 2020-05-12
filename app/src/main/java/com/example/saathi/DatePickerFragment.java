package com.example.saathi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener dateSetListener; // listener object to get calling fragment listener
    DatePickerDialog myDatePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
        dateSetListener=(DatePickerDialog.OnDateSetListener)getTargetFragment();
        myDatePicker=new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
        return myDatePicker;
    }
}
