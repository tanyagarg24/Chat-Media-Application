package com.example.chatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.ChatActivity;
import com.example.chatapp.models.ModelUsers;
import com.example.chatapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.Myholder> {

    Context context;
    List<ModelUsers> userlist;

    public AdapterUsers(Context context, List<ModelUsers> userlist) {
        this.context = context;
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        //inflate layout users_list_layout
        View view = LayoutInflater.from(context).inflate(R.layout.users_list_layout, parent,false);


        return new Myholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Myholder holder, final int position) {
        //get data
        final String hisUid=userlist.get(position).getUid();

        String userprofilepic=userlist.get(position).getImage();
        final String username=userlist.get(position).getName();
        //set data
        holder.username.setText(username);
        try {
            Picasso.get().load(userprofilepic)
                    .placeholder(R.drawable.profile_image)
                    .into(holder.profilepic);

        }
        catch (Exception e){

        }


            //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, ""+username, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid",hisUid);
                context.startActivity(intent);


            }


        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();

    }

    class Myholder extends RecyclerView.ViewHolder{
        ImageView profilepic;
        TextView username;



        public Myholder(@NonNull View itemView) {
            super(itemView);
            profilepic=itemView.findViewById(R.id.usersprofilepic);
            username=itemView.findViewById(R.id.username);

        }
    }
}
