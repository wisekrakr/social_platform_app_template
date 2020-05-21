package com.wisekrakr.david.teachwise.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.wisekrakr.david.teachwise.actions.ImageActionsStatic;
import com.wisekrakr.david.teachwise.actions.NotificationActionsStatic;
import com.wisekrakr.david.teachwise.actions.UserActionsStatic;
import com.wisekrakr.david.teachwise.activities.FollowersActivity;
import com.wisekrakr.david.teachwise.activities.OptionsActivity;
import com.wisekrakr.david.teachwise.activities.ProfileEditActivity;
import com.wisekrakr.david.teachwise.adapters.ImageAdapter;
import com.wisekrakr.david.teachwise.models.PostModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private List<String>userBookmarked;
    private ImageAdapter imageAdapterBookmark;
    private RecyclerView recyclerViewBookmark;
    private List<PostModel>imageListBookmark;

    private ImageView userAvatar, options;
    private TextView posts, followers, following, fullName, bio, location, username, atUsername;
    private Button editProfile;

    private FirebaseUser user;
    private String profileId;

    private ImageButton userImages, bookmarkedImages;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = prefs.getString("profileId", "none");

        userAvatar = view.findViewById(R.id.user_avatar);
        options = view.findViewById(R.id.options);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullName = view.findViewById(R.id.full_name);
        bio = view.findViewById(R.id.bio);
        location = view.findViewById(R.id.location);
        atUsername = view.findViewById(R.id.at_username);
        username = view.findViewById(R.id.username);
        editProfile = view.findViewById(R.id.edit_profile);
        userImages = view.findViewById(R.id.user_images);
        bookmarkedImages = view.findViewById(R.id.bookmarked_images);

//        User images recycler view
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        imageList = new ArrayList<>();

        imageAdapter = new ImageAdapter(getContext(), imageList);

        recyclerView.setAdapter(imageAdapter);

//        Bookmarked images recycler view
        recyclerViewBookmark = view.findViewById(R.id.recycler_view_bookmarked);
        recyclerViewBookmark.setHasFixedSize(true);
        LinearLayoutManager layoutManagerBookmarked = new GridLayoutManager(getContext(), 3);
        recyclerViewBookmark.setLayoutManager(layoutManagerBookmarked);

        imageListBookmark = new ArrayList<>();
        userBookmarked = new ArrayList<>();

        imageAdapterBookmark = new ImageAdapter(getContext(), imageListBookmark);

        recyclerViewBookmark.setAdapter(imageAdapterBookmark);

        UserActionsStatic.getProfileData(userAvatar, username, fullName, bio,location, atUsername, profileId);
        editProfile();
        getFollowsOrFollowing();
        getNumOfPosts();
        ImageActionsStatic.getUserImages(imageAdapter, imageList);
        ImageActionsStatic.getBookmarkedImages(imageAdapterBookmark, userBookmarked, imageListBookmark);
        showUserOrBookmarkedImageCollection();
        showFollowList(followers,"followers");
        showFollowList(following,"following");
        showOptions();

        if(profileId.equals(user.getUid())){
            editProfile.setText(R.string.edit_profile);
        }else{
            buttonSaysFollowOrFollowing();
            bookmarkedImages.setVisibility(View.GONE);
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

                if(btn.equals("Edit Profile")){
                    startActivity(new Intent(getContext(), ProfileEditActivity.class));
                }else if(btn.equals("follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid())
                            .child("following").child(profileId).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                            .child("followers").child(user.getUid()).setValue(true);

                    NotificationActionsStatic.addNotificationOnUser(profileId);
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

    private void showUserOrBookmarkedImageCollection(){
        recyclerView.setVisibility(View.VISIBLE);
        recyclerViewBookmark.setVisibility(View.GONE);

        userImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewBookmark.setVisibility(View.GONE);
            }
        });

        bookmarkedImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recyclerViewBookmark.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showFollowList(View view, String listName){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("title", listName);
                startActivity(intent);
            }
        });
    }

    private void showOptions(){
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OptionsActivity.class));
            }
        });
    }
}
