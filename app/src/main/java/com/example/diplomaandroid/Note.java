package com.example.diplomaandroid;

public class Note {
    int id;
    String headline;
    String body;
    boolean hasDeadline;
    String date;
    String time;

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

    public boolean isHasDeadline() {
        return hasDeadline;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
