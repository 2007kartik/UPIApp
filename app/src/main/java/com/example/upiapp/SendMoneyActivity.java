package com.example.upiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class SendMoneyActivity extends AppCompatActivity {

    private EditText editReceiverUpiId, editAmount, editMessage;
    private Button btnPay, btnScanQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        editReceiverUpiId = findViewById(R.id.edit_receiver_upi_id);
        editAmount = findViewById(R.id.edit_amount);
        editMessage = findViewById(R.id.edit_message);
        btnPay = findViewById(R.id.btn_pay);
        btnScanQr = findViewById(R.id.btn_scan_qr);

        btnScanQr.setOnClickListener(v -> startQrScanner());
        btnPay.setOnClickListener(v -> initiatePaymentFlow());

        ImageButton btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animation
                v.animate()
                        .scaleX(0.85f)
                        .scaleY(0.85f)
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                v.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(100)
                                        .start();

                                finish(); // Just closes current activity
                            }
                        })
                        .start();
            }
        });
    }

    // 🔹 START QR SCANNER
    private void startQrScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan UPI QR Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivityPortrait.class);
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);

        qrScannerLauncher.launch(options);
    }

    // 🔹 QR RESULT HANDLER (THIS IS THE PART YOU ASKED ABOUT)
    private final ActivityResultLauncher<ScanOptions> qrScannerLauncher =
            registerForActivityResult(new ScanContract(), result -> {

                if (result.getContents() == null) {
                    Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                String scannedText = result.getContents().trim();

                /*
                 EXPECTED QR CONTENT:
                 UPI:
                 ID=UserX123@demoupi;
                 NAME=UserX123;
                 */

                if (scannedText.startsWith("UPI:")) {
                    try {
                        String upiId = extractUpiId(scannedText);
                        editReceiverUpiId.setText(upiId);
                        Toast.makeText(this, "UPI ID detected: " + upiId, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Invalid UPI QR format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
                }
            });

    // 🔹 PARSE UPI ID
    private String extractUpiId(String qrText) {
        qrText = qrText.replace("UPI:", "").trim();

        String[] parts = qrText.split(";");

        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("ID=")) {
                return part.replace("ID=", "").trim();
            }
        }
        throw new IllegalArgumentException("UPI ID not found");
    }

    // 🔹 PAYMENT FLOW
    private void initiatePaymentFlow() {
        String receiverId = editReceiverUpiId.getText().toString().trim();
        String amountStr = editAmount.getText().toString().trim();
        String message = editMessage.getText().toString().trim();

        if (receiverId.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Enter UPI ID and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ConfirmPinActivity.class);
        intent.putExtra("RECEIVER_ID", receiverId);
        intent.putExtra("AMOUNT", amount);
        intent.putExtra("MESSAGE", message);
        intent.putExtra("IS_DEV_MODE", false);

        startActivity(intent);
    }
}
