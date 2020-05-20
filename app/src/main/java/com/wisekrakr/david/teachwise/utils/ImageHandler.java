package com.wisekrakr.david.teachwise.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class ImageHandler {

    public static void setPicassoImage(String imageFile, ImageView image, int resourceId){
        try {
            //if image is received then set that image
            Picasso.get().load(imageFile).into(image);
        }catch (Exception e){
            //if there is no image, set Image View to default
            Picasso.get().load(resourceId).into(image);
        }
    }

    public static void setPicassoImageWithPlaceHolder(String imageFile, ImageView image, int resourceId){
        try {
            //if image is received then set that image
            Picasso.get().load(imageFile).placeholder(resourceId).into(image);
        }catch (Exception e){
            //if there is no image, set Image View to default
            Picasso.get().load(resourceId).into(image);
        }
    }
}
