package com.wisekrakr.david.teachwise.models;

public class PostModel {

    private String postId, publisher, description, postImage, fieldOfStudy, studyContext;

    public PostModel() {
    }

    public PostModel(String postId, String publisher, String description, String postImage, String fieldOfStudy, String studyContext) {
        this.postId = postId;
        this.publisher = publisher;
        this.description = description;
        this.postImage = postImage;
        this.fieldOfStudy = fieldOfStudy;
        this.studyContext = studyContext;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
