package com.example.diplomaandroid;

import android.content.Context;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class FileNoteRepository implements NoteRepository {
    private static FileNoteRepository instance;
    private Context context;
    private File file;
    private HashMap<Integer, String> map = new HashMap<>();

    private FileNoteRepository(Context context) {
        this.context = context;
    }

    public static FileNoteRepository getInstance(Context context) {
        if (instance == null)
            instance = new FileNoteRepository(context);
        return instance;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Note> getNotes() throws IOException {
        ArrayList<Note> list = new ArrayList<>();
        AllSharedPreferences.NOTE_IN_QUEUE = 0;

        file = context.getFilesDir();
        File[] files = file.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

            for (File noteFile : files) {
                String noteName = noteFile.getName();
                map.put(AllSharedPreferences.NOTE_IN_QUEUE, noteName);

                JsonObject noteJson = JSONHelper.getJsonData(context, noteName);

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
            JSONHelper.setJsonData(context, note);
            return true;
        }
    }

    @Override
    public void deleteById(int id) {
        file = new File(context.getFilesDir(), id + ".txt");
        file.delete();
    }

    public HashMap<Integer, String> getMap() {
        return map;
    }
}
