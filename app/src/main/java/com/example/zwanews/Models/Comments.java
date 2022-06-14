package com.example.zwanews.Models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Comments  implements  Serializable{
    String idcomment;
    String fk_new;
    String content;
    String date_comment;
    String user_id;

    @Exclude
    public String getIdcomment() {
        return idcomment;
    }

    public Comments( String fk_new, String content, String date_comment,String user_id) {
        this.fk_new = fk_new;
        this.content = content;
        this.date_comment = date_comment;
        this.user_id = user_id;
    }

    public Comments() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getFk_new() {
        return fk_new;
    }

    public void setFk_new(String fk_new) {
        this.fk_new = fk_new;
    }

    public void setIdcomment(String idcomment) {
        this.idcomment = idcomment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_comment() {
        return date_comment;
    }

    public void setDate_comment(String date_comment) {
        this.date_comment = date_comment;
    }

}
