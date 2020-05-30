package com.example.saathi;

import android.app.Application;

import io.realm.Realm;

import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

import static com.example.saathi.Constants.AUTH_URL;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //SyncConfiguration syncConfig=SyncConfiguration.Builder.
        RealmConfiguration realmConfig=new RealmConfiguration.Builder().name("tasky.realm").build();
        Realm.setDefaultConfiguration(realmConfig);
        //Realm.setDefaultConfiguration(SyncUser.current().getDefaultConfiguration());
        //SyncConfiguration syncConfig=SyncUser.current().createConfiguration(AUTH_URL).fullSynchronization().build();
        //Realm.setDefaultConfiguration(syncConfig);


    }
}
