package com.example.diplomaandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
    private EditText date;
    private EditText time;
    private CheckBox hasDeadlineCB;

    private int id;
    static String idS;

    private boolean isBeingFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        isBeingFixed = false;
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        hasDeadlineCB = findViewById(R.id.has_deadline);

        Random random = new Random();
        id = random.nextInt();
        try {
            fillWithExistingData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();
    }

    private void fillWithExistingData() throws IOException {

        Intent intentWithNoteData = this.getIntent();
        if (intentWithNoteData != null) {

            HashMap<Integer, String> map = (HashMap<Integer, String>) intentWithNoteData.getSerializableExtra(Integer.toString(AllSharedPreferences.NOTE_IN_QUEUE));
            if (map != null) {
                isBeingFixed = true;
            idS = Objects.requireNonNull(map).get(AllSharedPreferences.NOTE_IN_QUEUE);
            try {
            id = Integer.parseInt(idS.substring(0, idS.length() - 4));} catch (Exception e) {
                Toast.makeText(this, "а нефиг было с рутом играться, теперь переустанавливай всё", Toast.LENGTH_SHORT).show();
            }

            JsonParser jsonParser = new JsonParser();
            BufferedReader bufferedReader = App.getNoteRepository().getBufferedReaderById(id);
            JsonObject noteJson = (JsonObject) jsonParser.parse(bufferedReader);

            EditText headline = findViewById(R.id.headline);
            headline.setText(noteJson.get("headline").getAsString());

            EditText body = findViewById(R.id.note_body);
            body.setText(noteJson.get("body").getAsString());

            if (hasDeadlineCB.isChecked())
                checkVisibility(hasDeadlineCB.isChecked());
            hasDeadlineCB.setChecked(noteJson.get("hasDeadline").getAsBoolean());

            if (hasDeadlineCB.isChecked()) {
                date.setText(noteJson.get("date").getAsString());
                time.setText(noteJson.get("time").getAsString());
            }
            bufferedReader.close();}
         else
            idS = id + ".txt";
    }}

    private void init() {
        createToolbar();

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

                if (App.getNoteRepository().saveNote(note)) {
                    if (!isBeingFixed)
                        Toast.makeText(this, getResources().getString(R.string.note_saved_toast), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, getResources().getString(R.string.note_fixed_toast), Toast.LENGTH_SHORT).show();
                    goToNotesActivity();
                } else
                    doIfNoteIsEmpty();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void doIfNoteIsEmpty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.empty_note_warning));
        builder.setPositiveButton(getResources().getString(R.string.option_change_your_mind), (dialogInterface, i) -> {
        });
        builder.setNegativeButton(getResources().getString(R.string.bye_berlin), (dialogInterface, i) -> {
            Toast.makeText(this, getResources().getString(R.string.empty_note_toast), Toast.LENGTH_SHORT).show();
            goToNotesActivity();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        if (isBeingFixed)
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
        if (!isBeingFixed)
            getSupportActionBar().setTitle(getResources().getString(R.string.create_note_title));
        else getSupportActionBar().setTitle(getResources().getString(R.string.fix_note_title));
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

    @Override
    public void onBackPressed() {
            if (!createNote().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.not_saved_note_warning));
                builder.setPositiveButton(getResources().getString(R.string.yep), (dialogInterface, i) -> {
                    try {
                        App.getNoteRepository().saveNote(createNote());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    goToNotesActivity();
                });

                builder.setNegativeButton(getResources().getString(R.string.nope), (dialogInterface, i) -> {
                    goToNotesActivity();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else doIfNoteIsEmpty();
    }
}