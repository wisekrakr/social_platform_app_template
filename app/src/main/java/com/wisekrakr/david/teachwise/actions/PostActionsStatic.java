package com.wisekrakr.david.teachwise.actions;

import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.models.PostModel;
import com.wisekrakr.david.teachwise.utils.ImageHandler;

import androidx.annotation.NonNull;

public class PostActionsStatic {

    public static void getPostImage(final ImageView image, String postId){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PostModel postModel = dataSnapshot.getValue(PostModel.class);

                ImageHandler.setPicassoImage(postModel.getPostImage(), image, R.drawable.ic_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
