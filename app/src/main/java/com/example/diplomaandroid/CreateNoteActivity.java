package com.example.diplomaandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CreateNoteActivity extends AppCompatActivity {
    ImageButton calendarButton;
    Calendar calendar;
    DatePickerDialog dialog;
    EditText dateAndTime;
    LinearLayout headlineLayout;
    CheckBox hasDeadlineCB;
    Note note;
    Button saveButton;
    int id;

    Intent intentWithNoteData;
    String idS;
    boolean isBeingFixed = false;

    HashMap<Integer, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        getNoteDate();
        init();
    }

    private void getNoteDate() {
        intentWithNoteData = this.getIntent();
        BufferedReader bufferedReader = null;
        if (intentWithNoteData != null) {
            map = (HashMap<Integer, String>) intentWithNoteData.getSerializableExtra(Integer.toString(AllSharedPreferences.NOTE_IN_QUEUE));
            if (map != null) {
                idS = map.get(AllSharedPreferences.NOTE_IN_QUEUE);
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(openFileInput(idS)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (bufferedReader != null) {
                    isBeingFixed = true;
                    JsonParser jsonParser = new JsonParser();
                    JsonObject noteJson = (JsonObject) jsonParser.parse(bufferedReader);

                    EditText headline = findViewById(R.id.headline);
                    headline.setText(noteJson.get("headline").getAsString());

                    EditText body = findViewById(R.id.note_body);
                    body.setText(noteJson.get("body").getAsString());

                    CheckBox hasDeadline = findViewById(R.id.has_deadline);
                    hasDeadline.setChecked(noteJson.get("hasDeadline").getAsBoolean());

                    EditText date = findViewById(R.id.date);
                    EditText time = findViewById(R.id.time);

                    if (hasDeadline.isChecked()) {
                        date.setText(noteJson.get("date").getAsString());
                        time.setText(noteJson.get("time").getAsString());
                    }

            try {
                bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }}
        }
    }}

    public void init() {
        Random random = new Random();
        id = random.nextInt();

        createToolbar();

        dateAndTime = findViewById(R.id.date);
        calendarButton = findViewById(R.id.calendar_button);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                dialog = new DatePickerDialog(CreateNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        dateAndTime.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                dialog.show();
            }
        });

        headlineLayout = findViewById(R.id.headline_layout);
        hasDeadlineCB = findViewById(R.id.has_deadline);
        hasDeadlineCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) headlineLayout.setVisibility(View.VISIBLE);
                else headlineLayout.setVisibility(View.INVISIBLE);
            }
        });

        saveButton = findViewById(R.id.save_note_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    note = createNote();
                    // MyRepository.getNoteRepository().saveNote(note);
                    saveNote(note);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveNote(Note note) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(note);
        BufferedWriter bufferedWriter;
        if (!isBeingFixed)
        {bufferedWriter = new BufferedWriter(new OutputStreamWriter(openFileOutput((id /*note.getId()*/ + ".txt"), MODE_PRIVATE)));} //fix!!
        else
        {bufferedWriter = new BufferedWriter(new OutputStreamWriter(openFileOutput((idS), MODE_PRIVATE)));}
        bufferedWriter.append(json);
        bufferedWriter.close();

        Toast.makeText(CreateNoteActivity.this, "Заметка сохранена)", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateNoteActivity.this, NotesActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String fileName = map.get(AllSharedPreferences.NOTE_IN_QUEUE);
        deleteByFileName(fileName);
        return true;
    }

    public void createToolbar() {
        Toolbar toolbar = findViewById(R.id.create_note_activity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Создаём заметочку)");
    }

    public Note createNote() {
        EditText headline = findViewById(R.id.headline);
        EditText noteBody = findViewById(R.id.note_body);
        String date;
        String time;
        if (!hasDeadlineCB.isChecked()) {
            date = null;
            time = null;
        } else {
            EditText dateET = findViewById(R.id.date);
            date = dateET.getText().toString();
            EditText timeET = findViewById(R.id.time);
            time = timeET.getText().toString();
        }
        return new Note(id, headline.getText().toString(), noteBody.getText().toString(), hasDeadlineCB.isChecked(), date, time);
    }

    // @Override
    public void deleteByFileName(String fileName)  {
        File file = new File(getFilesDir(), fileName);
        file.delete();
        Intent intent = new Intent(CreateNoteActivity.this, NotesActivity.class);
        startActivity(intent);
    }
}


/*исправить:
0.смещение заметок при удалении (id)
1.очищение пустых заметок или заметок из пробелов
2.отменить наложение даты и времени
 */
