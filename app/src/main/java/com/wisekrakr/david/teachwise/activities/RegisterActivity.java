package com.wisekrakr.david.teachwise.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.AuthActions;

import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "RegisterActivity";

    // views
    private EditText usernameText, fullNameText, emailText, passwordText;
    private Button signUpBtn;
    private TextView alreadyMember;

    //actions
    private AuthActions authActions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //init views
        emailText = findViewById(R.id.email_input);
        passwordText = findViewById(R.id.password_input);
        usernameText = findViewById(R.id.username_input);
        fullNameText = findViewById(R.id.full_name_input);
        signUpBtn = findViewById(R.id.signUp_btn);
        alreadyMember = findViewById(R.id.alreadyMember_text);

        //init actions
        authActions = new AuthActions();

        //handle sign up button click
        signUpClick();

        //handle login textview clicklistener
        alreadyMemberClick();
    }

    private void signUpClick(){
        signUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString().trim();
                String fullName = fullNameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(fullName) ){
                    usernameText.setError("These fields are required");
                    emailText.setFocusable(true);
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //set error and focus on email edittext
                    emailText.setError("Invalid Email");
                    emailText.setFocusable(true);
                }else if(password.length()<8){
                    //set error and focus on password edittext
                    passwordText.setError("Password length must be greater than 7 characters");
                    passwordText.setFocusable(true);
                }else{
                    registerUser(username, fullName, email, password);
                }
            }
        });
    }

    private void alreadyMemberClick(){
        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser(final String username, final String fullName, String email, String password) {
        //when email and password are valid register the user and show progress bar

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, dismiss progressbar and start register activity
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

                            //handle database entry for users
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", user.getUid());
                            hashMap.put("email", user.getEmail());
                            hashMap.put("username", username.toLowerCase());
                            hashMap.put("fullName", fullName);
                            hashMap.put("bio", "");
                            hashMap.put("location", "");
                            hashMap.put("avatar", "https://firebasestorage.googleapis.com/v0/b/teachwise.appspot.com/o/profile-placeholder.png?alt=media&token=4213869f-a732-4929-ab21-91a4cc754e21");
                            hashMap.put("createdAt", new Date().getTime());

                            authActions.createUser(reference, hashMap, RegisterActivity.this);

                            //registering user successful, start profile activity
                            Toast.makeText(RegisterActivity.this, "Registered \n" +user.getEmail(), Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error, dismiss progress bar and get and show the error message
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go to previous activity
        return super.onSupportNavigateUp();
    }
}
