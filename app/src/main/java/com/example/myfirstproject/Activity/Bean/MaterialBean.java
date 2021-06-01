package com.example.myfirstproject.Activity.Bean;
//下载资料的类
public class MaterialBean {
    private String id;
    private String title;
    private String pubtime;
    private String content;
    private String uname;
    private String peitu;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPeitu(String peitu) {
        this.peitu = peitu;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getPeitu() {
        return peitu;
    }

    public String getPubtime() {
        return pubtime;
    }

    public String getUname() {
        return uname;
    }
}
