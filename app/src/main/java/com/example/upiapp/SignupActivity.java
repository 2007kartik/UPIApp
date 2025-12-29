package com.example.upiapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upiapp.models.SignupRequest;
import com.example.upiapp.service.ApiService;
import com.example.upiapp.utils.LocalDataStore;

public class SignupActivity extends AppCompatActivity {

    private EditText editMobile;
    private EditText editName;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private Button btnSignUp;
    private TextView textLoginInstead;

    private LocalDataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dataStore = new LocalDataStore(this);

        // Initialize UI components
        editMobile = findViewById(R.id.edit_signup_mobile);
        editName = findViewById(R.id.edit_signup_name);
        editPassword = findViewById(R.id.edit_signup_password);
        editConfirmPassword = findViewById(R.id.edit_signup_confirm_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        textLoginInstead = findViewById(R.id.text_login_instead);

        // Set up the Sign Up button click listener
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignup();
            }
        });

        // Set up the "Login instead" link click listener
        textLoginInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to LoginActivity
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void performSignup() {
        String mobile = editMobile.getText().toString().trim();
        String name = editName.getText().toString().trim(); // Get the name value
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        String upiID = name + "@mockupi";

        // Simple validation for the demo
        if (mobile.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mobile.length() != 10) {
            Toast.makeText(this, "Mobile number must be 10 digits.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- DEMO SUCCESS LOGIC ---
        ApiService apiService = ApiClient.getClient(this);

        SignupRequest request = new SignupRequest(
                upiID,
                name,
                password,
                "1234",
                mobile,
                "DEVICE_A"
        );
        Log.d("SIGNUP", "Processing");
        apiService.signup(request).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call,
                                   retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    // Signup success
                    Log.d("SIGNUP", "Signup successful");

                    // 1. Store the registered credentials AND NAME in LocalDataStore
                    dataStore.saveUserCredentials(mobile, password, name); // <-- UPDATED CALL

                    Toast.makeText(SignupActivity.this, "Sign Up Successful! Credentials Saved. Redirecting to Login. Login with UPI ID: " + upiID, Toast.LENGTH_LONG).show();

                    // 2. After successful dummy sign up, redirect to the login page
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    // Error response (500, 400, etc.)
                    Log.e("SIGNUP", "Signup failed: " + response.code() + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Log.e("SIGNUP", "Network error", t);
            }
        });
    }
}