package com.example.upiapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private RelativeLayout resultLayout;
    private ImageView iconStatus;
    private TextView textStatusTitle, textReason, textTransactionId;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultLayout = findViewById(R.id.result_layout);
        iconStatus = findViewById(R.id.icon_status);
        textStatusTitle = findViewById(R.id.text_status_title);
        textReason = findViewById(R.id.text_reason);
        textTransactionId = findViewById(R.id.text_transaction_id);
        btnDone = findViewById(R.id.btn_done);

        // Get data passed from SendMoneyActivity
        String status = getIntent().getStringExtra("TRANSACTION_STATUS");
        String reason = getIntent().getStringExtra("TRANSACTION_REASON");
        String txnId = getIntent().getStringExtra("TRANSACTION_ID");
        int riskScore = getIntent().getIntExtra("TRANSACTION_RISK", 0);

        displayResult(status, reason, txnId, riskScore);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the main home dashboard
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayResult(String status, String reason, String txnId, int riskScore) {
        String fullReason = reason + " (Risk Score: " + riskScore + ")";

        if ("APPROVED".equals(status)) {
            // APPROVED UI [cite: 60, 98, 99, 100]
            resultLayout.setBackgroundColor(Color.parseColor("#E8F5E9")); // Light Green
            iconStatus.setImageResource(android.R.drawable.ic_menu_edit); // Use a checkmark or similar
            textStatusTitle.setText("Payment Successful");
            textStatusTitle.setTextColor(Color.parseColor("#4CAF50")); // Green
            textReason.setText(fullReason);

        } else if ("FLAGGED".equals(status)) {
            // FLAGGED UI [cite: 61, 102, 103, 104]
            resultLayout.setBackgroundColor(Color.parseColor("#FFFDE7")); // Light Yellow
            iconStatus.setImageResource(android.R.drawable.ic_dialog_alert); // Use a warning icon
            textStatusTitle.setText("Suspicious Activity Detected");
            textStatusTitle.setTextColor(Color.parseColor("#FFC107")); // Yellow/Orange
            textReason.setText("Decision: FLAGGED. " + fullReason);

        } else if ("BLOCKED".equals(status)) {
            // BLOCKED UI [cite: 62, 105, 107, 108]
            resultLayout.setBackgroundColor(Color.parseColor("#FFEBEE")); // Light Red
            iconStatus.setImageResource(android.R.drawable.ic_delete); // Use a red X icon
            textStatusTitle.setText("Transaction Blocked High Fraud Risk");
            textStatusTitle.setTextColor(Color.RED);
            textReason.setText("Decision: BLOCKED. " + fullReason);
        }

        textTransactionId.setText("TXN ID: " + txnId); // Display Transaction ID [cite: 101]
    }
}