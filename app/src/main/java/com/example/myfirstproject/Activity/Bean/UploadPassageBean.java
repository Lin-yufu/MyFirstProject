package com.example.myfirstproject.Activity.Bean;

import androidx.annotation.NonNull;

public class UploadPassageBean {
    private String hostId;
    private String articleId;
    private String publishTime;
    private String title;
    private String articleImage;
    private String passage;

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }


    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPassage(String passage) {
        this.passage = passage;
    }

    public String getHostId() {
        return hostId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getTitle() {
        return title;
    }

    public String getPassage() {
        return passage;
    }

}
