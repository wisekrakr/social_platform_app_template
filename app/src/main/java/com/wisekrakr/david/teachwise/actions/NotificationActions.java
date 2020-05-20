package com.wisekrakr.david.teachwise.actions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.adapters.NotificationAdapter;
import com.wisekrakr.david.teachwise.models.NotificationModel;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class NotificationActions implements NotificationActionsContext{

    private NotificationAdapter notificationAdapter;
    private List<NotificationModel>notificationList;

    public NotificationActions(NotificationAdapter notificationAdapter, List<NotificationModel> notificationList) {
        this.notificationAdapter = notificationAdapter;
        this.notificationList = notificationList;
    }

    @Override
    public void getNotifications() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    NotificationModel notificationModel = snapshot.getValue(NotificationModel.class);

                    notificationList.add(notificationModel);
                }

                Collections.reverse(notificationList);

                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
