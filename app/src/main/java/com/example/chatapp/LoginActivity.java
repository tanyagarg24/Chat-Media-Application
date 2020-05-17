package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseUser currentuser;
    Button loginbtn;
    EditText emailet,pwdet;
    TextView createaccount,forgrtpwd;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();

        currentuser=auth.getCurrentUser();
        loginbtn = findViewById(R.id.loginbtn);
        emailet=findViewById(R.id.emailet);
        pwdet=findViewById(R.id.pwdet);
        createaccount=findViewById(R.id.createaccount);
        forgrtpwd=findViewById(R.id.alreadyuser);

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegActivity();


            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailet.getText().toString();
                String password=pwdet.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Please enter email!", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        sendUserToMainActivity();
                                        Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String message=task.getException().toString();
                                        Toast.makeText(LoginActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

            }
        });
    }
//
//    @Override
//    protected void onStart() {
//
//        super.onStart();
//        if (currentuser!=null){
//            sendUserToMainActivity();
//        }
//
//    }

    private void sendUserToMainActivity() {
        Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void sendUserToRegActivity() {
        Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(regIntent);
    }
}
