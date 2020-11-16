package com.example.diplomaandroid;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileNoteRepository implements NoteRepository {

    public FileNoteRepository() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Note> getNotes() throws IOException {
        ArrayList<Note> list = new ArrayList<>();
        AllSharedPreferences.NOTE_IN_QUEUE = 0;

        File[] files = NotesActivity.files;
        assert files != null;
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        for (File noteFile : files) {
            String noteName = noteFile.getName();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(NotesActivity.directory, noteName)));
            NotesActivity.map.put(AllSharedPreferences.NOTE_IN_QUEUE, noteName);
            if (bufferedReader != null) {
                JsonParser jsonParser = new JsonParser();
                JsonObject noteJson = (JsonObject) jsonParser.parse(bufferedReader);

                String headline = noteJson.get("headline").getAsString();
                String body = noteJson.get("body").getAsString();
                boolean hasDeadline = noteJson.get("hasDeadline").getAsBoolean();

                String date;
                String time;
                if (hasDeadline) {
                    date = noteJson.get("date").getAsString();
                    time = noteJson.get("time").getAsString();
                } else {
                    date = null;
                    time = null;
                }
                int id = Integer.parseInt(noteName.substring(0, noteName.length() - 4));
                Note note = new Note(id, headline, body, hasDeadline, date, time);

                list.add(note);
                bufferedReader.close();
            }
            AllSharedPreferences.NOTE_IN_QUEUE++;
        }
        return list;
    }

    @Override
    public boolean saveNote(Note note) throws IOException {
        if (note.isEmpty()) {
            deleteById(note.getId());
            return false;
        } else {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(note);
            BufferedWriter bufferedWriter;
            bufferedWriter = new BufferedWriter(new FileWriter(CreateNoteActivity.file));
            bufferedWriter.append(json);
            bufferedWriter.close();
            return true;
        }
    }

    @Override
    public void deleteById(int id) {
        CreateNoteActivity.file.delete();
    }
}
