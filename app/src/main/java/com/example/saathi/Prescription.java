package com.example.saathi;

public class Prescription {
    String presId;
    String prescriptionTxt;
    public Prescription(){

    }

    public Prescription(String presId, String prescriptionTxt) {
        this.presId = presId;
        this.prescriptionTxt = prescriptionTxt;


    }

    public String getPrescriptionTxt() {
        return prescriptionTxt;
    }

    public String getPresId() {
        return presId;
    }

    public void setPrescriptionTxt(String prescriptionTxt) {
        this.prescriptionTxt = prescriptionTxt;
    }

    public void setPresId(String presId) {
        this.presId = presId;
    }
}
