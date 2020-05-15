package com.wisekrakr.david.teachwise.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    // views
    private EditText emailText, passwordText;
    private Button signInBtn;
    private TextView notYetMember, recoverPasswordText;

    private AuthActions authActions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //init views
        emailText = findViewById(R.id.email_input);
        passwordText = findViewById(R.id.password_input);
        recoverPasswordText = findViewById(R.id.recover_password_text);
        signInBtn = findViewById(R.id.signIn_btn);
        notYetMember = findViewById(R.id.notYetMember_text);

        //init actions
        authActions = new AuthActions();

        //handle sign in button click
        signInClick();

        //handle register textview clicklistener
        notYetMemberClick();

        //handle recover password textview clicklistener
        recoverPasswordClick();
    }

    private void signInClick(){
        signInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //set error and focus on email edittext
                    emailText.setError("Invalid Email");
                    emailText.setFocusable(true);
                }
                else if(password.length()<8){
                    //set error and focus to password edittext
                    passwordText.setError("Password length must be greater than 7 characters");
                    passwordText.setFocusable(true);
                }else{
                    loginUser(email, password);
                }
            }
        });

    }


    private void loginUser(String email, String password) {
        //when email and password are valid, login the user and show progress bar

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, dismiss progressbar and start login activity
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(user.getUid());

                            authActions.authenticateUser(reference, LoginActivity.this);

                            //user is logged in, start profile activity
                            Toast.makeText(LoginActivity.this, "Logged in \n" +user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error, dismiss progress bar and get and show the error message
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void notYetMemberClick(){
        notYetMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    private void recoverPasswordClick(){
        recoverPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Recover Password");

                //set linear layout
                LinearLayout linearLayout = new LinearLayout(LoginActivity.this);

                //view to set in dialog
                final EditText emailText = new EditText(LoginActivity.this);
                emailText.setHint("Email");
                emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                //sets a min width for the email Text View
                emailText.setMinEms(16);

                //add email to linear layout
                linearLayout.addView(emailText);
                linearLayout.setPadding(10,10,10,10);

                //add linear layout to builder and set view
                builder.setView(linearLayout);

                //recover button
                builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //input email
                        String email = emailText.getText().toString().trim();
                        startRecoveryProcess(email);
                    }
                });

                //cancel button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss dialog
                        dialog.dismiss();
                    }
                });

                //show dialog
                builder.create().show();
            }
        });
    }

    /**
     * Starts the recovery process for a password
     * @param email String
     */
    private void startRecoveryProcess(String email) {
        //when email is being send

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "It worked! Email sent", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Oh no, sending email failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //show error message
                        Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go to previous activity
        return super.onSupportNavigateUp();
    }


}
