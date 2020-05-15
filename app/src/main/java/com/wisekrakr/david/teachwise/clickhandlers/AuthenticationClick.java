package com.wisekrakr.david.teachwise.clickhandlers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.wisekrakr.david.teachwise.activities.LoginActivity;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.activities.RegisterActivity;

public class AuthenticationClick implements AuthenticationClickContext {
    // views
    private Button registerBtn, loginBtn;

    private Activity app;

    public AuthenticationClick(Activity app) {
        this.app = app;
    }


    @Override
    public void init() {
        //init views
        registerBtn = app.findViewById(R.id.register);
        loginBtn = app.findViewById(R.id.login);
    }

    @Override
    public void login() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start LoginActivity
                app.startActivity(new Intent(app, LoginActivity.class));
            }
        });
    }

    @Override
    public void register() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start RegisterActivity
                app.startActivity(new Intent(app, RegisterActivity.class));
            }
        });
    }
}
