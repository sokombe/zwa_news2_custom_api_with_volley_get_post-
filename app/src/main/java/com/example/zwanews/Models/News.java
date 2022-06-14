package com.example.zwanews.Models;

public class News {
    String date_news;
    String url;
    String description;
    String title;

    public String getDate_news() {
        return date_news;
    }

    public News(String date_news, String url, String description, String title) {
        this.date_news = date_news;
        this.url = url;
        this.description = description;
        this.title = title;
    }
    public News() {
    }
    public void setDate_news(String date_news) {
        this.date_news = date_news;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
