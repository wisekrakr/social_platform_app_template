package com.wisekrakr.david.teachwise.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.wisekrakr.david.teachwise.R;

import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity {
    public static final String TAG = "PostActivity";

    private Uri fileUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;

    //views
    private ImageView close, fileAdded;
    private TextView post;
    private EditText title, fieldOfStudy, studyContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        //init views
        close = findViewById(R.id.close);
        fileAdded = findViewById(R.id.file_added);
        post = findViewById(R.id.post);
        title = findViewById(R.id.title);
        fieldOfStudy = findViewById(R.id.field_of_study);
        studyContext = findViewById(R.id.study_context);

        //init storage
        storageReference = FirebaseStorage.getInstance().getReference("posts");

        //handle close click
        closePost();

        //handles clicking to add to large text field
        handleStudyContextInput();

        //handle post click
        handlePost();

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);
    }

    /**
     * Handles closing post activity
     */
    private void closePost(){
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, DashboardActivity.class));
                finish();
            }
        });
    }

    /**
     * Handles posting
     */
    private void handlePost(){
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
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
                fileUri = result.getUri();

                fileAdded.setImageURI(fileUri);
            }catch (NullPointerException e){
                Toast.makeText(PostActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(PostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, DashboardActivity.class));
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /**
     * handles uploading a file (text or image)
     */
    private void uploadFile(){

        if(fileUri != null){
            final StorageReference fileReference = storageReference
                    .child(System.currentTimeMillis() + "." + getFileExtension(fileUri));

            StorageTask uploadTask = fileReference.putFile(fileUri);
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
                        myUrl = downloadUri != null ? downloadUri.toString() : null;

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

                        String postId = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postId", postId);
                        hashMap.put("postImage", myUrl);
                        hashMap.put("title", title.getText().toString());
                        hashMap.put("fieldOfStudy", fieldOfStudy.getText().toString());
                        hashMap.put("studyContext", studyContext.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("createdAt", new Date().getTime());


                        reference.child(postId).setValue(hashMap);

                        startActivity(new Intent(PostActivity.this, DashboardActivity.class));
                        finish();
                    }else{
                        Toast.makeText(PostActivity.this,"Uploading image failed", Toast.LENGTH_SHORT).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(PostActivity.this,"No Image Selected", Toast.LENGTH_SHORT).show();

        }


    }


    private void handleStudyContextInput(){
        studyContext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

    }

}
