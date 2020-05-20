package com.wisekrakr.david.teachwise.actions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NotificationActionsStatic {

    public static void addNotificationOnPost(String userId, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);

        HashMap<String, Object>hashMap = new HashMap<>();
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("postId", postId);
        hashMap.put("comment", "liked your post");
        hashMap.put("isPost", true);

        reference.push().setValue(hashMap);
    }

    public static void addNotificationOnUser(String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);

        HashMap<String, Object>hashMap = new HashMap<>();
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("postId", "");
        hashMap.put("comment", "started following you");
        hashMap.put("isPost", false);

        reference.push().setValue(hashMap);
    }

    public static void addNotificationOnComment(String userId, String comment, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);

        HashMap<String, Object>hashMap = new HashMap<>();
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("comment", "commented: " + comment);
        hashMap.put("postId", postId);
        hashMap.put("isPost", true);

        reference.push().setValue(hashMap);
    }

}
