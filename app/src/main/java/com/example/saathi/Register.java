package com.example.saathi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class Register extends AppCompatActivity {

    EditText emailId, password, confirmPassword;
    Button register;
    TextView loginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2738A5")));

        emailId=findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirm_password);
        register=findViewById(R.id.Register_Button);
        loginBtn=findViewById(R.id.Login_user);

        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_Bar);

        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailId.getText().toString().trim();
                String passWord=password.getText().toString().trim();
                String confirmPassWord=confirmPassword.getText().toString().trim();

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
                            Toast.makeText(Register.this,"User successfully created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(Register.this, "ERROR! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}

