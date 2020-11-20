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

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ChangePinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        init();
    }

    public void init() {
        createToolbar();

        EditText newPinET = findViewById(R.id.newPin);
        Button saveButton = findViewById(R.id.saveNewPinButton);
        saveButton.setOnClickListener(view -> {
            String newPin = newPinET.getText().toString();
            if (!App.getPasswordRepository().saveNew(newPin)) {
                TextInputLayout passwordLayout = findViewById(R.id.password_layout);
                passwordLayout.setError(getResources().getString(R.string.new_password_error));
            } else {
                PinActivity.isFirstTimePreferences.edit().putBoolean(AllSharedPreferences.FIRST_TIME, false).apply();
                Toast.makeText(ChangePinActivity.this, getResources().getString(R.string.new_pin_saved), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePinActivity.this, PinActivity.class);
                startActivity(intent);
            }
        });

        ImageButton viewPinButton = findViewById(R.id.buttonViewPin);
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
        toolbar.setTitle(getResources().getString(R.string.change_pin_title));
        setSupportActionBar(toolbar);
        if (!App.getPasswordRepository().hasPin()) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }
}