package com.example.chatapp;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


import androidx.appcompat.widget.Toolbar;
//
//import android.widget.Toolbar;

import com.example.chatapp.adapters.AdapterUsers;
import com.example.chatapp.models.ModelUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowAllUsers extends AppCompatActivity {
    RecyclerView recyclerview;
    AdapterUsers adapterUsers;
    List<ModelUsers> userlist;
    Toolbar searchToolbar;
    SearchView searchView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_all_users);
        recyclerview=findViewById(R.id.user_recycler_view);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        searchToolbar=findViewById(R.id.searchToolbar);
        setSupportActionBar(searchToolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search People");

        searchView=findViewById(R.id.searchuserview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim())){
                    searchUsers(query);

                }
                else {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {


                if (!TextUtils.isEmpty(query.trim())){
                    searchUsers(query);


                }
                else {
                    getAllUsers();
                }
                return false;
            }
        });

        //init userlist
        userlist=new ArrayList<>();

        //get all users
        getAllUsers();
    }

    private void searchUsers(final String query) {
        //get current user
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        //path of users containing info
        DatabaseReference databaseReference=FirebaseDatabase.getInstance()
                .getReference("users");
        //get all users data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren() ){
                    ModelUsers modelUsers=ds.getValue(ModelUsers.class);
                    try {//get all searched users
                        if (!modelUsers.getUid().equals(user.getUid())) {
                            if (modelUsers.getName().toLowerCase().contains(query.toLowerCase())){
                                userlist.add(modelUsers);


                            }
//                            userlist.add(modelUsers);



                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    adapterUsers=new AdapterUsers(ShowAllUsers.this,userlist);

                    //refresh adapter
                    adapterUsers.notifyDataSetChanged();
                    //set adapter
                    recyclerview.setAdapter(adapterUsers);
                }
                Log.d("tanyagarg",""+userlist.get(0).getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getAllUsers() {
        //get current user
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        //path of users containing info
        DatabaseReference databaseReference=FirebaseDatabase.getInstance()
                .getReference("users");
        //get all users data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren() ){
                    ModelUsers modelUsers=ds.getValue(ModelUsers.class);
                    try {//get all users except currently signed in

                        if (!modelUsers.getUid().equals(user.getUid())) {
                            userlist.add(modelUsers);


                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                   adapterUsers=new AdapterUsers(ShowAllUsers.this,userlist);
                    //set adapter
                    recyclerview.setAdapter(adapterUsers);
                }
                Log.d("tanyagarg",""+userlist.get(0).getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}