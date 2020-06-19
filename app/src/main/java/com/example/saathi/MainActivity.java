package com.example.saathi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.SyncUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final int RECORD_AUDIO_PERMISSION = 1;

    private TextToSpeech medicine, report, places, yogapg, addmed, addrep, medlist, replist, invalid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*String notificationFragment=getIntent().getStringExtra("NotificationFragment");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(notificationFragment !=null){
            if (notificationFragment.equals("MedicinesViewFragment")){
                fragmentTransaction.replace(R.id.fragment_container, new MedicinesViewFragment())
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .addToBackStack(null)
                        .commit();
            }
        }*/

        medicine = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = medicine.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported.");
                    }
                } else {
                    Log.e("TTS", "onInit: Initialised failed.");
                }
            }
        });

        report = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = report.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported.");
                    }
                } else {
                    Log.e("TTS", "onInit: Initialised failed.");
                }
            }
        });

        places = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = places.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported.");
                    }
                } else {
                    Log.e("TTS", "onInit: Initialised failed.");
                }
            }
        });


        yogapg = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = yogapg.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported.");
                    }
                } else {
                    Log.e("TTS", "onInit: Initialised failed.");
                }
            }
        });

        medlist = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = medlist.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported.");
                    }
                } else {
                    Log.e("TTS", "onInit: Initialised failed.");
                }
            }
        });

        replist = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = replist.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported.");
                    }
                } else {
                    Log.e("TTS", "onInit: Initialised failed.");
                }
            }
        });

        addmed = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = addmed.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported.");
                    }
                } else {
                    Log.e("TTS", "onInit: Initialised failed.");
                }
            }
        });

        addrep = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = addrep.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported.");
                    }
                } else {
                    Log.e("TTS", "onInit: Initialised failed.");
                }
            }
        });

        invalid = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = invalid.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "onInit: Language not supported.");
                    }
                } else {
                    Log.e("TTS", "onInit: Initialised failed.");
                }

            }
        });


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        SyncUser.current().logOut();

        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void voiceCommands() {
        checkPermission();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

            case R.id.nav_about_me:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutMeFragment()).commit();
                break;

            case R.id.nav_logout:
                Toast.makeText(this, "User Logging out!", Toast.LENGTH_SHORT).show();
                logout();
                break;

            case R.id.nav_voice:
                voiceCommands();

        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                //openDialog();
                voiceAutomation();
            } else {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_AUDIO_PERMISSION:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);

                        if (showRationale) {
                            Toast.makeText(this, "PERMISSION DENIED ONCE or MORE!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "NEVER ASK AGAIN!", Toast.LENGTH_SHORT).show();
                            showSettingsAlert();
                        }
                    } else {
                        //openDialog();
                        voiceAutomation();
                    }
                }
        }
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
                        openAppSettings(MainActivity.this);

                    }
                });
        alertDialog.show();
    }

    public static void openAppSettings(final AppCompatActivity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    private void voiceAutomation() {
        Intent voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(voice, RECORD_AUDIO_PERMISSION);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_AUDIO_PERMISSION && data != null && resultCode == RESULT_OK) {
            ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String voiceInput = arrayList.get(0);
            switch (voiceInput) {
                case "my medicine":

                    medicine.speak("Navigating to my medicines page.", TextToSpeech.QUEUE_FLUSH, null);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new MedicinesFragment())
                            .addToBackStack(null)
                            .commit();
                    break;

                case "my report":

                    report.speak("Navigating to my reports page.", TextToSpeech.QUEUE_FLUSH, null);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new ReportsFragment())
                            .addToBackStack(null)
                            .commit();
                    break;

                case "places near me":
                    places.speak("Navigating to places near me page.", TextToSpeech.QUEUE_FLUSH, null);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new GETFragment())
                            .addToBackStack(null)
                            .commit();
                    break;

                case "do yoga":
                    yogapg.speak("Navigating to do yoga page.", TextToSpeech.QUEUE_FLUSH, null);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new YogaFragment())
                            .addToBackStack(null)
                            .commit();
                    break;

                case "view medicine":
                    medlist.speak("Navigating to View Medicines page.", TextToSpeech.QUEUE_FLUSH, null);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new MedicinesViewFragment())
                            .addToBackStack(null)
                            .commit();
                    break;

                case "view report":
                    replist.speak("Navigating to view reports page", TextToSpeech.QUEUE_FLUSH, null);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new ReportsViewFragment())
                            .addToBackStack(null)
                            .commit();
                    break;

                case "add medicine":
                    addmed.speak("navigating to add medicine page.", TextToSpeech.QUEUE_FLUSH, null);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new MedicinesShowFragment())
                            .addToBackStack(null)
                            .commit();
                    break;

                case "add report":
                    addrep.speak("navigating to add report page.", TextToSpeech.QUEUE_FLUSH, null);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new ReportsShowFragment())
                            .addToBackStack(null)
                            .commit();
                    break;

                case "capture":


                default:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Uh oh! Could not navigate to desired page", Toast.LENGTH_LONG).show();
                            invalid.speak("Uh oh! Could not navigate to desired page", TextToSpeech.QUEUE_FLUSH, null);
                            openDialog();
                        }
                    }, 1000);
            }

        }


    }

    public void openDialog(){
        VoiceDialog voiceDialog=new VoiceDialog();
        voiceDialog.show(getSupportFragmentManager(), "voice dialog");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (medicine!=null){
            medicine.stop();
            medicine.shutdown();
        }
        if (report!=null){
            report.stop();
            report.shutdown();
        }
        if (places!=null){
            places.stop();
            places.shutdown();
        }
        if (yogapg!=null){
            yogapg.stop();
            yogapg.shutdown();
        }
        if (medlist!=null){
            medlist.stop();
            medlist.shutdown();
        }
        if (replist!=null){
            replist.stop();
            replist.shutdown();
        }
        if (addmed!=null){
            addmed.stop();
            addmed.shutdown();
        }
        if (addrep!=null){
            addrep.stop();
            addrep.shutdown();
        }


    }
}
