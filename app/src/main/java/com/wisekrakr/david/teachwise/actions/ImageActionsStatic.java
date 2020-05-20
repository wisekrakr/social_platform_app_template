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

public class ImageActionsStatic {


    public static void getUserImages(final ImageAdapter imageAdapter, final List<PostModel> list) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PostModel postModel = snapshot.getValue(PostModel.class);

                    if (postModel.getPublisher().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        list.add(postModel);
                    }
                }

                Collections.reverse(list);
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public static void getBookmarkedImages(final ImageAdapter imageAdapter, final List<String> list, final List<PostModel> imageList) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bookmarked")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    list.add(snapshot.getKey());
                }

                readBookmarked(imageAdapter, list, imageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static void readBookmarked(final ImageAdapter imageAdapter, final List<String> list, final List<PostModel> imageList){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PostModel postModel = snapshot.getValue(PostModel.class);

                    for(String id: list){
                        if(postModel.getPostId().equals(id)){
                            imageList.add(postModel);
                        }
                    }
                }

                imageAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
