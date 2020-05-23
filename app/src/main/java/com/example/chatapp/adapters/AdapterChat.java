package com.example.chatapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {
    private static final int MSG_RIGHT=1;
    private static final int MSG_LEFT=0;
    Context context;
    List<ModelChat> chatList;
    String  imageUrl;
    FirebaseUser user;


    public AdapterChat(Context context, List<ModelChat> chatList, String hisimage) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl=imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
            return new MyHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        String  message=chatList.get(position).getMessage();
        String timestamp=chatList.get(position).getTimestamp();
        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String datetime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").toString().
               format(timestamp);
        //String datetime=DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();


        holder.messagetv.setText(message);
        holder.timetv.setText(datetime);
        try {
            Picasso.get().load(imageUrl).into(holder.profileiv);
        }
            catch(Exception e){

            }


        holder.messagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this mesage?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();;
                    }
                });
                builder.create().show();



            }
        });


    }

    private void deleteMessage(int position) {

        final String muUid=FirebaseAuth.getInstance().getCurrentUser().getUid();


        String messagetimestamp=chatList.get(position).getTimestamp();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        Query query=databaseReference.orderByChild("timestamp").equalTo(messagetimestamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    if (ds.child("sender").getValue().equals(muUid)){
                        //ds.getRef().removeValue();
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("message","This message was deleted");
                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context, "Message Deleted", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(context, "You can't delete this message", Toast.LENGTH_SHORT).show();
                    }





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();

    }

    @Override
    public int getItemViewType(int position) {
        user= FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(user.getUid())){
            return MSG_RIGHT;

        }
        else {
            return MSG_LEFT;

        }

    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView messagetv,timetv;
        ImageView profileiv;
        LinearLayout  messagelayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            messagetv=itemView.findViewById(R.id.messagetv);
            timetv=itemView.findViewById(R.id.timetv);
            profileiv=itemView.findViewById(R.id.profileimageview);
            messagelayout=itemView.findViewById(R.id.messageLayoutshow);
        }
    }
}
