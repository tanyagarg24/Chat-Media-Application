package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    Button updatesettingsbtn;
    EditText setusername,setabout;
    CircleImageView chngprofilepic;
    String currentuid;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private  static final int gallerypic=1;
    TextView myposts;
    StorageReference userprofilepicref;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        auth=FirebaseAuth.getInstance();
        currentuid=auth.getCurrentUser().getUid();
        myposts=findViewById(R.id.my_posts);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        userprofilepicref= FirebaseStorage.getInstance().getReference().child("Progile Images");





        updatesettingsbtn=findViewById(R.id.updatesettingsbtn);
        setusername=findViewById(R.id.setusername);
        setabout=findViewById(R.id.setuserabout);
        chngprofilepic=findViewById(R.id.setprofile_image);
//        setusername.setVisibility(View.INVISIBLE);
        updatesettingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();


            }
        });
        Retrieveuserinfo();

        chngprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent galleryintent=new Intent();
                        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryintent.setType("image/'");
                        startActivityForResult(galleryintent,gallerypic);
            }
        });

        myposts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToViewMyPosts();
            }
        });

    }

    private void sendUserToViewMyPosts() {
        Intent mypostIntent=new Intent(SettingsActivity.this,MypostsActivity.class);
        startActivity(mypostIntent);

    }

    private void Retrieveuserinfo() {
        databaseReference.child("users").child(currentuid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image")){
                            String retrieveusername=dataSnapshot.child("name").getValue().toString();
                            String retrieveuserabout=dataSnapshot.child("about").getValue().toString();
                            String retrieveprofileimage=dataSnapshot.child("image").getValue().toString();

                            setusername.setText(retrieveusername);
                            setabout.setText(retrieveuserabout);
                           // Picasso.get().load("retrieveprofileimage").into(chngprofilepic);
//                            Glide.with(SettingsActivity.this).load(retrieveprofileimage).into(chngprofilepic);


                        }
                        else if (dataSnapshot.exists() && dataSnapshot.hasChild("name")){
                            String retrieveusername=dataSnapshot.child("name").getValue().toString();
                            String retrieveuserabout=dataSnapshot.child("about").getValue().toString();
                            setusername.setText(retrieveusername);
                            setabout.setText(retrieveuserabout);

                        }
                        else{
                            Toast.makeText(SettingsActivity.this, "Please set or updatae user info.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallerypic && resultCode==RESULT_OK && data!=null){
            Uri imageUri=data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                Uri resultUri=result.getUri();
                StorageReference filepath=userprofilepicref.child(currentuid+".jpg");
                filepath.putFile(resultUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this, "Profile picture uploaded", Toast.LENGTH_LONG).show();
                            final String downloadUrl =  task.getResult().getStorage().getDownloadUrl().toString();
                            databaseReference.child("users").child(currentuid).child("image").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(SettingsActivity.this, "Image stored in database", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                String message=task.getException().toString();
                                                Toast.makeText(SettingsActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        }
                        else {
                            String message=task.getException().toString();
                            Toast.makeText(SettingsActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();


                        }

                    }
                });

            }



            }

        }



    private void UpdateSettings() {
        final String setuserr=setusername.getText().toString();
        final String setaboutt=setabout.getText().toString();
        if (TextUtils.isEmpty(setuserr)){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setaboutt)){
            Toast.makeText(this, "Please enter about content", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String ,String> profilemap=new HashMap<>();
            profilemap.put("uid",currentuid);
            profilemap.put("name",setuserr);
            profilemap.put("about",setaboutt);
            databaseReference.child("users").child(currentuid).setValue(profilemap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                sendUserToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            }
                            else{
//                                setusername.setVisibility(View.VISIBLE);
                                String message=task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    }
    private void sendUserToMainActivity() {
        Intent mainIntent=new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
