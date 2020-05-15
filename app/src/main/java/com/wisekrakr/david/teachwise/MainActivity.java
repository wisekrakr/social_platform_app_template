package com.wisekrakr.david.teachwise;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.wisekrakr.david.teachwise.activities.DashboardActivity;
import com.wisekrakr.david.teachwise.clickhandlers.AuthenticationClick;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private AuthenticationClick authenticationClick;

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        //init authentication
        authenticationClick = new AuthenticationClick(this);
        authenticationClick.init();
        authenticationClick.login();
        authenticationClick.register();
    }
}
