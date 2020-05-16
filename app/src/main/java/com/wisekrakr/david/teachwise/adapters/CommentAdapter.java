package com.wisekrakr.david.teachwise.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.UserActionsStatic;
import com.wisekrakr.david.teachwise.activities.CommentActivity;
import com.wisekrakr.david.teachwise.models.CommentModel;
import com.wisekrakr.david.teachwise.utils.CreatedAtFormatter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<CommentModel> commentList;

    private FirebaseUser user;

    public CommentAdapter(Context context, List<CommentModel> commentList) {
        this.context = context;
        this.commentList = commentList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        user = FirebaseAuth.getInstance().getCurrentUser();

        CommentModel commentModel = commentList.get(position);

        holder.commentText.setText(commentModel.getComment());

        String date = CreatedAtFormatter.getTimeDate(commentModel.getCreatedAt());
        holder.createdAtText.setText(date);

        //get user info
        UserActionsStatic.getUserData(holder.avatarImage, holder.nameText, commentModel.getPublisher());
        //clicked on comment
        clickHandler(commentModel, holder.commentText);
        //clicked on user avatar
        clickHandler(commentModel, holder.avatarImage);


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView avatarImage;
        private TextView nameText, commentText, createdAtText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            avatarImage = itemView.findViewById(R.id.user_avatar);
            nameText = itemView.findViewById(R.id.username);
            commentText = itemView.findViewById(R.id.comment);
            createdAtText = itemView.findViewById(R.id.created_at);

        }
    }

    private void clickHandler(final CommentModel comment, View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("publisherId", comment.getPublisher());
                context.startActivity(intent);

            }
        });
    }
}
