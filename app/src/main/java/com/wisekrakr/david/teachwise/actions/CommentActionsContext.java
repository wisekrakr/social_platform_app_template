package com.wisekrakr.david.teachwise.actions;

import android.widget.EditText;

public interface CommentActionsContext {
    void addComment(EditText comment, String postId, String publisherId);
    void getComments(String postId);
}
