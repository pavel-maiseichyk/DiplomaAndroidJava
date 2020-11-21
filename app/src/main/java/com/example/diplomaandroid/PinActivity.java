package com.example.diplomaandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PinActivity extends AppCompatActivity {
    private TextView wrongPasswordTV;

    private View[] pinViews;

    private String pin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        init();
    }

    private void init() {
        createToolbar();
        SharedPreferences isFirstTimePreferences = getSharedPreferences(AllSharedPreferences.FIRST_TIME_PREFS, MODE_PRIVATE);
        boolean isFirstTime = App.getPasswordRepository().hasPin();

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

        wrongPasswordTV = findViewById(R.id.wrong_password_tv);

        View pinValue1 = findViewById(R.id.pinValue1);
        View pinValue2 = findViewById(R.id.pinValue2);
        View pinValue3 = findViewById(R.id.pinValue3);
        View pinValue4 = findViewById(R.id.pinValue4);

        View.OnClickListener listener = v -> {
            if (pin.length() == 3) {
                pin = pin + v.getTag();
                if (!App.getPasswordRepository().checkPin(pin)) actIfPasswordIsWrong();
                else goToNotesActivity();
            } else {
                pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                pin = pin + v.getTag();
            }
        };
        Button button1 = findViewById(R.id.button1);
        button1.setTag("1");
        button1.setOnClickListener(listener);
        Button button2 = findViewById(R.id.button2);
        button2.setTag("2");
        button2.setOnClickListener(listener);
        Button button3 = findViewById(R.id.button3);
        button3.setTag("3");
        button3.setOnClickListener(listener);
        Button button4 = findViewById(R.id.button4);
        button4.setTag("4");
        button4.setOnClickListener(listener);
        Button button5 = findViewById(R.id.button5);
        button5.setTag("5");
        button5.setOnClickListener(listener);
        Button button6 = findViewById(R.id.button6);
        button6.setTag("6");
        button6.setOnClickListener(listener);
        Button button7 = findViewById(R.id.button7);
        button7.setTag("7");
        button7.setOnClickListener(listener);
        Button button8 = findViewById(R.id.button8);
        button8.setTag("8");
        button8.setOnClickListener(listener);
        Button button9 = findViewById(R.id.button9);
        button9.setTag("9");
        button9.setOnClickListener(listener);
        Button button0 = findViewById(R.id.button0);
        button0.setTag("0");
        button0.setOnClickListener(listener);

        Button buttonDelete = findViewById(R.id.buttonDelete);
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
        toolbar.setTitle(getResources().getString(R.string.welcome_title));
        setSupportActionBar(toolbar);
    }

    private void actIfPasswordIsWrong() {
        for (View view : pinViews) view.setBackgroundColor(Color.GRAY);
        pin = "";
        wrongPasswordTV.setText(getResources().getString(R.string.wrong_password_warning));
    }

    private void goToNotesActivity() {
        Intent intent = new Intent(PinActivity.this, NotesActivity.class);
        startActivity(intent);
    }
}