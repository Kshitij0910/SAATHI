package com.example.saathi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class MedicinesShowFragment extends Fragment {
    private static final String TAG = "MedicinesShowFragment";
    public static final int CAMERA_PERMISSION_REQ=1000;
    private Button capture;
    ImageView medicine;
    EditText prescription;
    Button uploadPrescription, voicePrescription;


    ProgressBar load;

    String currentPhotoPath;
    StorageReference imgStorageReference;
    FirebaseAuth fAuth;

    DatabaseReference imgDatabaseReference;
    private StorageTask mUploadTask;
    private Uri contentUri;

    String fName;

    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;






    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_medicines_show, container, false);




        imgStorageReference= FirebaseStorage.getInstance().getReference("Prescription");
        imgDatabaseReference=FirebaseDatabase.getInstance().getReference("Prescription");
        fAuth=FirebaseAuth.getInstance();
        //prescriptionRef= FirebaseDatabase.getInstance().getReference("Prescription");

        capture=view.findViewById(R.id.capture_photo);
        medicine=view.findViewById(R.id.my_medicine);
        prescription=view.findViewById(R.id.my_prescription);
        load=view.findViewById(R.id.uploading_image);
        uploadPrescription=view.findViewById(R.id.upload_prescription);
        voicePrescription=view.findViewById(R.id.voice_prescription);

        mSpeechRecognizer=SpeechRecognizer.createSpeechRecognizer(getContext());
        mSpeechRecognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        capture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    init();



                capture.setBackgroundResource(R.drawable.captured);
            }
        });



         uploadPrescription.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 closeKeyboard();
                 load.setVisibility(View.VISIBLE);
                if(contentUri == null){
                    load.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Please capture an image!", Toast.LENGTH_SHORT).show();
                }
                else if (mUploadTask !=null && mUploadTask.isInProgress()){
                     Toast.makeText(getActivity(), "Upload in Progress!", Toast.LENGTH_SHORT).show();
                 }
                 else{
                     uploadImageToFirebase(fName, contentUri);
                 }

             }
         });

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches!=null){
                    prescription.setText(matches.get(0));
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        voicePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Tap and hold to record prescription.", Toast.LENGTH_SHORT).show();
                prescription.setHint("MY PRESCRIPTION");
            }
        });

        voicePrescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        prescription.setHint("MY PRESCRIPTION");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        prescription.setText("");
                        prescription.setHint("Listening");
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }





                return false;
            }
        });








        return view;
    }




    //Methods required for uploading medicines.



    //Conditions on SDK for requesting permission
    private void init(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            //No permission needed.
            dispatchTakePictureIntent();
        }
        else{
            handlePermission();
        }
    }

    //To request permission.
    private void handlePermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            dispatchTakePictureIntent();
        }
        else{
            //Request permission.
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQ);
        }

    }
    //To check request
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
                        dispatchTakePictureIntent();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //Dialog Box to request open settings
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


    //To upload image

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            //super.onActivityResult(requestCode, resultCode, data);



            if(requestCode==CAMERA_PERMISSION_REQ){
                if(resultCode==RESULT_OK) {
                    File f=new File(currentPhotoPath);
                    medicine.setImageURI(Uri.fromFile(f));

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    contentUri=Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);

                    fName=f.getName();
                }
                else {
                    capture.setBackgroundResource(R.drawable.capture);
                }
            }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_PERMISSION_REQ);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadImageToFirebase(String name, final Uri contentUri){

        final StorageReference imgPath=imgStorageReference.child("users/" + Objects.requireNonNull(fAuth.getCurrentUser()).getUid()).child("Prescription/"+name);
        mUploadTask= imgPath.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: Uploaded Image Url is"+uri.toString());
                        Upload upload=new Upload(uri.toString(), prescription.getText().toString());
                        String uploadId=imgDatabaseReference.push().getKey();
                        imgDatabaseReference.child("users/").child(fAuth.getCurrentUser().getUid()).child(uploadId).setValue(upload);

                    }


                });
                load.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Your Medicines are uploaded!", Toast.LENGTH_SHORT).show();

                openMedicinesViewFragment();



            }



        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                load.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Upload Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openMedicinesViewFragment(){
        FragmentTransaction fr6=getFragmentManager().beginTransaction();//getFragmentManager is deprecated
        fr6.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right);
        fr6.replace(R.id.fragment_container, new MedicinesViewFragment());
        fr6.addToBackStack(null);
        fr6.commit();
    }

    private void closeKeyboard() {
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


        }
    }

}



