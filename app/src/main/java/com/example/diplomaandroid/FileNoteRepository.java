package com.example.diplomaandroid;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class FileNoteRepository implements NoteRepository {

    private File file;
    static HashMap<Integer, String> map = new HashMap<>();

    @Override
    public BufferedReader getBufferedReaderById(int id) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(new File(Note.PATH_TO_FILES, CreateNoteActivity.idS)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bufferedReader;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Note> getNotes() throws IOException {
        ArrayList<Note> list = new ArrayList<>();
        AllSharedPreferences.NOTE_IN_QUEUE = 0;

        file = new File(Note.PATH_TO_FILES);
        File[] files = file.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

            for (File noteFile : files) {
                String noteName = noteFile.getName();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(Note.PATH_TO_FILES, noteName)));
                map.put(AllSharedPreferences.NOTE_IN_QUEUE, noteName);
                JsonParser jsonParser = new JsonParser();
                JsonObject noteJson = (JsonObject) jsonParser.parse(bufferedReader);

                String headline = noteJson.get("headline").getAsString();
                String body = noteJson.get("body").getAsString();
                boolean hasDeadline = noteJson.get("hasDeadline").getAsBoolean();

                String date = null;
                String time = null;
                if (hasDeadline) {
                    date = noteJson.get("date").getAsString();
                    time = noteJson.get("time").getAsString();
                }
                int id = Integer.parseInt(noteName.substring(0, noteName.length() - 4));
                Note note = new Note(id, headline, body, hasDeadline, date, time);

                list.add(note);

                AllSharedPreferences.NOTE_IN_QUEUE++;
            }
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
            file = new File(Note.PATH_TO_FILES, note.getId() + ".txt");
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.append(json);
            bufferedWriter.close();
            return true;
        }
    }

    @Override
    public void deleteById(int id) {
        file = new File(Note.PATH_TO_FILES, id + ".txt");
        file.delete();
    }

    @Override
    public void putNoteInMap() {
        Intent intent = new Intent();
        intent.putExtra(Integer.toString(AllSharedPreferences.NOTE_IN_QUEUE), map);
    }
}
