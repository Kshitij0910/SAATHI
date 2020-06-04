package com.example.saathi.model;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {
    Realm realm;

    public RealmHelper(Realm realm){
        this.realm=realm;
    }

    public void save(final UserProfile userProfile){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserProfile userP=realm.copyToRealm(userProfile);
            }
        });
    }

    public ArrayList<String> retrieveName() {
        ArrayList<String> userName= new ArrayList<>();
        RealmResults<UserProfile> userProfiles = realm.where(UserProfile.class).findAllAsync();
        for (UserProfile userP:userProfiles){
            userName.add(userP.getFullName());
        }
        return userName;
    }

    public ArrayList<String> retrieveDob() {
        ArrayList<String> userDob= new ArrayList<>();
        RealmResults<UserProfile> userProfiles = realm.where(UserProfile.class).findAllAsync();
        for (UserProfile userP:userProfiles){
            userDob.add(userP.getDateOfBirth());
        }
        return userDob;
    }

    public ArrayList<String> retrievePhn() {
        ArrayList<String> userPhn= new ArrayList<>();
        RealmResults<UserProfile> userProfiles = realm.where(UserProfile.class).findAllAsync();
        for (UserProfile userP:userProfiles){
            userPhn.add(userP.getPhoneNumber());
        }
        return userPhn;
    }

    public ArrayList<String> retrieveBldGrp() {
        ArrayList<String> userBldGrp= new ArrayList<>();
        RealmResults<UserProfile> userProfiles = realm.where(UserProfile.class).findAllAsync();
        for (UserProfile userP:userProfiles){
            userBldGrp.add(userP.getBloodGrp());
        }
        return userBldGrp;
    }

    public ArrayList<String> retrieveIllness() {
        ArrayList<String> userIllness= new ArrayList<>();
        RealmResults<UserProfile> userProfiles = realm.where(UserProfile.class).findAllAsync();
        for (UserProfile userP:userProfiles){
            userIllness.add(userP.getIllness());
        }
        return userIllness;
    }

    public ArrayList<String> retrieveAddress() {
        ArrayList<String> userAdd= new ArrayList<>();
        RealmResults<UserProfile> userProfiles = realm.where(UserProfile.class).findAllAsync();
        for (UserProfile userP:userProfiles){
            userAdd.add(userP.getAddress());
        }
        return userAdd;
    }

    public ArrayList<String> retrievePin() {
        ArrayList<String> userPin= new ArrayList<>();
        RealmResults<UserProfile> userProfiles = realm.where(UserProfile.class).findAllAsync();
        for (UserProfile userP:userProfiles){
            userPin.add(userP.getPin());
        }
        return userPin;
    }

    public ArrayList<String> retrieveCity() {
        ArrayList<String> userCity= new ArrayList<>();
        RealmResults<UserProfile> userProfiles = realm.where(UserProfile.class).findAllAsync();
        for (UserProfile userP:userProfiles){
            userCity.add(userP.getCity());
        }
        return userCity;
    }
}
