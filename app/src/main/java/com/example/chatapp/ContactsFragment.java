package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    RecyclerView recyclerview;
    AdapterUsers adapterUsers;
    List<ModelUsers> userlist;
    Toolbar searchToolbar;
    SearchView searchView;



    public ContactsFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.activity_show_all_users, container, false);
//        searchToolbar=view.findViewById(R.id.searchToolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        recyclerview=view.findViewById(R.id.user_recycler_view);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView=view.findViewById(R.id.searchuserview);
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


        // Inflate the layout for this fragment

        return  view;

    }


    private void searchUsers(final String query) {
        //get current user
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        //path of users containing info
        DatabaseReference databaseReference= FirebaseDatabase.getInstance()
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
                    adapterUsers=new AdapterUsers(getActivity(),userlist);

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
                    adapterUsers=new AdapterUsers(getActivity(),userlist);
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


