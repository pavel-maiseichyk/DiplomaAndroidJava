package com.example.diplomaandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
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
    EditText date;
    EditText time;
    CheckBox hasDeadlineCB;

    int id;
    static String idS;

    static boolean isBeingFixed = false;
    static File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        getNoteData();
        init();
    }

    private void getNoteData() {
        Random random = new Random();
        id = random.nextInt();

        hasDeadlineCB = findViewById(R.id.has_deadline);
        Intent intentWithNoteData = this.getIntent();
        BufferedReader bufferedReader = null;
        if (intentWithNoteData != null) {
            HashMap<Integer, String> map = (HashMap<Integer, String>) intentWithNoteData.getSerializableExtra(Integer.toString(AllSharedPreferences.NOTE_IN_QUEUE));
            if (map != null) {
                idS = map.get(AllSharedPreferences.NOTE_IN_QUEUE);
                assert idS != null;
                id = Integer.parseInt(idS.substring(0, idS.length() - 4));
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

                    hasDeadlineCB.setChecked(noteJson.get("hasDeadline").getAsBoolean());

                    EditText date = findViewById(R.id.date);
                    EditText time = findViewById(R.id.time);

                    if (hasDeadlineCB.isChecked()) {
                        date.setText(noteJson.get("date").getAsString());
                        time.setText(noteJson.get("time").getAsString());
                    }
                    if (hasDeadlineCB.isChecked()) {
                        checkVisibility(hasDeadlineCB.isChecked());
                    }
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                idS = id + ".txt";
            }
        }
    }

    public void init() {
        createToolbar();

        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month == 13) month = 1;
        int finalMonth = month;
        int year = calendar.get(Calendar.YEAR);

        ImageButton calendarButton = findViewById(R.id.calendar_button);
        calendarButton.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateNoteActivity.this, (datePicker, mYear, mMonth, mDay) -> date.setText(checkIfToAddZero(mDay) + "/" + checkIfToAddZero(mMonth + 1) + "/" + checkIfToAddZero(mYear)), day, finalMonth, year);
            datePickerDialog.show();
        });

        ImageButton timeButton = findViewById(R.id.time_button);
        timeButton.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(CreateNoteActivity.this, (timePicker, mHour, mMinute) -> time.setText(checkIfToAddZero(mHour) + ":" + checkIfToAddZero(mMinute)), hour, minute, true);
            timePickerDialog.show();
        });

        hasDeadlineCB.setOnCheckedChangeListener((compoundButton, isChecked) -> checkVisibility(isChecked));

        Button saveButton = findViewById(R.id.save_note_button);
        saveButton.setOnClickListener(view -> {
            try {
                Note note = createNote();
                if (App.getNoteRepository().saveNote(note))
                    Toast.makeText(this, "Заметка сохранена)", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Заметка была пустой, поэтому не была сохранена :(", Toast.LENGTH_SHORT).show();
                goToNotesActivity();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        file = new File(getFilesDir(), idS);
    }

    private void checkVisibility(boolean isChecked) {
        LinearLayout headlineLayout = findViewById(R.id.headline_layout);
        TextView haveANiceDay = findViewById(R.id.have_a_nice_day);
        if (isChecked) {
            headlineLayout.setVisibility(View.VISIBLE);
            haveANiceDay.setVisibility(View.INVISIBLE);
        } else {
            headlineLayout.setVisibility(View.INVISIBLE);
            haveANiceDay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            App.getNoteRepository().deleteById(id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        goToNotesActivity();
        return true;
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.create_note_activity_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        getSupportActionBar().setTitle("Создаём заметочку)");
    }

    private Note createNote() {
        EditText headline = findViewById(R.id.headline);
        EditText noteBody = findViewById(R.id.note_body);
        String dateS;
        String timeS;
        if (!hasDeadlineCB.isChecked()) {
            dateS = null;
            timeS = null;
        } else {
            dateS = date.getText().toString();
            timeS = time.getText().toString();
        }
        return new Note(id, headline.getText().toString(), noteBody.getText().toString(), hasDeadlineCB.isChecked(), dateS, timeS);
    }

    private String checkIfToAddZero(int value) {
        String valueS = Integer.toString(value);
        if (valueS.length() == 1)
            valueS = "0" + value;
        return valueS;
    }

    private void goToNotesActivity() {
        Intent intent = new Intent(CreateNoteActivity.this, NotesActivity.class);
        startActivity(intent);
    }
}
