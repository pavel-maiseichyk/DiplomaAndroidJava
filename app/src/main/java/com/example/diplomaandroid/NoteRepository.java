package com.example.diplomaandroid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface NoteRepository {
    Note getNoteById(String id);
    List<Note> getNotes();
    void saveNote(Note note) throws IOException;
    void deleteById(int id) throws FileNotFoundException;
}
