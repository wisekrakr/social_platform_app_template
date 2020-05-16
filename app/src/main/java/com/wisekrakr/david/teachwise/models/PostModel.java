package com.wisekrakr.david.teachwise.models;

public class PostModel {

    private String postId, publisher, title, postImage, fieldOfStudy, studyContext;
    private Long createdAt;

    public PostModel() {
    }

    public PostModel(String postId, String publisher, String title, String postImage, String fieldOfStudy, String studyContext, Long createdAt) {
        this.postId = postId;
        this.publisher = publisher;
        this.title = title;
        this.postImage = postImage;
        this.fieldOfStudy = fieldOfStudy;
        this.studyContext = studyContext;
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

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getStudyContext() {
        return studyContext;
    }

    public void setStudyContext(String studyContext) {
        this.studyContext = studyContext;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
