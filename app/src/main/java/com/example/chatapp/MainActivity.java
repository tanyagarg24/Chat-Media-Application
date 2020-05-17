

package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

//import android.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
   private ViewPager viewPager;
   private TabLayout tabLayout;
   private TabsAcessAdapter tabsAcessAdapter;
   FirebaseUser currentuser;
   FirebaseAuth auth;
   DatabaseReference databaseReference;
    private static final int gallerypic=1;
    StorageReference mypostsref;
    String currentuid;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();

        currentuser=auth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        currentuid=auth.getCurrentUser().getUid();
        mypostsref= FirebaseStorage.getInstance().getReference().child("My posts");

        toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat App");
        viewPager=findViewById(R.id.main_pager);
        tabsAcessAdapter=new TabsAcessAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAcessAdapter);
        tabLayout=findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onStart() {

        super.onStart();
        if (currentuser==null){
            sendUserToLoginActivity();
        }
        else{
            VerifyUserExistance();

        }

    }

    private void VerifyUserExistance() {
        String currentuid=auth.getCurrentUser().getUid();
        databaseReference.child("users").child(currentuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("name").exists())){
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendUserToSettingsActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void sendUserToLoginActivity() {
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_logout) {
            auth.signOut();
            sendUserToLoginActivity();
        }
        if (item.getItemId() == R.id.main_settings) {
            sendUserToSettingsActivity();
        }
        if (item.getItemId() == R.id.main_search_option) {
            sendUserToSearchUserActivity();

        }
        if (item.getItemId() == R.id.main_add_post) {
            sendUserToAddPost();


        }
        return  true;
    }


    private void sendUserToAddPost() {
        Intent galleryintent=new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/'");
        startActivityForResult(galleryintent,gallerypic);

    }

    private void sendUserToSettingsActivity() {
        Intent settingsIntent=new Intent(MainActivity.this,SettingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }
    private void sendUserToSearchUserActivity() {
        Intent searchIntent=new Intent(MainActivity.this, ShowAllUsers.class);
        startActivity(searchIntent);
    }





    //to crop image for posting
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
                StorageReference filepath=mypostsref.child(currentuid+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Post added", Toast.LENGTH_LONG).show();

                        }
                        else {
                            String message=task.getException().toString();
                            Toast.makeText(MainActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

        }
    }
}
