package com.example.saathi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailId, password;
    Button login;
    TextView registerBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailId=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.Login_Button);
        registerBtn=findViewById(R.id.New_Registrattion);
        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_Bar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailId.getText().toString().trim();
                String passWord=password.getText().toString().trim();

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
                fAuth.createUserWithEmailAndPassword(email, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Logged in successfully! WELCOME!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
    }
}

