package com.wisekrakr.david.teachwise.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.activities.CommentActivity;
import com.wisekrakr.david.teachwise.models.PostModel;
import com.wisekrakr.david.teachwise.models.UserModel;
import com.wisekrakr.david.teachwise.utils.UserImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{

    private Context context;
    private List<PostModel> postList;

    private FirebaseUser user;

    public PostAdapter(Context context, List<PostModel> postList) {
        this.context = context;
        this.postList = postList;

        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final PostModel postModel = postList.get(position);

        //if image was uploaded
        UserImage.setPicassoImageWithPlaceHolder(postModel.getPostImage(), holder.postImage, R.drawable.ic_person_black);


        //set data
        if(postModel.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else if(postModel.getFieldOfStudy().equals("")){
            holder.fieldOfStudy.setVisibility(View.GONE);
        }else if(postModel.getStudyContext().equals("")){
            holder.studyContext.setVisibility(View.GONE);
        }else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(postModel.getDescription());

            holder.fieldOfStudy.setVisibility(View.VISIBLE);
            holder.fieldOfStudy.setText(postModel.getFieldOfStudy());

            holder.studyContext.setVisibility(View.VISIBLE);
            holder.studyContext.setText(postModel.getStudyContext());
        }

        //show publisher data
        publisherData(holder.avatarImage, holder.username, holder.publisher, postModel.getPublisher());
        //show likes
        isLiked(postModel.getPostId(), holder.like);
        //show number likes
        numLikes(postModel.getPostId(), holder.likes);
        //show number comments
        numComments(postModel.getPostId(), holder.comments);
        //like click handler
        clickedLike(holder, postModel,user);
        //comment click post handler
        clickHandler(postModel,holder.comment);
        //comments list handler
        clickHandler(postModel,holder.comments);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView avatarImage, postImage, like, comment, save;
        private TextView username, publisher, likes, description, fieldOfStudy, studyContext, comments;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            avatarImage = itemView.findViewById(R.id.user_avatar);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            username = itemView.findViewById(R.id.username);
            publisher = itemView.findViewById(R.id.publisher);
            likes = itemView.findViewById(R.id.likes);
            description = itemView.findViewById(R.id.description);
            fieldOfStudy = itemView.findViewById(R.id.field_of_study);
            studyContext = itemView.findViewById(R.id.study_context);
            comments = itemView.findViewById(R.id.comments);

        }
    }

    private void clickedLike(final MyViewHolder holder, final PostModel post, final FirebaseUser user){
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId()).child(user.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId()).child(user.getUid()).removeValue();

                }
            }
        });
    }

    private void clickHandler(final PostModel post, View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("publisherId", post.getPublisher());
                context.startActivity(intent);

            }
        });
    }

    private void numLikes(String postId,final TextView likes ){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void numComments(String postId, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText("View All " + dataSnapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isLiked(String postId, final ImageView imageView){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                }else{
                    imageView.setImageResource(R.drawable.ic_fav_border_black);
                    imageView.setTag("like");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void publisherData(final ImageView avatarImage, final TextView username, final TextView publisher, String userId){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);

                    UserImage.setPicassoImageWithPlaceHolder(userModel.getAvatar(), avatarImage, R.drawable.ic_person_black);
                    username.setText(userModel.getUsername());
                    publisher.setText(userModel.getUsername());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
