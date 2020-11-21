package com.example.diplomaandroid;

import android.content.Context;
import android.content.SharedPreferences;

public class SimpleKeystore implements PasswordRepository {
    private static SimpleKeystore instance;
    private Context context;

    private SimpleKeystore(Context context) {
        this.context = context;
    }

    public static SimpleKeystore getInstance(Context context) {
        if (instance == null)
            instance = new SimpleKeystore(context);
        return instance;
    }

    @Override
    public boolean hasPin() {
        SharedPreferences isFirstTimePreferences = context.getSharedPreferences(AllSharedPreferences.FIRST_TIME_PREFS, Context.MODE_PRIVATE);
        return isFirstTimePreferences.getBoolean(AllSharedPreferences.FIRST_TIME, true);
    }

    @Override
    public boolean checkPin(String pin) {
        SharedPreferences pinPreferences = context.getSharedPreferences(AllSharedPreferences.PIN_PREFS, Context.MODE_PRIVATE);
        return pin.equals(pinPreferences.getString(AllSharedPreferences.PIN, ""));
    }

    @Override
    public boolean saveNew(String newPin) {
        if (newPin.length() != 4) {
            return false;
        } else {
            SharedPreferences pinPreferences = context.getSharedPreferences(AllSharedPreferences.PIN_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor pinEditor = pinPreferences.edit();
            pinEditor.putString(AllSharedPreferences.PIN, newPin).apply();

            SharedPreferences isFirstTimePreferences = context.getSharedPreferences(AllSharedPreferences.FIRST_TIME_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor isFirstTimeEditor = isFirstTimePreferences.edit();
            isFirstTimeEditor.putBoolean(AllSharedPreferences.FIRST_TIME, false).apply();
            return true;
        }
    }
}
