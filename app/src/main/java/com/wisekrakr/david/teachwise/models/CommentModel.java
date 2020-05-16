package com.wisekrakr.david.teachwise.models;

public class CommentModel {

    private String comment;
    private String publisher;
    private Long createdAt;

    public CommentModel() {
    }

    public CommentModel(String comment, String publisher, Long createdAt) {
        this.comment = comment;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
