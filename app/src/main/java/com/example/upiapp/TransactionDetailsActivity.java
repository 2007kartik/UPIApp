package com.example.upiapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

public class TransactionDetailsActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        // Initialize TextViews
        TextView textStatus = findViewById(R.id.text_detail_status);
        TextView textSender = findViewById(R.id.text_detail_sender);
        TextView textReceiver = findViewById(R.id.text_detail_receiver);
        TextView textAmount = findViewById(R.id.text_detail_amount);
        TextView textMessage = findViewById(R.id.text_detail_message);
        TextView textRiskScore = findViewById(R.id.text_detail_risk_score);
        TextView textReason = findViewById(R.id.text_detail_reason);

        // Get data from Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String status = extras.getString("STATUS", "UNKNOWN");
            String sender = extras.getString("SENDER", "N/A");
            String receiver = extras.getString("RECEIVER", "N/A");
            double amount = extras.getDouble("AMOUNT", 0.0);
            String message = extras.getString("MESSAGE", "No message provided.");
            String riskScore = extras.getString("RISK_SCORE", "N/A");
            String reason = extras.getString("REASON", "Data not available.");

            // Populate UI
            textStatus.setText(status);
            textReceiver.setText(receiver);
            textSender.setText(sender);
            textAmount.setText(String.format("₹ %.2f", amount));
            textMessage.setText(message);
            textRiskScore.setText(riskScore);
            textReason.setText(reason);

            // Set color based on status
            int color;
            if (status.equals("APPROVED")) {
                color = Color.parseColor("#4CAF50"); // Green
            } else if (status.equals("FLAGGED")) {
                color = Color.parseColor("#FF9800"); // Orange
            } else { // BLOCKED or UNKNOWN
                color = Color.parseColor("#D81B60"); // Red
            }
            textStatus.setTextColor(color);
            textRiskScore.setTextColor(color);
        }
    }
}