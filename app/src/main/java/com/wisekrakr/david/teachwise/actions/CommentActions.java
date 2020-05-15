package com.wisekrakr.david.teachwise.actions;

import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.models.UserModel;
import com.wisekrakr.david.teachwise.utils.UserImage;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class CommentActions implements CommentActionsContext {

    private FirebaseUser user;

    public CommentActions() {
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void addComment(EditText comment, String postId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", comment.getText().toString());
        hashMap.put("publisher", user.getUid());

        reference.push().setValue(hashMap);
        comment.setText("");
    }

    @Override
    public void getAvatar(final ImageView image) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

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
