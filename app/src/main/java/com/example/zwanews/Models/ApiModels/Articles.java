package com.example.zwanews.Models.ApiModels;

import java.io.Serializable;

public class Articles implements Serializable {
    String id;
    String source;
    String author;
    String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String content;

    public Articles(String id,String source, String author, String title, String content, String link, String category_id, String createdAt, String language) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.content = content;
        this.link = link;
        this.category_id = category_id;
        this.createdAt = createdAt;
        this.language = language;
        this.id=id;
    }

    String link;
    String category_id;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    String createdAt;
    String language;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }



    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
