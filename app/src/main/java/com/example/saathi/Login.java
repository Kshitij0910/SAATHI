package com.example.saathi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import io.realm.ObjectServerError;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static com.example.saathi.Constants.AUTH_URL;

public class Login extends AppCompatActivity {

    EditText emailId, password;
    Button login;
    TextView registerBtn, resetPassword, authView;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    LottieAnimationView unlock;

    private static final String TAG="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        authView=findViewById(R.id.Login);
        emailId=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.Login_Button);
        registerBtn=findViewById(R.id.New_Registration);
        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_Bar);
        resetPassword=findViewById(R.id.reset_password);

        unlock=findViewById(R.id.anim_unlock);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=emailId.getText().toString().trim();
                final String passWord=password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    emailId.setError("PLEASE ENTER YOUR EMAIL-ID TO LOGIN.");
                    return;
                }
                if(TextUtils.isEmpty(passWord)){
                    password.setError("PASSWORD IS REQUIRED TO LOGIN.");
                    return;
                }
                else if(passWord.length()<8){
                    password.setError("PASSWORD MUST BE AT LEAST 8 CHARACTERS LONG");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                //Authenticate the user.
                fAuth.signInWithEmailAndPassword(email, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            SyncCredentials credentials=SyncCredentials.usernamePassword(email, passWord, false);
                            SyncUser.logInAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
                                @Override
                                public void onSuccess(SyncUser syncUser) {
                                    //User logged
                                    Log.d(TAG, "onSuccess: User logged in");
                                    authView.setVisibility(View.INVISIBLE);
                                    emailId.setVisibility(View.INVISIBLE);
                                    password.setVisibility(View.INVISIBLE);
                                    registerBtn.setVisibility(View.INVISIBLE);
                                    resetPassword.setVisibility(View.INVISIBLE);
                                    login.setVisibility(View.INVISIBLE);

                                    Toast.makeText(Login.this,"Logged in successfully! WELCOME!",Toast.LENGTH_SHORT).show();
                                    unlock.setVisibility(View.VISIBLE);
                                    unlock.playAnimation();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        }
                                    },2000);
                                }

                                @Override
                                public void onError(ObjectServerError error) {
                                    //Handle error
                                    Log.e(TAG, "onError: Uh oh! Something went wrong.",error );
                                }
                            });
                            /*authView.setVisibility(View.INVISIBLE);
                            emailId.setVisibility(View.INVISIBLE);
                            password.setVisibility(View.INVISIBLE);
                            registerBtn.setVisibility(View.INVISIBLE);
                            resetPassword.setVisibility(View.INVISIBLE);
                            login.setVisibility(View.INVISIBLE);

                            Toast.makeText(Login.this,"Logged in successfully! WELCOME!",Toast.LENGTH_SHORT).show();
                            unlock.setVisibility(View.VISIBLE);
                            unlock.playAnimation();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            },2000);*/


                        }

                        else {
                            Toast.makeText(Login.this,"ERROR! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });



        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail=new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your registered Email-id to receive the reset link!");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link
                        String mail=resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Reset Link sent to your Email!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"ERROR! Could not send the Reset Link" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog.
                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }
}

