package com.wisekrakr.david.teachwise.actions;

import android.widget.EditText;
import android.widget.ImageView;

public interface CommentActionsContext {
    void addComment(EditText comment, String postId);
    void getAvatar(ImageView image);
}
