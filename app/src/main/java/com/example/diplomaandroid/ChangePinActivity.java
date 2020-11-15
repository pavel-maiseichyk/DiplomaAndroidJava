package com.example.diplomaandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.MovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePinActivity extends AppCompatActivity {
    static SharedPreferences pinPreferences;
    static SharedPreferences isFirstTimePrefs;

    TextView newPinWarning;
    EditText newPinET;
    String newPin;

    Button saveButton;
    ImageButton viewPinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        init();
    }

    public void init() {

        createToolbar();

        pinPreferences = getSharedPreferences(AllSharedPreferences.PIN_PREFS, MODE_PRIVATE);
        isFirstTimePrefs = getSharedPreferences(AllSharedPreferences.FIRST_TIME_PREFS, MODE_PRIVATE);

        newPinET = findViewById(R.id.newPin);

        newPinWarning = findViewById(R.id.new_pin_warning);

        saveButton = findViewById(R.id.saveNewPinButton);

        saveButton.setOnClickListener(view -> {
            newPin = newPinET.getText().toString();

            if (!App.getPasswordRepository().saveNew(newPin)) {
                newPinWarning.setText("Ноуп, подавай 4 циферки");
                newPinET.setText("");
            } else {

                Toast.makeText(ChangePinActivity.this, "PIN сохранен!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePinActivity.this, PinActivity.class);
                startActivity(intent);
            }
        });

        viewPinButton = findViewById(R.id.buttonViewPin);
        viewPinButton.setOnClickListener(view -> {
            if (newPinET.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                newPinET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                viewPinButton.setImageDrawable(ContextCompat.getDrawable(ChangePinActivity.this, R.drawable.visibility_visible));
            } else {
                newPinET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                viewPinButton.setImageDrawable(ContextCompat.getDrawable(ChangePinActivity.this, R.drawable.visibility_off));
            }
        });
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.change_pin_toolbar);
        toolbar.setTitle("Меняем парольчик...");
        setSupportActionBar(toolbar);
    }
}