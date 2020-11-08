package com.example.diplomaandroid;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class NotesActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Note> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        try {
            init();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException, JSONException {
        createToolbar();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, CreateNoteActivity.class);
                startActivity(intent);
            }
        });

        list = getNoteList();

        listView = findViewById(R.id.notes_listView);
        NoteAdapter adapter = new NoteAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(NotesActivity.this, CreateNoteActivity.class);
                intent.putExtra(AllSharedPreferences.NOTE_ID, position+1);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

    }

    private ArrayList<Note> getNoteList() throws IOException {
        list = new ArrayList<>();
        int id = 1;
        while (id <= 100) //добавить пользователю возможность выбора числа заметок
        {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(openFileInput(id + ".txt")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                id++;
            }

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

                Note note = new Note(id, headline, body, hasDeadline, date, time);
                list.add(note);
                bufferedReader.close();
            }
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent); //сделать так, чтобы при выключении экрана пароль требовался заново
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(NotesActivity.this, ChangePinActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_to_settings, menu);
        return true;
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Заметочки...");
        setSupportActionBar(toolbar);
    }
}