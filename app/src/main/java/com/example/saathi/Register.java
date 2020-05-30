package com.example.saathi;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static com.example.saathi.Constants.AUTH_URL;
import static com.example.saathi.Constants.REALM_URL;



public class Register extends AppCompatActivity {

    EditText emailId, password, confirmPassword;
    Button register;
    TextView loginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    LottieAnimationView unlockReg;

    String email, passWord, confirmPassWord;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);






        emailId=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirm_password);
        register=findViewById(R.id.Register_Button);
        loginBtn=findViewById(R.id.Login_user);

        unlockReg=findViewById(R.id.anim_unlock);

        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_Bar);

        if(fAuth.getCurrentUser()!=null /*&& SyncUser.current()!=null*/){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email=emailId.getText().toString().trim();
                 passWord=password.getText().toString().trim();
                 confirmPassWord=confirmPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    emailId.setError("EMAIL ID IS A REQUIRED FIELD");
                    return;
                }
                if(TextUtils.isEmpty(passWord))
                {
                    emailId.setError("PASSWORD IS A REQUIRED FIELD");
                    return;
                }
                else if(passWord.length()<8)
                {
                    password.setError("PASSWORD MUST BE AT LEAST 8 CHARACTERS LONG");
                    return;
                }
                if(TextUtils.isEmpty(confirmPassWord))
                {
                    confirmPassword.setError("PLEASE RE-CONFIRM YOUR PASSWORD");
                    return;
                }
                else if(!confirmPassWord.equals(passWord))
                {
                    confirmPassword.setError("PASSWORD DOES NOT MATCH!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //Register the user.
                fAuth.createUserWithEmailAndPassword(email, confirmPassWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //SyncCredentials credentials=SyncCredentials.usernamePassword(email, confirmPassWord, true);
                            //SyncUser.logInAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
                                //@Override
                                //public void onSuccess(SyncUser syncUser) {
                                    Toast.makeText(Register.this,"User successfully created!", Toast.LENGTH_SHORT).show();
                                    unlockReg.setVisibility(View.VISIBLE);
                                    unlockReg.playAnimation();
                                   new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        }
                                    },2000);
                                //}

                                /*@Override
                                public void onError(ObjectServerError error) {
                                    Log.e("REGISTER", "onError: ",error );
                                }
                            });
                              */
                            //SyncUser user = SyncUser.current();
                            //String url=REALM_URL+"/~/userRealm";
                            //SyncConfiguration syncConfig=SyncUser.current().createConfiguration(url).fullSynchronization().build();
                            //Realm realmNew=Realm.getDefaultInstance();
                            //Toast.makeText(Register.this,"User successfully created!", Toast.LENGTH_SHORT).show();
                            //unlockReg.setVisibility(View.VISIBLE);
                            //unlockReg.playAnimation();
                            //new Handler().postDelayed(new Runnable() {
                              //  @Override
                                //public void run() {
                                  //  startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                //}
                            //},2000);

                        }
                        else{
                            Toast.makeText(Register.this, "ERROR! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });


        /*SyncCredentials credentials=SyncCredentials.usernamePassword(email, confirmPassWord);
        SyncUser.logInAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
            @Override
            public void onSuccess(SyncUser syncUser) {

            }

            @Override
            public void onError(ObjectServerError error) {

            }
        });
        */
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });


    }
}

