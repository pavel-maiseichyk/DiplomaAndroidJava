package com.example.diplomaandroid;

public interface PasswordRepository {
        boolean hasPin();
        boolean checkPin(String pin);
        void saveNew(String pin);
}
