package com.example.chatapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.chatapp.adapters.AdapterChatList;
import com.example.chatapp.models.ModelChat;
import com.example.chatapp.models.ModelChatList;
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
public class ChatFragment extends Fragment {
    RecyclerView chatrecyclerView;
    List<ModelChatList> chatlistList;
    List<ModelUsers> usersList;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    FirebaseAuth auth;
    AdapterChatList adapterChatList;









    public ChatFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        auth=FirebaseAuth.getInstance();
        currentUser=FirebaseAuth.getInstance().getCurrentUser();

        chatlistList=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(currentUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   chatlistList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    ModelChatList chatlist = ds.getValue(ModelChatList.class);
                    chatlistList.add(chatlist);

                }
                loadChats();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        chatrecyclerView=view.findViewById(R.id.chatListrecyclerView);


        return  view;



    }

    private void loadChats() {
        usersList=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelUsers users=ds.getValue(ModelUsers.class);
                    for (ModelChatList chatlist:chatlistList){
                        if (users.getUid()!=null && users.getUid().equals(chatlist.getId())){
                            usersList.add(users);
                            break;

                        }
                    }

                    adapterChatList=new AdapterChatList(getContext(),usersList);
                    chatrecyclerView.setAdapter(adapterChatList);
                    for (int i=0;i<usersList.size();i++){
                        lastMessage(usersList.get(i).getUid());
                        
                    }
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void lastMessage(final String uid) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastmessage="default";
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelChat chat=ds.getValue(ModelChat.class);
                    if (chat==null) {
                        continue;
                    }
                    String sender=chat.getSender();
                    String receiver =chat.getReceiver();
                    if(sender==null  || receiver==null){
                        continue;

                    }
                    if (chat.getReceiver().equals(currentUser.getUid()) && chat.getSender().equals(uid) ||
                    chat.getReceiver().equals(uid) && chat.getSender().equals(currentUser.getUid())){
                        lastmessage=chat.getMessage();



                    }
                }
                adapterChatList.setLastmessagemap(uid,lastmessage);
                    adapterChatList.notifyDataSetChanged();

                Log.i("tanyagarg",""+chatlistList.get(0).getId());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
