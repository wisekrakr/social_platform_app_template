package com.wisekrakr.david.teachwise.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wisekrakr.david.teachwise.R;
import com.wisekrakr.david.teachwise.models.PostModel;
import com.wisekrakr.david.teachwise.utils.UserImage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context context;
    private List<PostModel> imageList;


    public ImageAdapter(Context context, List<PostModel> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);

        return new ImageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PostModel postModel = imageList.get(position);

        UserImage.setPicassoImage(postModel.getPostImage(), holder.image, R.drawable.ic_image);

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.post_image);
        }
    }
}
