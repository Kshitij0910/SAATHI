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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import com.example.saathi.model.RealmHelper;
import com.example.saathi.model.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import static com.example.saathi.Constants.REALM_URL;


public class AboutMeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "AboutMeFragment";

    TextView selectDate;
    EditText name, phnNbr, bldGrp, illness, address,pinCode, city;
    Button saveBtn;
    Realm realm;
    de.hdodenhof.circleimageview.CircleImageView profileImg;
    TextView display, dropdown, dropup;
    StorageReference mStorageReference;
    ProgressBar loadImg;
    FirebaseAuth fAuth;

    ArrayList<String> dataName;
    ArrayList<String> dataDob;
    ArrayList<String> dataPhn;
    ArrayList<String> dataBldGrp;
    ArrayList<String> dataIllness;
    ArrayList<String> dataAdd;
    ArrayList<String> dataPin;
    ArrayList<String> dataCity;

    String info;

    Animation animSlideDown, animSlideUp;
    RelativeLayout editProfile;





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_about_me, container, false);

        SyncUser user=SyncUser.current();
        String url=REALM_URL+"/~/UserProfile";
        final SyncConfiguration config=user.createConfiguration(url).fullSynchronization().build();



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
        dropdown=view.findViewById(R.id.drop_down);
        dropup=view.findViewById(R.id.drop_up);
        editProfile=view.findViewById(R.id.edit_profile);

        animSlideDown= AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
        animSlideUp= AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);

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


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment=new DatePickerFragment();
                newFragment.setTargetFragment(AboutMeFragment. this, 0);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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


        Log.d(TAG, "onCreateView: Realm instantiating");

        realm=Realm.getInstance(config);
        //realm=Realm.getDefaultInstance();
        /*Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                RealmHelper helper=new RealmHelper(realm);

                dataName=helper.retrieveName();
                dataDob=helper.retrieveDob();
                dataPhn=helper.retrievePhn();
                dataBldGrp=helper.retrieveBldGrp();
                dataIllness=helper.retrieveIllness();
                dataAdd=helper.retrieveAddress();
                dataPin=helper.retrievePin();
                dataCity=helper.retrieveCity();

                info="NAME: "+ dataName.get(dataName.size()-1) +"\n\n"+
                        "DATE OF BIRTH: "+dataDob.get(dataDob.size()-1)+"\n\n"+
                        "EMERGENCY CONTACT NUMBER: "+dataPhn.get(dataPhn.size()-1)+"\n\n"+
                        "BLOOD GROUP: "+dataBldGrp.get(dataBldGrp.size()-1)+"\n\n"+
                        "MAJOR ILLNESS: "+dataIllness.get(dataIllness.size()-1)+"\n\n"+
                        "RESIDENTIAL ADDRESS: "+dataAdd.get(dataAdd.size()-1)+"\n\n"+
                        "PIN CODE: "+dataPin.get(dataPin.size()-1)+"\n\n"+
                        "CITY: "+dataCity.get(dataCity.size()-1);

                display.setText(info);
            }
        });*/

        final RealmHelper helper=new RealmHelper(realm);


        dataName=helper.retrieveName();
        dataDob=helper.retrieveDob();
        dataPhn=helper.retrievePhn();
        dataBldGrp=helper.retrieveBldGrp();
        dataIllness=helper.retrieveIllness();
        dataAdd=helper.retrieveAddress();
        dataPin=helper.retrievePin();
        dataCity=helper.retrieveCity();
        Log.d(TAG, "onCreateView: RealmHelper retrieved data");
        info="NAME: "+ dataName.get(dataName.size()-1) +"\n\n"+
                "DATE OF BIRTH: "+dataDob.get(dataDob.size()-1)+"\n\n"+
                "EMERGENCY CONTACT NUMBER: "+dataPhn.get(dataPhn.size()-1)+"\n\n"+
                "BLOOD GROUP: "+dataBldGrp.get(dataBldGrp.size()-1)+"\n\n"+
                "MAJOR ILLNESS: "+dataIllness.get(dataIllness.size()-1)+"\n\n"+
                "RESIDENTIAL ADDRESS: "+dataAdd.get(dataAdd.size()-1)+"\n\n"+
                "PIN CODE: "+dataPin.get(dataPin.size()-1)+"\n\n"+
                "CITY: "+dataCity.get(dataCity.size()-1);
        Log.d(TAG, "onClick: Retrieved Data displaying");
        if(dataName.size() >0)
            display.setText(info);
        else
            display.setText("");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm.getInstanceAsync(config, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        UserProfile userP=new UserProfile();
                        userP.setFullName(name.getText().toString());
                        userP.setDateOfBirth(selectDate.getText().toString());
                        userP.setPhoneNumber(phnNbr.getText().toString());
                        userP.setBloodGrp(bldGrp.getText().toString());
                        userP.setIllness(illness.getText().toString());
                        userP.setAddress(address.getText().toString());
                        userP.setPin(pinCode.getText().toString());
                        userP.setCity(city.getText().toString());

                        RealmHelper helper=new RealmHelper(realm);
                        helper.save(userP);
                        name.setText("");
                        selectDate.setText("");
                        phnNbr.setText("");
                        bldGrp.setText("");
                        illness.setText("");
                        address.setText("");
                        pinCode.setText("");
                        city.setText("");


                        dataName=helper.retrieveName();
                        dataDob=helper.retrieveDob();
                        dataPhn=helper.retrievePhn();
                        dataBldGrp=helper.retrieveBldGrp();
                        dataIllness=helper.retrieveIllness();
                        dataAdd=helper.retrieveAddress();
                        dataPin=helper.retrievePin();
                        dataCity=helper.retrieveCity();


                        info="NAME: "+ dataName.get(dataName.size()-1) +"\n\n"+
                                "DATE OF BIRTH: "+dataDob.get(dataDob.size()-1)+"\n\n"+
                                "EMERGENCY CONTACT NUMBER: "+dataPhn.get(dataPhn.size()-1)+"\n\n"+
                                "BLOOD GROUP: "+dataBldGrp.get(dataBldGrp.size()-1)+"\n\n"+
                                "MAJOR ILLNESS: "+dataIllness.get(dataIllness.size()-1)+"\n\n"+
                                "RESIDENTIAL ADDRESS: "+dataAdd.get(dataAdd.size()-1)+"\n\n"+
                                "PIN CODE: "+dataPin.get(dataPin.size()-1)+"\n\n"+
                                "CITY: "+dataCity.get(dataCity.size()-1);
                        Log.d(TAG, "onClick: Data stored");
                        display.setText(info);
                        Log.d(TAG, "onClick: Data displayed");
                    }
                });
                /*UserProfile userP=new UserProfile();
                userP.setFullName(name.getText().toString());
                userP.setDateOfBirth(selectDate.getText().toString());
                userP.setPhoneNumber(phnNbr.getText().toString());
                userP.setBloodGrp(bldGrp.getText().toString());
                userP.setIllness(illness.getText().toString());
                userP.setAddress(address.getText().toString());
                userP.setPin(pinCode.getText().toString());
                userP.setCity(city.getText().toString());

                RealmHelper helper=new RealmHelper(realm);
                helper.save(userP);
                name.setText("");
                selectDate.setText("");
                phnNbr.setText("");
                bldGrp.setText("");
                illness.setText("");
                address.setText("");
                pinCode.setText("");
                city.setText("");


                dataName=helper.retrieveName();
                dataDob=helper.retrieveDob();
                dataPhn=helper.retrievePhn();
                dataBldGrp=helper.retrieveBldGrp();
                dataIllness=helper.retrieveIllness();
                dataAdd=helper.retrieveAddress();
                dataPin=helper.retrievePin();
                dataCity=helper.retrieveCity();


                info="NAME: "+ dataName.get(dataName.size()-1) +"\n\n"+
                     "DATE OF BIRTH: "+dataDob.get(dataDob.size()-1)+"\n\n"+
                     "EMERGENCY CONTACT NUMBER: "+dataPhn.get(dataPhn.size()-1)+"\n\n"+
                     "BLOOD GROUP: "+dataBldGrp.get(dataBldGrp.size()-1)+"\n\n"+
                     "MAJOR ILLNESS: "+dataIllness.get(dataIllness.size()-1)+"\n\n"+
                     "RESIDENTIAL ADDRESS: "+dataAdd.get(dataAdd.size()-1)+"\n\n"+
                     "PIN CODE: "+dataPin.get(dataPin.size()-1)+"\n\n"+
                     "CITY: "+dataCity.get(dataCity.size()-1);

                Log.d(TAG, "onClick: Data stored");
                display.setText(info);
                Log.d(TAG, "onClick: Data displayed");*/



            }
        });

        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile.setVisibility(View.VISIBLE);
                editProfile.startAnimation(animSlideDown);
                dropdown.setVisibility(View.GONE);
                dropup.setVisibility(View.VISIBLE);
            }
        });

        dropup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editProfile.startAnimation(animSlideUp);
                editProfile.setVisibility(View.GONE);
                dropdown.setVisibility(View.VISIBLE);
                dropup.setVisibility(View.GONE);
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

