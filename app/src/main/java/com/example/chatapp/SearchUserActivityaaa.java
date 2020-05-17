//package com.example.chatapp;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.appcompat.widget.Toolbar;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.squareup.picasso.Picasso;
//
//import de.hdodenhof.circleimageview.CircleImageView;
////import android.widget.Toolbar;
//
//
//public class SearchUserActivityaaa extends AppCompatActivity {
//    Toolbar searchToolbar;
//    RecyclerView searchRecyclerview;
//    DatabaseReference databaseReference;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_user);
//        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
//        searchToolbar=findViewById(R.id.searchToolbar);
//        searchRecyclerview=findViewById(R.id.searchRecyclerview);
//        searchRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        searchToolbar=findViewById(R.id.searchToolbar);
//        setSupportActionBar(searchToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Search People");
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<AllUsers> options=
//                new FirebaseRecyclerOptions.Builder<AllUsers>()
//                .setQuery(databaseReference,AllUsers.class)
//                .build();
//
//
//            FirebaseRecyclerAdapter<AllUsers,UsersRecyclerviewholder> adapter=
//                new FirebaseRecyclerAdapter<AllUsers,UsersRecyclerviewholder>(options){
//                    @Override
//                    protected void onBindViewHolder(@NonNull UsersRecyclerviewholder usersRecyclerviewholder, int i, @NonNull AllUsers allUsers) {
//                        usersRecyclerviewholder.username.setText(allUsers.getName());
//                        //Picasso.get().load(allUsers.getImage()).placeholder(R.drawable.profile_image).into(usersRecyclerviewholder.profileimgusers);
//                    }
//
//                    @NonNull
//                    @Override
//                    public UsersRecyclerviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_layout,parent,false);
//                        UsersRecyclerviewholder recyclerviewholder=new UsersRecyclerviewholder(view);
//                        return recyclerviewholder;
//                    }
//                };
//                    searchRecyclerview.setAdapter(adapter);
//                    adapter.startListening();
//    }
//    public class UsersRecyclerviewholder extends RecyclerView.ViewHolder{
//        TextView username;
//        CircleImageView profileimgusers;
//
//        public UsersRecyclerviewholder(@NonNull View itemView) {
//            super(itemView);
//            username=findViewById(R.id.username);
//            profileimgusers=findViewById(R.id.usersprofilepic);
//        }
//    }
//
//
//}