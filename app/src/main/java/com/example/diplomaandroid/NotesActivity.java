package com.example.diplomaandroid;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NotesActivity extends AppCompatActivity {
    static HashMap<Integer, String> map;
    static File[] files;
    static File directory;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        directory = getFilesDir();
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() throws IOException {
        createToolbar();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(NotesActivity.this, CreateNoteActivity.class);
            startActivity(intent);
        });

        files = this.getFilesDir().listFiles();

        map = new HashMap<>();

        ArrayList<Note> list = (ArrayList<Note>) App.getNoteRepository().getNotes();

        ListView listView = findViewById(R.id.notes_listView);
        final NoteAdapter adapter = new NoteAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            Intent intent = new Intent(NotesActivity.this, CreateNoteActivity.class);
            AllSharedPreferences.NOTE_IN_QUEUE = position;
            intent.putExtra(Integer.toString(AllSharedPreferences.NOTE_IN_QUEUE), map);
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((adapterView, view, position, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
            builder.setTitle(getResources().getString(R.string.delete_alert));
            builder.setPositiveButton(getResources().getString(R.string.yep), (dialogInterface, i) -> {
                String fileName = map.get(position);
                assert fileName != null;
                File file = new File(getFilesDir(), fileName);
                file.delete();
                Toast.makeText(NotesActivity.this, getResources().getString(R.string.note_deleted_toast), Toast.LENGTH_SHORT).show();
                try {
                    init();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.nope), (dialogInterface, i) -> {
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
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
        toolbar.setTitle(getResources().getString(R.string.notes_title));
        setSupportActionBar(toolbar);
    }
}