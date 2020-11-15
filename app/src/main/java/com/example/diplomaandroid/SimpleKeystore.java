package com.example.diplomaandroid;

import android.content.SharedPreferences;

public class SimpleKeystore implements PasswordRepository {
    public SimpleKeystore() {
    }

    @Override
    public boolean hasPin() {
        return PinActivity.isFirstTimePreferences.getBoolean(AllSharedPreferences.FIRST_TIME, true);
    }

    @Override
    public boolean checkPin(String pin) {
        return pin.equals(PinActivity.pinPreferences.getString(AllSharedPreferences.PIN, ""));
    }

    @Override
    public boolean saveNew(String newPin) {
        if (newPin.length() != 4) {
            return false;
        } else {
            SharedPreferences.Editor pinEditor = ChangePinActivity.pinPreferences.edit();
            pinEditor.putString(AllSharedPreferences.PIN, newPin).apply();

            SharedPreferences.Editor isFirstTimeEditor = ChangePinActivity.isFirstTimePrefs.edit();
            isFirstTimeEditor.putBoolean(AllSharedPreferences.FIRST_TIME, false).apply();
            return true;
        }
    }
}
