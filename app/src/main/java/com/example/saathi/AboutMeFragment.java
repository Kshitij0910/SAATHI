package com.example.saathi;




import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import com.example.saathi.model.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;


public class AboutMeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AboutMeFragment";

    TextView selectDate;
    EditText name, phnNbr, bldGrp, illness, address,pinCode, city;
    Button saveBtn;
    Realm realm;
    de.hdodenhof.circleimageview.CircleImageView profileImg;
    TextView display;
    StorageReference mStorageReference;
    ProgressBar loadImg;
    FirebaseAuth fAuth;







    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_about_me, container, false);

        display=view.findViewById(R.id.display);
        selectDate=view.findViewById(R.id.dob);
        name=view.findViewById(R.id.name);
        phnNbr=view.findViewById(R.id.phone);
        bldGrp=view.findViewById(R.id.bld_grp);
        illness=view.findViewById(R.id.illness);
        address=view.findViewById(R.id.add);
        pinCode=view.findViewById(R.id.pincode);
        city=view.findViewById(R.id.city);
        saveBtn=view.findViewById(R.id.save_btn);
        profileImg=view.findViewById(R.id.profile_image);
        loadImg=view.findViewById(R.id.load_image);

        mStorageReference= FirebaseStorage.getInstance().getReference();
         fAuth=FirebaseAuth.getInstance();

         StorageReference profileRef= mStorageReference.child("users/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImg);
            }
        });


        Log.d(TAG, "onCreateView: View initialisation done!");

        /*realm=Realm.getDefaultInstance();

        UserProfile userProfile=new UserProfile();

        realm.beginTransaction();
        realm.insertOrUpdate();
        realm.commitTransaction();*/
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment=new DatePickerFragment();
                newFragment.setTargetFragment(AboutMeFragment. this, 0);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                realm=Realm.getDefaultInstance();

                UserProfile userProfile=new UserProfile();

                realm.beginTransaction();
                //realm.insertOrUpdate();
                realm.commitTransaction();

                saveData();
                readData();



            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Intent openGalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
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

    private void saveData(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                UserProfile userProfile = bgRealm.createObject(UserProfile.class);
                userProfile.setFullName(name.getText().toString().trim());
                userProfile.setDateOfBirth(selectDate.getText().toString().trim());
                userProfile.setPhoneNumber(phnNbr.getText().toString().trim());
                userProfile.setBloodGrp(bldGrp.getText().toString().trim());
                userProfile.setIllness(illness.getText().toString().trim());
                userProfile.setAddress(address.getText().toString().trim());
                userProfile.setPin(pinCode.getText().toString().trim());
                userProfile.setCity(city.getText().toString().trim());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.d(TAG, "onSuccess: Data written successfully!");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.d(TAG, "onError: Error occurred!");
            }
        });
    }

    private void readData(){
        RealmResults<UserProfile> userProfiles=realm.where(UserProfile.class).findAll();
        String data="";


        for (UserProfile userProfile:userProfiles) {
            try {
                Log.d(TAG, "readData: Reading Data");
                data="";
                data=data + "\n" + userProfile.toString();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        display.setText(data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                Uri imgUri=data.getData();
                //profileImg.setImageURI(imgUri);
                uploadImgToFirebase(imgUri);

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadImgToFirebase(Uri imgUri){
        //upload image to firebase storage.
        final StorageReference fileRef=mStorageReference.child("users/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"/profile.jpg");
        fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                loadImg.setVisibility(View.VISIBLE);
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.get().load(uri).into(profileImg);

                        Toast.makeText(getActivity(), "Profile Photo Uploaded!", Toast.LENGTH_SHORT).show();
                        loadImg.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Upload Failed!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}

