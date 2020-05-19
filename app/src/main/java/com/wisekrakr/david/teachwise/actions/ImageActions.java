package com.wisekrakr.david.teachwise.actions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.adapters.ImageAdapter;
import com.wisekrakr.david.teachwise.models.PostModel;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class ImageActions implements ImageActionsContext {

    private ImageAdapter imageAdapter;
    private List<PostModel> imageList;

    public ImageActions(ImageAdapter imageAdapter, List<PostModel> imageList) {
        this.imageAdapter = imageAdapter;
        this.imageList = imageList;
    }

    @Override
    public void getUserImages() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PostModel postModel = snapshot.getValue(PostModel.class);

                    if (postModel.getPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        imageList.add(postModel);
                    }
                }

                Collections.reverse(imageList);
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
