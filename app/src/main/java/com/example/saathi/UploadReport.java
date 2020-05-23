package com.example.saathi;

import com.google.firebase.database.Exclude;

public class UploadReport {
    private String mFileName;
    private String mFileUrl;
    private String mKey;

    public UploadReport() {
    }

    public UploadReport(String fileName, String fileUrl) {
        mFileName = fileName;
        mFileUrl = fileUrl;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public void setFileUrl(String fileUrl) {
        mFileUrl = fileUrl;
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
