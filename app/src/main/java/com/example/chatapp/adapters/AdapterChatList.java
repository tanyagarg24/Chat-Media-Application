package com.example.chatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.ChatActivity;
import com.example.chatapp.R;
import com.example.chatapp.models.ModelUsers;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.Myholder> {
    Context context;
    List<ModelUsers> usersList;
    private HashMap<String,String> lastmessagemap;

    public AdapterChatList(Context context, List<ModelUsers> usersList) {
        this.context = context;
        this.usersList = usersList;
        lastmessagemap = new HashMap<>();
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chat_list_layout,parent,false);

        return new Myholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        final String hisUid=usersList.get(position).getUid();
        String hisImage=usersList.get(position).getImage();
        String username=usersList.get(position).getName();
        String lastmessage=lastmessagemap.get(hisUid);

        holder.nametv.setText(username);
       if (lastmessage!=null || lastmessage.equals("default")){
           holder.lastmessagetv.setVisibility(View.GONE);


       }
       else {
           holder.lastmessagetv.setVisibility(View.VISIBLE);
           holder.lastmessagetv.setText(lastmessage);

       }
       try {
           Picasso.get().load(hisImage).placeholder(R.drawable.profile_image).into(holder.profileiv);

       }
       catch (Exception e){

           Picasso.get().load(R.drawable.profile_image).into(holder.profileiv);


       }
//       if (usersList.get(position).getOnlineStatus().equals("online")){
//           holder.onlinest.setImageResource(R.drawable.circle_online);
//
//       }
//       else {
//           holder.onlinest.setImageResource(R.drawable.circle_online);
//
//       }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //start chat with that user
               Intent intent=new Intent(context, ChatActivity.class);
               intent.putExtra("hisUid",hisUid);
               context.startActivity(intent);



           }
       });

    }

    public void setLastmessagemap(String userUid,String lastmessage){
        lastmessagemap.put(userUid,lastmessage);

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{
        ImageView profileiv;
        TextView nametv,lastmessagetv;



        public Myholder(@NonNull View itemView) {
            super(itemView);
            profileiv=itemView.findViewById(R.id.profileuser);
            //onlinest=itemView.findViewById(R.id.onlinestatususer);
            nametv=itemView.findViewById(R.id.namee);
            lastmessagetv=itemView.findViewById(R.id.lastmessage);



        }
    }
}
