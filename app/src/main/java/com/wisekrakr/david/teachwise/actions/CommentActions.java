package com.wisekrakr.david.teachwise.actions;

import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.adapters.CommentAdapter;
import com.wisekrakr.david.teachwise.models.CommentModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

public class CommentActions implements CommentActionsContext {

    private FirebaseUser user;
    private List<CommentModel> commentList;
    private CommentAdapter commentAdapter;

    public CommentActions(List<CommentModel> commentList, CommentAdapter commentAdapter) {
        this.commentList = commentList;
        this.commentAdapter = commentAdapter;

        user = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    public void addComment(EditText comment, String postId, String publisherId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", comment.getText().toString());
        hashMap.put("publisher", user.getUid());
        hashMap.put("createdAt", new Date().getTime());

        reference.push().setValue(hashMap);

        NotificationActionsStatic.addNotificationOnComment(publisherId, comment.getText().toString(), postId);

        comment.setText("");
    }



    @Override
    public void getComments(String postId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    CommentModel commentModel = snapshot.getValue(CommentModel.class);
                    commentList.add(commentModel);
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
