package com.example.diplomaandroid;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static NoteRepository noteRepository;
    private static PasswordRepository passwordRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        noteRepository =  FileNoteRepository.getInstance(this);

        passwordRepository = new SimpleKeystore();
    }

    static NoteRepository getNoteRepository() {
        return noteRepository;
    }

    static PasswordRepository getPasswordRepository() {
        return passwordRepository;
    }
}
