package com.example.upiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.upiapp.utils.LocalDataStore;

public class ProfileActivity extends AppCompatActivity {

    private LocalDataStore dataStore;
    private Button btnLogout;

    // UI elements to display profile data
    private TextView textUsername, textPhoneNumber, textDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataStore = new LocalDataStore(this);

        // Initialize UI components
        textUsername = findViewById(R.id.text_username);
        textPhoneNumber = findViewById(R.id.text_phone_number);
        textDeviceId = findViewById(R.id.text_device_id);
        btnLogout = findViewById(R.id.btn_logout);

        displayDummyProfileData();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
    }

    private void displayDummyProfileData() {
        // Displaying dummy data as per the project requirements
        textUsername.setText("Kartik_" + System.currentTimeMillis() % 1000); // Dynamic dummy ID
        textPhoneNumber.setText("+91 8421497761");
        // Showing the device ID requirement
        textDeviceId.setText("XYZ123_Android_Simulator");
    }

    private void performLogout() {
        // Clear the logged in status in SharedPreferences
        dataStore.logout();

        Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show();

        // Navigate back to LoginActivity and clear the activity stack
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}