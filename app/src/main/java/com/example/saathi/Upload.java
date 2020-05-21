package com.example.saathi;

import com.google.firebase.database.Exclude;

public class Upload {
     private String mImageUrl;
     private String mPrescription;
     private String mKey;

    public Upload(){

    }

    public Upload(String ImageUrl, String Prescription) {
        mImageUrl = ImageUrl;
        mPrescription = Prescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        mImageUrl = ImageUrl;
    }

    public String getPrescription() {
        return mPrescription;
    }

    public void setPrescription(String Prescription) {
        mPrescription = Prescription;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
