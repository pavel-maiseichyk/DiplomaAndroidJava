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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PinActivity extends AppCompatActivity {
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button button0;
    Button buttonDelete;

    TextView wrongPasswordTV;

    View pinValue1;
    View pinValue2;
    View pinValue3;
    View pinValue4;
    View[] pinViews;

    String pin = "";

    SharedPreferences isFirstTimePreferences;
    public boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        init();

        pinViews = new View[]{pinValue1, pinValue2, pinValue3, pinValue4};
    }

    public void init() {
        createToolbar();
        isFirstTimePreferences = getSharedPreferences(AllSharedPreferences.FIRST_TIME_PREFS, MODE_PRIVATE);
        isFirstTime = isFirstTimePreferences.getBoolean(AllSharedPreferences.FIRST_TIME, true);

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

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        buttonDelete = findViewById(R.id.buttonDelete);
        button0 = findViewById(R.id.button0);

        wrongPasswordTV = findViewById(R.id.wrong_password_tv);

        pinValue1 = findViewById(R.id.pinValue1);
        pinValue2 = findViewById(R.id.pinValue2);
        pinValue3 = findViewById(R.id.pinValue3);
        pinValue4 = findViewById(R.id.pinValue4);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "1";
                    if (!checkPin(pin)) actIfPasswordIsWrong();
                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "1";
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "2";
                    if (!checkPin(pin)) actIfPasswordIsWrong();
                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "2";
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "3";
                    if (!checkPin(pin)) actIfPasswordIsWrong();
                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "3";
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "4";
                    if (!checkPin(pin)) actIfPasswordIsWrong();
                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "4";
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "5";
                    if (!checkPin(pin)) actIfPasswordIsWrong();
                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "5";
                }
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "6";
                    if (!checkPin(pin)) actIfPasswordIsWrong();
                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "6";
                }
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "7";
                    if (!checkPin(pin)) actIfPasswordIsWrong();
                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "7";
                }
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "8";
                    if (!checkPin(pin)) actIfPasswordIsWrong();
                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "8";
                }
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "9";
                    if (!checkPin(pin)) actIfPasswordIsWrong();
                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "9";
                }
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() == 3) {
                    pin = pin + "0";

                    if (!checkPin(pin)) actIfPasswordIsWrong();

                } else {
                    pinViews[pin.length()].setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //изменить цвет и форму
                    pin = pin + "0";
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.length() != 0) {
                    pin = pin.substring(0, pin.length() - 1);
                    pinViews[pin.length()].setBackgroundColor(Color.GRAY);
                }
            }
        });
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.pin_activity_toolbar);
        toolbar.setTitle("Welcome, Mate)");
        setSupportActionBar(toolbar);
    }


    public boolean hasPin() {
        return false;
    }


    public boolean checkPin(String pin) {
        SharedPreferences preferences = getSharedPreferences(AllSharedPreferences.PIN_PREFS, MODE_PRIVATE);
        if (pin.equals(preferences.getString(AllSharedPreferences.PIN, ""))) {
            Intent intent = new Intent(PinActivity.this, NotesActivity.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }


    public void actIfPasswordIsWrong() {
        for (View view : pinViews) view.setBackgroundColor(Color.GRAY);
        pin = "";
        wrongPasswordTV.setText("А всё, а всё...");
    }
}