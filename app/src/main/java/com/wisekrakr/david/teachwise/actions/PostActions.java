package com.wisekrakr.david.teachwise.actions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.adapters.PostAdapter;
import com.wisekrakr.david.teachwise.models.PostModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class PostActions implements PostActionsContext {

    private PostAdapter postAdapter;
    private List<PostModel> postsList;
    private List<String> followingList;

    public PostActions(PostAdapter postAdapter, List<PostModel> postsList) {
        this.postAdapter = postAdapter;
        this.postsList = postsList;

        followingList = new ArrayList<>();
    }

    @Override
    public void getPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postsList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PostModel postModel = snapshot.getValue(PostModel.class);

                    for(String id: followingList){
                        if (postModel.getPublisher().equals(id)){
                            postsList.add(postModel);
                        }
                    }
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void checkFollowing() {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }

                getPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
