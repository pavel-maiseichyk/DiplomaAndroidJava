package com.example.diplomaandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PinActivity extends AppCompatActivity {
    TextView wrongPasswordTV;

    View pinValue1;
    View pinValue2;
    View pinValue3;
    View pinValue4;
    View[] pinViews;

    String pin = "";

    static SharedPreferences isFirstTimePreferences;
    static SharedPreferences pinPreferences;
    public boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        init();
    }

    public void init() {
        createToolbar();
        pinPreferences = getSharedPreferences(AllSharedPreferences.PIN_PREFS, MODE_PRIVATE);
        isFirstTimePreferences = getSharedPreferences(AllSharedPreferences.FIRST_TIME_PREFS, MODE_PRIVATE);
        isFirstTime = App.getPasswordRepository().hasPin();

        if (isFirstTime) {
            Intent intent = new Intent(PinActivity.this, ChangePinActivity.class);
            startActivity(intent);
        } else {
            SharedPreferences pinPreferences = getSharedPreferences(AllSharedPreferences.PIN_PREFS, MODE_PRIVATE);
            if (pinPreferences != null) {
                SharedPreferences.Editor editor = isFirstTimePreferences.edit();
                editor.putBoolean(AllSharedPreferences.FIRST_TIME, false).apply();
            }
        }

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button buttonDelete = findViewById(R.id.buttonDelete);
        Button button0 = findViewById(R.id.button0);

        wrongPasswordTV = findViewById(R.id.wrong_password_tv);

        pinValue1 = findViewById(R.id.pinValue1);
        pinValue2 = findViewById(R.id.pinValue2);
        pinValue3 = findViewById(R.id.pinValue3);
        pinValue4 = findViewById(R.id.pinValue4);

        button1.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "1";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "1";
            }
        });

        button2.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "2";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
                ;
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "2";
            }
        });

        button3.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "3";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "3";
            }
        });

        button4.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "4";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "4";
            }
        });

        button5.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "5";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "5";
            }
        });

        button6.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "6";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "6";
            }
        });

        button7.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "7";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "7";
            }
        });

        button8.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "8";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "8";
            }
        });

        button9.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "9";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "9";
            }
        });

        button0.setOnClickListener(view -> {
            if (pin.length() == 3) {
                pin = pin + "0";
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                pin = pin + "0";
            }
        });

        buttonDelete.setOnClickListener(view -> {
            if (pin.length() != 0) {
                pin = pin.substring(0, pin.length() - 1);
                pinViews[pin.length()].setBackgroundColor(Color.GRAY);
            }
        });
        pinViews = new View[]{pinValue1, pinValue2, pinValue3, pinValue4};
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.pin_activity_toolbar);
        toolbar.setTitle("Welcome, Mate)");
        setSupportActionBar(toolbar);
    }

    public void actIfPasswordIsWrong() {
        for (View view : pinViews) view.setBackgroundColor(Color.GRAY);
        pin = "";
        wrongPasswordTV.setText("А всё, а всё...");
    }

    private void goToNotesActivity() {
        Intent intent = new Intent(PinActivity.this, NotesActivity.class);
        startActivity(intent);
    }
}