package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.adapters.AdapterChat;
import com.example.chatapp.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import android.widget.Toolbar;


public class ChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileimage;
    TextView nametv,onlinest;
    EditText messageEt;
    ImageButton sendbtn;
    FirebaseAuth auth;
    String hisUid;
    String myUid;
    String hisimage;
    List<ModelChat> chatList;
    AdapterChat adapterChat;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar=findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView=findViewById(R.id.chat_recyclerview);
        profileimage=findViewById(R.id.profileimageview);
        nametv=findViewById(R.id.nametextview);
        onlinest=findViewById(R.id.useronlinestatus);
        messageEt=findViewById(R.id.messageEt);
        sendbtn=findViewById(R.id.sendbtn);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        auth= FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();

        Intent intent=getIntent();
        hisUid=intent.getStringExtra("hisUid");

        myUid=user.getUid();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");

        Query query=databaseReference.orderByChild("uid").equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String name= ""+ds.child("name").getValue();
                    hisimage= ""+ds.child("image").getValue();

                    nametv.setText(name);
                    try{
                        Picasso.get().load(hisimage).placeholder(R.drawable.profile_image).into(profileimage);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.profile_image).into(profileimage);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messagee=messageEt.getText().toString().trim();
                if (TextUtils.isEmpty(messagee)){
                    Toast.makeText(ChatActivity.this, "Can't send empty message!", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendMessage(messagee);

                }

            }
        });

            readMessage();





    }
    private void readMessage() {
        chatList=new ArrayList<>();
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Log.i("error404","3");


                    chatList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren() ) {

                    ModelChat chat = ds.getValue(ModelChat.class);


                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) ||
                            chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {
                        chatList.add(chat);

                    }
                    adapterChat = new AdapterChat(ChatActivity.this, chatList, hisimage);
                    adapterChat.notifyDataSetChanged();

                    recyclerView.setAdapter(adapterChat);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    private void sendMessage(String messagee){


        DatabaseReference messagereference=FirebaseDatabase.getInstance().getReference();

       String timestamp=String.valueOf(System.currentTimeMillis());
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",messagee);
        hashMap.put("timestamp",timestamp);

        messagereference.child("Chats").push().setValue(hashMap);
        messageEt.setText("");
        //reset edittext after sending message

           }

}