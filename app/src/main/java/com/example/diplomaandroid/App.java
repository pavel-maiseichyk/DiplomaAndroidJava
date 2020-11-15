package com.example.diplomaandroid;

import android.app.Application;

public class App extends Application {
    private static NoteRepository noteRepository;
    private static PasswordRepository passwordRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        noteRepository = new FileNoteRepository();
        passwordRepository = new SimpleKeystore();
    }

    public static NoteRepository getNoteRepository() {
        return noteRepository;
    }

    public static PasswordRepository getPasswordRepository() {
        return passwordRepository;
    }
}
