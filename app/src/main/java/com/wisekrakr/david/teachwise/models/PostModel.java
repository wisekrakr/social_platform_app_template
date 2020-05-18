package com.wisekrakr.david.teachwise.models;

public class PostModel {

    private String postId, publisher, title, postImage, tags, description;
    private Long createdAt;

    public PostModel() {
    }

    public PostModel(String postId, String publisher, String title, String postImage, String tags, String description, Long createdAt) {
        this.postId = postId;
        this.publisher = publisher;
        this.title = title;
        this.postImage = postImage;
        this.tags = tags;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
