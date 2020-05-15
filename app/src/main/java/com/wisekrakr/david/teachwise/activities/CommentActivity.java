package com.wisekrakr.david.teachwise.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.CommentActions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CommentActivity extends AppCompatActivity {

    private EditText addCommentText;
    private ImageView avatarImage;
    private Button postCommentBtn;

    private String postId;
    private String publisherId;

    private CommentActions commentActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //init comment actions
        commentActions = new CommentActions();

        addCommentText = findViewById(R.id.add_comment);
        avatarImage = findViewById(R.id.user_avatar);
        postCommentBtn = findViewById(R.id.post_comment);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        publisherId = intent.getStringExtra("publisherId");

        postCommentHandler();

    }

    private void postCommentHandler(){
        commentActions.getAvatar(avatarImage);
        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addCommentText.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this, "Please say something before posting", Toast.LENGTH_SHORT).show();
                }else{
                    commentActions.addComment(addCommentText, postId);
                }
            }
        });
    }



}
