package com.wisekrakr.david.teachwise.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.UserActions;
import com.wisekrakr.david.teachwise.adapters.UserAdapter;
import com.wisekrakr.david.teachwise.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FollowersActivity extends AppCompatActivity {

    private String id;
    private String title;

    private UserActions userActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<UserModel> userList = new ArrayList<>();

        UserAdapter userAdapter = new UserAdapter(this, userList, false);

        userActions = new UserActions(userAdapter, userList);

        recyclerView.setAdapter(userAdapter);

        handleListGetter();
    }

    private void handleListGetter(){


        switch (title){
            case "likes":
                userActions.getLikes(id);
                break;
            case "followers":
                userActions.getFollowers(id);
                break;
            case "following":
                userActions.getFollowing(id);
                break;
        }
    }
}
