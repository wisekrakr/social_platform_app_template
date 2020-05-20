package com.wisekrakr.david.teachwise.models;

public class NotificationModel {

    private String userId, comment, postId;
    private boolean isPost;

    public NotificationModel() {
    }

    public NotificationModel(String userId, String comment, String postId, boolean isPost) {
        this.userId = userId;
        this.comment = comment;
        this.postId = postId;
        this.isPost = isPost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isIsPost() {
        return isPost;
    }

    public void setIsPost(boolean post) {
        isPost = post;
    }
}
