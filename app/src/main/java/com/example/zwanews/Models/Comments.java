package com.example.zwanews.Models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Comments  implements  Serializable {
    String content;
    String id_article;
    String id_user;

    public Comments(String content, String id_article, String id_user) {
        this.content = content;
        this.id_article = id_article;
        this.id_user = id_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }





    public String getId_article() {
        return id_article;
    }

    public void setId_article(String id_article) {
        this.id_article = id_article;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}