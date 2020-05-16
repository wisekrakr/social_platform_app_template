package com.wisekrakr.david.teachwise.actions;

import android.widget.EditText;

public interface CommentActionsContext {
    void addComment(EditText comment, String postId);
    void getComments(String postId);
}
