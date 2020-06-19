package com.example.saathi;

import com.google.firebase.database.Exclude;

public class Upload {
     private String mImageUrl;
     private String mPrescription;
     private String mKey;
     private boolean expanded;

    public Upload(){

    }

    public Upload(String ImageUrl, String Prescription) {
        mImageUrl = ImageUrl;
        mPrescription = Prescription;
        this.expanded=false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
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
