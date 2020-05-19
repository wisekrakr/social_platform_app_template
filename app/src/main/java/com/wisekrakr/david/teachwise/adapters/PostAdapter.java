package com.wisekrakr.david.teachwise.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.wisekrakr.david.teachwise.actions.UserActionsStatic;
import com.wisekrakr.david.teachwise.activities.CommentActivity;
import com.wisekrakr.david.teachwise.fragments.PostFragment;
import com.wisekrakr.david.teachwise.fragments.ProfileFragment;
import com.wisekrakr.david.teachwise.models.PostModel;
import com.wisekrakr.david.teachwise.utils.CreatedAtFormatter;
import com.wisekrakr.david.teachwise.utils.UserImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{

    private Context context;
    private List<PostModel> postList;

    private FirebaseUser user;

    public PostAdapter(Context context, List<PostModel> postList) {
        this.context = context;
        this.postList = postList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        user = FirebaseAuth.getInstance().getCurrentUser();

        final PostModel postModel = postList.get(position);

        //if image was uploaded
        UserImage.setPicassoImageWithPlaceHolder(postModel.getPostImage(), holder.postImage, R.drawable.ic_image);


        //set data
        if(postModel.getTitle().equals("")){
            holder.title.setVisibility(View.GONE);
        }else if(postModel.getTags().equals("")){
            holder.tags.setVisibility(View.GONE);
        }else if(postModel.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else {

            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(postModel.getTitle());

            holder.tags.setVisibility(View.VISIBLE);
            holder.tags.setText(postModel.getTags());

            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(postModel.getDescription());

            String date = CreatedAtFormatter.getTimeDate(postModel.getCreatedAt());
            holder.createdAt.setVisibility(View.VISIBLE);
            holder.createdAt.setText(date);
        }

        //show publisher data
        UserActionsStatic.getPublisherData(holder.avatarImage, holder.username, holder.publisher, postModel.getPublisher());
        //show likes
        isLiked(postModel.getPostId(), holder.like);
        //show bookmarked
        isBookmarked(postModel.getPostId(), holder.bookmark);
        //show number likes
        numLikes(postModel.getPostId(), holder.likes);
        //show number comments
        numComments(postModel.getPostId(), holder.comments);
        //like click handler
        onClickLikeOrBookmark(holder, postModel);
        //comment click post handler
        setActivityToCommenting(postModel,holder.comment);
        //comments list handler
        setActivityToCommenting(postModel,holder.comments);
        //go to profile
        onClickGoToProfile(holder.avatarImage, postModel);
        onClickGoToProfile(holder.username, postModel);
        onClickGoToProfile(holder.publisher, postModel);
        //go to post
        onClickGoToPost(holder.postImage, postModel);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView avatarImage, postImage, like, comment, bookmark;

        private TextView username,  likes, title, tags, description, comments, publisher, createdAt;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            avatarImage = itemView.findViewById(R.id.user_avatar);
            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            bookmark = itemView.findViewById(R.id.bookmark);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            title = itemView.findViewById(R.id.title);
            tags = itemView.findViewById(R.id.tags);
            description = itemView.findViewById(R.id.description);
            comments = itemView.findViewById(R.id.comments);
            createdAt = itemView.findViewById(R.id.created_at);

        }
    }

    private void onClickLikeOrBookmark(final MyViewHolder holder, final PostModel post){
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(user.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(user.getUid()).removeValue();

                }
            }
        });

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.bookmark.getTag().equals("bookmark")){
                    FirebaseDatabase.getInstance().getReference().child("Bookmarked").child(user.getUid())
                            .child(post.getPostId()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Bookmarked").child(user.getUid())
                            .child(post.getPostId()).removeValue();

                }
            }
        });
    }

    private void setActivityToCommenting(final PostModel post, View view){
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
                comments.setText("View " + dataSnapshot.getChildrenCount() + " Comment(s)");
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

    private void isBookmarked(final String postId, final ImageView imageView){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Bookmarked").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postId).exists()){
                    imageView.setImageResource(R.drawable.ic_bookmarked);
                    imageView.setTag("bookmarked");
                }else{
                    imageView.setImageResource(R.drawable.ic_bookmark_border_black);
                    imageView.setTag("bookmark");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onClickGoToProfile(View view, final PostModel post){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileId", post.getPublisher());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();


            }
        });

    }

    private void onClickGoToPost(View view, final PostModel post){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postId", post.getPostId());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PostFragment()).commit();


            }
        });

    }
}
