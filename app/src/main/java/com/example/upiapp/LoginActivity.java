package com.example.upiapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.upiapp.utils.LocalDataStore;

public class LoginActivity extends AppCompatActivity {

    private EditText editPhoneNumber, editOtp;
    private Button btnLogin;
    private LocalDataStore dataStore;
    private final String DUMMY_OTP = "1236";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataStore = new LocalDataStore(this);

        // Check if the user is already logged in (for a seamless demo)
        if (dataStore.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return; // Stop execution here
        }

        setContentView(R.layout.activity_login);

        editPhoneNumber = findViewById(R.id.edit_phone_number);
        editOtp = findViewById(R.id.edit_otp);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // Disable the actual Send OTP button as per requirement [cite: 26]
        findViewById(R.id.btn_send_otp).setVisibility(View.GONE);
    }

    private void attemptLogin() {
        String phone = editPhoneNumber.getText().toString().trim();
        String otp = editOtp.getText().toString().trim();

        if (phone.isEmpty() || otp.isEmpty()) {
            Toast.makeText(this, "Please enter phone number and OTP.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Dummy authentication logic [cite: 25, 26, 80]
        if (otp.equals(DUMMY_OTP) && phone.length() == 10) {
            dataStore.setLoggedIn(true);
            Toast.makeText(this, "Login Successful! Redirecting...", Toast.LENGTH_SHORT).show();

            // Navigate to the Home Dashboard
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid phone number or OTP. Use 1234.", Toast.LENGTH_LONG).show();
        }
    }
}