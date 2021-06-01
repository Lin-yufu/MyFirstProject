package com.example.myfirstproject.Activity.Bean;

public class DownloadReviewBean {
    private String reviewerIcon;
    private String reviewerNick;
    private String reviewTime;
    private String reviewContent;

    public String getReviewerContent() {
        return reviewContent;
    }

    public String getReviewerIcon() {
        return reviewerIcon;
    }

    public String getReviewerNick() {
        return reviewerNick;
    }

    public String getRevieweTime() {
        return reviewTime;
    }

    public void setReviewerContent(String reviewerContent) {
        this.reviewContent = reviewerContent;
    }

    public void setReviewerIcon(String reviewerIcon) {
        this.reviewerIcon = reviewerIcon;
    }

    public void setReviewerNick(String reviewerNick) {
        this.reviewerNick = reviewerNick;
    }

    public void setRevieweTime(String revieweTime) {
        this.reviewTime = revieweTime;
    }
}
