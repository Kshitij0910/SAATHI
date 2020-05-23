package com.example.saathi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ReportsShowFragment extends Fragment {
    Button selectFile, upload;
    TextView file, gotoReports;

    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;
    FirebaseAuth fAuth;

    Uri pdfUri;

    ProgressBar loadReport;

    private static final String TAG = "ReportsShowFragment";

    public static final int FILE_PERMISSION_REQ=1000;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_show_reports, container, false);
        selectFile=view.findViewById(R.id.file_select);
        file=view.findViewById(R.id.file);
        upload=view.findViewById(R.id.report_upload);
        loadReport=view.findViewById(R.id.load_report);
        gotoReports=view.findViewById(R.id.show_reports);

        fAuth=FirebaseAuth.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference("Reports");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Reports");

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_PERMISSION_REQ );
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                loadReport.setVisibility(View.VISIBLE);
                if (pdfUri !=null){
                    uploadPdf(pdfUri);
                }
                else {
                    loadReport.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Please select your Report File!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gotoReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr8=getFragmentManager().beginTransaction();//getFragmentManager is deprecated
                fr8.replace(R.id.fragment_container, new ReportsViewFragment());
                fr8.commit();

            }
        });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case FILE_PERMISSION_REQ:
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
                        selectPdf();
                    }
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                        openAppSettings(ReportsShowFragment.this);

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

    private void selectPdf(){
        Intent fileIntent=new Intent();
        fileIntent.setType("application/pdf");
        fileIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(fileIntent, 2000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==2000 && resultCode==RESULT_OK && data!=null){
          pdfUri=data.getData();
          file.setText("FILE SELECTED: "+data.getData().getLastPathSegment());
        }
        else {
            Toast.makeText(getActivity(), "Please select your Report File!", Toast.LENGTH_SHORT).show();
        }

        //super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadPdf(final Uri pdfUri){


        //final String fileName=System.currentTimeMillis()+".pdf";
        final String fileName=System.currentTimeMillis()+"";

        final StorageReference pdfPath= mStorageRef.child("users/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()).child("Reports/"+fileName);
        pdfPath.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pdfPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: Uploaded File url is:" +uri.toString());
                        UploadReport uploadReport=new UploadReport(fileName, uri.toString());

                        String uploadRepId=mDatabaseRef.push().getKey();
                        mDatabaseRef.child("users/"+fAuth.getCurrentUser().getUid()).child(uploadRepId).setValue(uploadReport);
                    }
                });
                loadReport.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Your Reports are Uploaded!", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Upload Failed!"+e.getMessage(), Toast.LENGTH_SHORT);
                loadReport.setVisibility(View.GONE);
            }
        });





    }
}
