package com.wisekrakr.david.teachwise.actions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.adapters.UserAdapter;
import com.wisekrakr.david.teachwise.models.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;

public class UserActions implements UserActionsContext {
    public static final String TAG = "ProfileActions";
    private final ArrayList<String> idList;

    private UserAdapter userAdapter;
    private List<UserModel> usersList;

    private DatabaseReference reference;
    private FirebaseUser user;

    public UserActions(UserAdapter userAdapter, List<UserModel> usersList) {
        this.userAdapter = userAdapter;
        this.usersList = usersList;

        reference = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        idList = new ArrayList<>();
    }

    @Override
    public void showUsers() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserModel userModel = snapshot.getValue(UserModel.class);

                    for(String id: idList){
                        if(userModel.getId().equals(id)){
                            usersList.add(userModel);
                        }

                    }
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getAllUsers() {
        //get all data from path "Users"
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserModel userModel = snapshot.getValue(UserModel.class);

                    //get all profiles except current user

                    if(!userModel.getId().equals(user.getUid())){
                        usersList.add(userModel);
                    }

                    //TODO: possible bug (minSDK set to 24 instead of 21. Language to 8 instead of 6)
                    Collections.sort(usersList, Comparator.comparing(UserModel::getUsername));

                    //refresh adapter
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void searchUsers(final String searchQuery) {

        Query query = reference.orderByChild("username")
                .startAt(searchQuery)
                .endAt(searchQuery + "\uf8ff");

        //get all searched from path "Users"
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserModel userModel = snapshot.getValue(UserModel.class);

                    //User cannot be current user
                    //The username or email contains text entered in SearchView (case sensitive)
                    //get all profiles except current user
                    if(!userModel.getId().equals(user.getUid())){
                        if(userModel.getUsername().toLowerCase().contains(searchQuery.toLowerCase()) || userModel.getEmail().toLowerCase().contains(searchQuery.toLowerCase())){
                            usersList.add(userModel);
                        }
                    }
                    //refresh adapter
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getFollowers(String id) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(id).child("followers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void getFollowing(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(id).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getLikes(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Likes")
                .child(id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    idList.add(snapshot.getKey());
                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
