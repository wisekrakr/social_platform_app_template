package com.wisekrakr.david.teachwise.actions;

public interface PostActionsContext {
    void getPosts();
    void getPost(String postId);
    void checkFollowing();
}
