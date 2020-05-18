package com.wisekrakr.david.teachwise.actions;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.models.UserModel;
import com.wisekrakr.david.teachwise.utils.UserImage;

import androidx.annotation.NonNull;


public class UserActionsStatic {


    public static void getUserData(final ImageView avatarImage, final TextView username, String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                UserImage.setPicassoImageWithPlaceHolder(userModel.getAvatar(), avatarImage, R.drawable.ic_person_black);
                username.setText(userModel.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getPublisherData(final ImageView avatarImage, final TextView username,final TextView publisher, String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                UserImage.setPicassoImageWithPlaceHolder(userModel.getAvatar(), avatarImage, R.drawable.ic_person_black);
                username.setText(userModel.getUsername());
                publisher.setText(userModel.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getProfileData(final ImageView avatarImage, final TextView username,final TextView fullName,final TextView bio, String profileId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                UserImage.setPicassoImageWithPlaceHolder(userModel.getAvatar(), avatarImage, R.drawable.ic_person_black);
                username.setText(userModel.getUsername());
                fullName.setText(userModel.getFullName());
                bio.setText(userModel.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getAvatar(final ImageView image,String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                UserImage.setPicassoImage(userModel.getAvatar(), image, R.drawable.ic_person_black);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
