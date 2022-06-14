package com.example.zwanews.Models;

import com.google.firebase.firestore.Exclude;

public class Users {
    String userEmail;
    String displayname;
    String Url;

    public Users(String displayname, String url) {
        this.displayname = displayname;
        Url = url;
    }
    public String getdisplayname() {

        return displayname;
    }
    public void setdisplayname(String displayName) {

        displayname = displayName;
    }

    public Users() {

    }

    @Exclude
    public String getUserEmail() {

        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }


}
