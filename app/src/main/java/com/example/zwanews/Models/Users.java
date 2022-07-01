package com.example.zwanews.Models;

import com.google.firebase.firestore.Exclude;

public class Users {
    String id;
    String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String firstname;
    String secondname;
    String password;
    String phone;


    public Users( String id,String firstname, String secondname, String email,String password, String phone) {
        this.email = email;
        this.firstname = firstname;
        this.secondname = secondname;
        this.password = password;
        this.phone = phone;
        this.id=id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
