package com.wisekrakr.david.teachwise.actions;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.activities.DashboardActivity;

import java.util.HashMap;

import androidx.annotation.NonNull;


public class AuthActions implements AuthActionsContext{
    public static final String TAG = "AuthActions";
    /**
     * Firebase database has to be initialized in the class before using this method.
     * @param reference
     * @param userMap
     * @param context
     *
     */
    @Override
    public void createUser(DatabaseReference reference, HashMap<String, Object> userMap, final Context context){
        //when user is registered store user data in firebase realtime database

        if(!userMap.isEmpty()){
            reference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(context, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed to register", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void authenticateUser(DatabaseReference reference, final Context context) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
