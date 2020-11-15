package com.example.diplomaandroid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface NoteRepository {
    List<Note> getNotes() throws IOException;

    boolean saveNote(Note note) throws IOException;

    void deleteById(int id) throws FileNotFoundException;
}
