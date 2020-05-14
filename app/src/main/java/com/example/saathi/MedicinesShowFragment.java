package com.example.saathi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MedicinesShowFragment extends Fragment {

    public static final int CAMERA_PERMISSION_REQ=1000;
    private Button capture;
    ImageView medicine;
    EditText prescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_medicines_show, container, false);
        capture=view.findViewById(R.id.capture_photo);
        medicine=view.findViewById(R.id.my_medicine);
        prescription=view.findViewById(R.id.my_prescription);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });



        return view;
    }
    private void handlePermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            openCamera();
        }
        else{
            //Request permission.
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQ);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_PERMISSION_REQ:
                for (int i=0; i<permissions.length; i++)
                {
                    String permission=permissions[i];
                    if (grantResults[i]==PackageManager.PERMISSION_DENIED){
                        boolean showRationale= shouldShowRequestPermissionRationale(permission);
                        if (showRationale){
                            Toast.makeText(getActivity(), "PERMISSION DENIED ONCE or MORE!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "NEVER ASK AGAIN!", Toast.LENGTH_SHORT).show();
                            showSettingsAlert();
                        }
                    }
                    else {
                        openCamera();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void init(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            //No permission needed.
            openCamera();
        }
        else{
            handlePermission();
        }
    }

    private void openCamera(){
        Intent cameraIntent=new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(cameraIntent);
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DON'T ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openAppSettings(MedicinesShowFragment.this);

                    }
                });
        alertDialog.show();
    }

    public static void openAppSettings(final  Fragment context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getActivity().getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
}
