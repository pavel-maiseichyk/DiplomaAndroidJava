package com.example.diplomaandroid;

import java.io.File;

public class Note {
    private int id;
    private String headline;
    private String body;
    private boolean hasDeadline;
    private String date;
    private String time;
    private int position;

    public Note(int id, String headline, String body, boolean hasDeadline, String date, String time) {
        this.id = id;
        this.headline = headline;
        this.body = body;
        this.hasDeadline = hasDeadline;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return this.id;
    }

    public String getHeadline() {
        return headline;
    }

    public String getBody() {
        return body;
    }

    public boolean hasDeadline() {
        return hasDeadline;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isEmpty() {
        if (getHeadline().equals(""))
            return getBody().equals("");
        return false;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
