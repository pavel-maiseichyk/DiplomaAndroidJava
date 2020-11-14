package com.example.diplomaandroid;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;

public class FileNoteRepository extends Application implements NoteRepository {

    public FileNoteRepository(Context context) {
    }

    @Override
    public Note getNoteById(String id) {
        return null;
    }

    @Override
    public List<Note> getNotes() {
        return null;
    }

    @Override
    public void saveNote(Note note) throws IOException {
        if (note.isEmpty()) {
            deleteById(note.getId());
            Toast.makeText(this, "Заметка была пустой, поэтому не была сохранена :(", Toast.LENGTH_SHORT).show();
        } else {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(note);
            BufferedWriter bufferedWriter;

            bufferedWriter = new BufferedWriter(new OutputStreamWriter(openFileOutput((CreateNoteActivity.idS), MODE_PRIVATE)));

            bufferedWriter.append(json);
            bufferedWriter.close();
            Toast.makeText(this, "Заметка сохранена)", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void deleteById(int id) {
        CreateNoteActivity.file.delete();
    }
}
