package com.wisekrakr.david.teachwise.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.actions.PostActionsStatic;
import com.wisekrakr.david.teachwise.actions.UserActionsStatic;
import com.wisekrakr.david.teachwise.fragments.PostFragment;
import com.wisekrakr.david.teachwise.fragments.ProfileFragment;
import com.wisekrakr.david.teachwise.models.NotificationModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    private Context context;
    private List<NotificationModel>notificationList;

    public NotificationAdapter(Context context, List<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final NotificationModel notificationModel = notificationList.get(position);

        holder.comment.setText(notificationModel.getComment());

        UserActionsStatic.getUserData(holder.avatar, holder.username, notificationModel.getUserId());

        if(notificationModel.isIsPost()){
            holder.postImage.setVisibility(View.VISIBLE);
            PostActionsStatic.getPostImage(holder.postImage, notificationModel.getPostId());
        }else{
            holder.postImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notificationModel.isIsPost()){
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postId", notificationModel.getPostId());
                    editor.apply();

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new PostFragment()).commit();
                }else{
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileId", notificationModel.getUserId());
                    editor.apply();

                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment()).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView username, comment;
        private ImageView avatar, postImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            avatar = itemView.findViewById(R.id.user_avatar);
            postImage = itemView.findViewById(R.id.post_image);
        }
    }
}
