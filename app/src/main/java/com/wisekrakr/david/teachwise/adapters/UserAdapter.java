package com.wisekrakr.david.teachwise.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.fragments.ProfileFragment;
import com.wisekrakr.david.teachwise.models.UserModel;
import com.wisekrakr.david.teachwise.utils.UserImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

    private Context context;
    private List<UserModel> userList;

    private FirebaseUser user;

    public UserAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;

        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final UserModel userModel = userList.get(position);

        holder.followBtn.setVisibility(View.VISIBLE);

        //get data
        String userAvatar = userModel.getAvatar();
        String userUsername = userModel.getUsername();
        String userFullName = userModel.getFullName();

        //set data
        holder.nameText.setText(userUsername);
        holder.fullNameText.setText(userFullName);
        UserImage.setPicassoImageWithPlaceHolder(userAvatar, holder.avatarImage, R.drawable.ic_person_black);

        //check if user is following
        isFollowing(userModel.getId(), holder.followBtn);

        //set visibility follow  button
        if(userModel.getId().equals(user.getUid())){
            holder.followBtn.setVisibility(View.GONE);
        }

        //handle item click
        viewItemClick(holder, userModel);

        //handle follow click
        followButtonClick(holder, userModel);
    }

    /**
     * Handles item clicks
     * @param holder where all the clickable view items are stored
     * @param userModel user model
     */
    private void viewItemClick(final MyViewHolder holder, final UserModel userModel){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileId", userModel.getId());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();

                Toast.makeText(context, " "+ userModel.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handles item clicks
     * @param holder where all the clickable view items are stored
     * @param userModel
     */
    private void followButtonClick(final MyViewHolder holder, final UserModel userModel){

        //init database reference
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Follow");

        holder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(holder.followBtn.getText().toString().equals("follow")){
                   databaseReference.child(user.getUid()).child("following").child(userModel.getId()).setValue(true);
                   databaseReference.child(userModel.getId()).child("followers").child(user.getUid()).setValue(true);
               }else{
                   databaseReference.child(user.getUid()).child("following").child(userModel.getId()).removeValue();
                   databaseReference.child(userModel.getId()).child("followers").child(user.getUid()).removeValue();
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView avatarImage;
        private TextView nameText, fullNameText;
        private Button followBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            avatarImage = itemView.findViewById(R.id.user_avatar);
            nameText = itemView.findViewById(R.id.username);
            fullNameText = itemView.findViewById(R.id.full_name);

            followBtn = itemView.findViewById(R.id.follow_btn);
        }
    }

    private void isFollowing(final String userId, final Button button){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(user.getUid()).child("following");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(userId).exists()){
                    System.out.println("data changed");

                    button.setText(R.string.following);
                }else{
                    button.setText(R.string.follow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
