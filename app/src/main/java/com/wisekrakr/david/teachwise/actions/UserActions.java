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

import java.util.List;

import androidx.annotation.NonNull;

public class UserActions implements UserActionsContext {
    public static final String TAG = "ProfileActions";

    private UserAdapter userAdapter;
    private List<UserModel> usersList;

    private DatabaseReference reference;
    private FirebaseUser user;

    public UserActions(UserAdapter userAdapter, List<UserModel> usersList) {
        this.userAdapter = userAdapter;
        this.usersList = usersList;

        reference = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
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



}
