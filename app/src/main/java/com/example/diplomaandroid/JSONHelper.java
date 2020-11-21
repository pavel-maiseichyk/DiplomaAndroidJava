package com.example.diplomaandroid;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class JSONHelper {

    public static JsonObject getJsonData(Context context, String noteName) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.openFileInput(noteName)));
        JsonParser jsonParser = new JsonParser();
        return (JsonObject) jsonParser.parse(bufferedReader);
    }

    public static void setJsonData(Context context, Note note) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(note);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(note.getId() + ".txt", Context.MODE_PRIVATE)));
        bufferedWriter.append(json);
        bufferedWriter.close();
    }
}
