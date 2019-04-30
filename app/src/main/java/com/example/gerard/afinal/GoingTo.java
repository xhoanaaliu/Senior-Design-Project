package com.example.gerard.afinal;

import java.io.Serializable;

public class GoingTo implements Serializable {
    private String title;
    private String user_id;

    public GoingTo() {
    }

    public GoingTo(String title, String user_id) {
        this.title = title;
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
