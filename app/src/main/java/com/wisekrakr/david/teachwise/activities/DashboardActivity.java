package com.wisekrakr.david.teachwise.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wisekrakr.david.teachwise.MainActivity;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.fragments.HomeFragment;
import com.wisekrakr.david.teachwise.fragments.NotificationFragment;
import com.wisekrakr.david.teachwise.fragments.ProfileFragment;
import com.wisekrakr.david.teachwise.fragments.SearchFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseUser user;

    private Fragment selectedFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //init firebase user

        user = FirebaseAuth.getInstance().getCurrentUser();

        //bottom navigation
        //views
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener());

        Bundle intent = getIntent().getExtras();
        if(intent != null){
            String publisher = intent.getString("publisherId");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileId", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();

        }else{
            //home fragment transaction as default
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }


    }



    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener(){
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //handle item clicks
                switch (item.getItemId()){
                    case R.id.nav_home:
                        //home fragment transaction
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_profile:
                        //profile fragment transaction
                        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                        editor.putString("profile_id", user.getUid());
                        editor.apply();
                        selectedFragment = new ProfileFragment();
                        break;
                    case R.id.nav_add:
                        //add fragment transaction
                        selectedFragment = null;
                        startActivity(new Intent(DashboardActivity.this, PostActivity.class));
                        break;
                    case R.id.nav_search:
                        //search fragment transaction
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.nav_heart:
                        //favorites fragment transaction
                        selectedFragment = new NotificationFragment();
                        break;
                }

                if(selectedFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }

                return true;
            }
        };
    }

    private void checkUserStatus(){
        // check for user
        if(user != null){
            //user is signed in, stay here
            //set email of logged user


        }else{
            //user is not signed in, go back to main
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onStart() {
        // Check if user is signed in (non-null)
        checkUserStatus();
        super.onStart();
    }




}
