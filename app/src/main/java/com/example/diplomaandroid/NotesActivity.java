package com.example.diplomaandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class NotesActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Note> list;
    HashMap<Integer, String> map;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void init() throws IOException {
        createToolbar();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, CreateNoteActivity.class);
                startActivity(intent);
            }
        });

        map = new HashMap<>();

        list = getNoteList();

        listView = findViewById(R.id.notes_listView);
        final NoteAdapter adapter = new NoteAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(NotesActivity.this, CreateNoteActivity.class);
                AllSharedPreferences.NOTE_IN_QUEUE = position;
                intent.putExtra(Integer.toString(AllSharedPreferences.NOTE_IN_QUEUE), map);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
                builder.setTitle("ну что, удаляем?");
                builder.setPositiveButton("угусь", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fileName = map.get(position);
                        assert fileName != null;
                        File file = new File(getFilesDir(), fileName);
                        file.delete();
                        Toast.makeText(NotesActivity.this, "Заметка удалена!", Toast.LENGTH_SHORT).show();
                        try {
                            init();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                builder.setNegativeButton("ноуп", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Note> getNoteList() throws IOException {
        list = new ArrayList<>();
        AllSharedPreferences.NOTE_IN_QUEUE = 0;

        File[] files = this.getFilesDir().listFiles();
        assert files != null;
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        for (File noteFile : files) {
            String noteName = noteFile.getName();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput(noteName)));
            map.put(AllSharedPreferences.NOTE_IN_QUEUE, noteName);
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

    //спросить: как сделать неиспользованные поля заметки GONE?
}