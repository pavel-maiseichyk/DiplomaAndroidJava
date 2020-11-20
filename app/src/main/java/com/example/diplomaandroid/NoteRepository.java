package com.example.diplomaandroid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface NoteRepository {
    BufferedReader getBufferedReaderById(int id);

    List<Note> getNotes() throws IOException;

    boolean saveNote(Note note) throws IOException;

    void deleteById(int id) throws FileNotFoundException;

    void putNoteInMap() throws IOException;
}
