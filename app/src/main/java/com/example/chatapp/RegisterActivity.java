 package com.example.chatapp;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

 public class RegisterActivity extends AppCompatActivity {
    Button regbtn;
    EditText emailet2,pwdet2;
    TextView alreadyuser;
    FirebaseAuth auth;
    ProgressBar progbar;
    TextView setProgress;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        regbtn=findViewById(R.id.regbtn);
        emailet2=findViewById(R.id.emailet2);
        pwdet2=findViewById(R.id.pwdet2);
        alreadyuser=findViewById(R.id.alreadyuser);
        progbar=new ProgressBar(this);


        alreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(loginIntent);

            }
        });



        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailet2.getText().toString();
                String password=pwdet2.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Please enter email!", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                }
                else{
//                    progbar.setProgress(pStatus);
//                    txtProgress.setText(pStatus + " %");
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){


                                        String currentuid=auth.getCurrentUser().getUid();
                                        databaseReference.child("users").child(currentuid).setValue("");
                                        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String message=task.getException().toString();
                                        Toast.makeText(RegisterActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }


            }
        });


    }

}
