package com.wisekrakr.david.teachwise.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.ImageActions;
import com.wisekrakr.david.teachwise.actions.UserActionsStatic;
import com.wisekrakr.david.teachwise.adapters.ImageAdapter;
import com.wisekrakr.david.teachwise.models.PostModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private ImageAdapter imageAdapter;
    private RecyclerView recyclerView;
    private List<PostModel>imageList;
    private ImageActions imageActions;

    private ImageView userAvatar, options;
    private TextView posts, followers, following, fullName, bio, username;
    private Button editProfile;

    private FirebaseUser user;
    private String profileId;

    private ImageButton myImages, savedImages;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = prefs.getString("profileId", "none");

        userAvatar = view.findViewById(R.id.user_avatar);
        options = view.findViewById(R.id.options);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullName = view.findViewById(R.id.full_name);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        editProfile = view.findViewById(R.id.edit_profile);
        myImages = view.findViewById(R.id.my_images);
        savedImages = view.findViewById(R.id.saved_images);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        imageList = new ArrayList<>();

        imageAdapter = new ImageAdapter(getContext(), imageList);

        recyclerView.setAdapter(imageAdapter);

        imageActions = new ImageActions(imageAdapter, imageList);

        UserActionsStatic.getProfileData(userAvatar, username, fullName, bio, profileId);
        editProfile();
        getFollowsOrFollowing();
        getNumOfPosts();
        imageActions.getUserImages();

        if(profileId.equals(user.getUid())){
            editProfile.setText(R.string.edit_profile);
        }else{
            buttonSaysFollowOrFollowing();
            savedImages.setVisibility(View.GONE);
        }

        return view;
    }

    /**
     * Method after edit profile click
     */
    private void editProfile(){
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = editProfile.getText().toString();

                if(btn.equals(R.string.edit_profile)){
                    //to edit profile
                }else if(btn.equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid())
                            .child("following").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(user.getUid()).setValue(true);
                }else if(btn.equals("following")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid())
                            .child("following").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(user.getUid()).removeValue();
                }
            }
        });
    }

    private void buttonSaysFollowOrFollowing(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Follow")
                .child(user.getUid())
                .child("following");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(profileId).exists()){
                    editProfile.setText(R.string.following);
                }else{
                    editProfile.setText(R.string.follow);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowsOrFollowing(){
        final DatabaseReference followersRef = FirebaseDatabase.getInstance().getReference("Follow")
                .child(profileId)
                .child("followers");

        followersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText(" " + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference("Follow")
                .child(profileId)
                .child("following");

        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText(" " + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNumOfPosts(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    PostModel postModel = snapshot.getValue(PostModel.class);

                    if(postModel.getPublisher().equals(profileId)){
                        i++;
                    }
                }

                posts.setText(" " + i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
