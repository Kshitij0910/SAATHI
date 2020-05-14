package com.example.saathi;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig=new RealmConfiguration.Builder().name("tasky.realm").build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
