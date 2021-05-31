package com.example.myfirstproject.Activity.Bean;

public class DownloadPassageBean {
    private String articleId;
    private String authorNick;
    private String authorIcon;
    private String publishTime;
    private String title;
    private String articleImage;
    private String passage;

    public void setAuthorNick(String authorNick) {
        this.authorNick = authorNick;
    }

    public String getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(String authorIcon) {
        this.authorIcon = authorIcon;
    }

    public String getAuthorNick() {
        return authorNick;
    }

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



    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPassage(String passage) {
        this.passage = passage;
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
