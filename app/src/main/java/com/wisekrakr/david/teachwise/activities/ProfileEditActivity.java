package com.wisekrakr.david.teachwise.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.UserActionsStatic;
import com.wisekrakr.david.teachwise.models.UserModel;
import com.wisekrakr.david.teachwise.utils.Extensions;
import com.wisekrakr.david.teachwise.utils.ImageHandler;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity {

    private ImageView close, userAvatar;
    private TextView save, changeAvatar;
    private MaterialEditText fullName, username, bio, location;

    private FirebaseUser user;

    private Uri avatarUri;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        user = FirebaseAuth.getInstance().getCurrentUser();

        //init views
        close = findViewById(R.id.close);
        userAvatar = findViewById(R.id.user_avatar);
        save = findViewById(R.id.save_edit_profile);
        changeAvatar = findViewById(R.id.change_avatar);
        fullName = findViewById(R.id.full_name_input);
        username = findViewById(R.id.username_input);
        bio = findViewById(R.id.bio_input);
        location = findViewById(R.id.location_input);

        //init storage
        storageReference = FirebaseStorage.getInstance().getReference("avatars");

        //handle close
        close();
        //handle edit profile
        handleProfileEdit();
        //handle avatar change
        avatarCrop(changeAvatar);
        avatarCrop(userAvatar);
        //handle saving profile edit
        saveProfileEdit();
    }

    private void handleProfileEdit(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                fullName.setText(userModel.getFullName());
                username.setText(userModel.getUsername());
                bio.setText(userModel.getBio());
                location.setText(userModel.getLocation());
                ImageHandler.setPicassoImage(userModel.getAvatar(), userAvatar, R.drawable.ic_person_black);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Handles closing edit profile activity
     */
    private void close(){
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Method to start cropping activity
     * @param view View object that can be clicked on
     */
    private void avatarCrop(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfileEditActivity.this);
            }
        });
    }

    /**
     * Method to handle editing user profile
     */
    private void saveProfileEdit(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActionsStatic.updateUserData(
                        user.getUid(),
                        fullName.getText().toString(),
                        username.getText().toString(),
                        bio.getText().toString(),
                        location.getText().toString()
                );
                Toast.makeText(ProfileEditActivity.this,"Profile Updated!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * handles uploading a file (text or image)
     */
    private void uploadAvatar(){

        if(avatarUri != null){
            final StorageReference fileReference = storageReference
                    .child(System.currentTimeMillis() + "." + Extensions.getFileExtension(avatarUri, ProfileEditActivity.this));

            StorageTask uploadTask = fileReference.putFile(avatarUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String myUrl = downloadUri != null ? downloadUri.toString() : null;

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());


                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("avatar", "" + myUrl);

                        reference.updateChildren(hashMap);

                    }else{
                        Toast.makeText(ProfileEditActivity.this,"Editing profile failed", Toast.LENGTH_SHORT).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileEditActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(ProfileEditActivity.this,"No Image Selected", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Called after picking file from internal storage
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //When uploading an image, give the option to crop the image
        if(resultCode == Activity.RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            try {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                avatarUri = result.getUri();

                uploadAvatar();
            }catch (NullPointerException e){
                Toast.makeText(ProfileEditActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(ProfileEditActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
