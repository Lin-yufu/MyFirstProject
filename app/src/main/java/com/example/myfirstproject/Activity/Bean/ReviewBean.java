package com.example.myfirstproject.Activity.Bean;

public class ReviewBean {
    private String commentId;
    private String articleId;
    private String reviewerId;
    private String reviewTime;
    private String reviewContent;

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public String getReviewTime() {
        return reviewTime;
    }
}
