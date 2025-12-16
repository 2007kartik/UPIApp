package com.example.upiapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.upiapp.utils.LocalDataStore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceiveMoneyActivity extends AppCompatActivity {

    private LocalDataStore dataStore;
    private TextView textReceiveUpiId;
    private ImageView imageQrCode;
    private Button btnShareUpiId;

    private String userUpiId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_money);

        dataStore = new LocalDataStore(this);

        textReceiveUpiId = findViewById(R.id.text_receive_upi_id);
        imageQrCode = findViewById(R.id.image_qr_code);
        btnShareUpiId = findViewById(R.id.btn_share_upi_id);

        displayUserUpiId();
        generateQrCode();

        btnShareUpiId.setOnClickListener(v -> shareUpiIdAndQr());
    }

    // 🔹 Fetch user and show UPI ID
    private void displayUserUpiId() {
        String mobileNumber = dataStore.getSavedUsername();

        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            userUpiId = mobileNumber + "@demoupi";
            userName = mobileNumber;
            textReceiveUpiId.setText(userUpiId);
        } else {
            userUpiId = null;
            Toast.makeText(this, "User data missing. Please login again.", Toast.LENGTH_LONG).show();
        }
    }

    // 🔹 QR CODE GENERATION (MAIN LOGIC)
    // 🔹 QR CODE GENERATION (CUSTOM UPI FORMAT LIKE WIFI QR)
    // 🔹 QR CODE GENERATION (CUSTOM UPI FORMAT LIKE WIFI QR)
    private void generateQrCode() {
        if (userUpiId == null || userName == null) return;

        try {
            // Custom structured QR content (WiFi-style)
            String qrContent =
                    "UPI:\n" +
                            "ID=" + userUpiId + ";\n" +
                            "NAME=" + userName + ";";

            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(
                    qrContent,
                    BarcodeFormat.QR_CODE,
                    600,
                    600
            );

            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);

            imageQrCode.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate QR Code", Toast.LENGTH_SHORT).show();
        }
    }



    // 🔹 Share UPI ID + QR Image
    private void shareUpiIdAndQr() {
        if (userUpiId == null) {
            Toast.makeText(this, "UPI ID not available", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri imageUri = getQrImageUri();

        if (imageUri != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.putExtra(Intent.EXTRA_TEXT,
                    "My UPI ID: " + userUpiId + "\nScan this QR to pay me.");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Share UPI via"));
        }
    }

    // 🔹 Convert ImageView QR to sharable URI
    private Uri getQrImageUri() {
        try {
            BitmapDrawable drawable = (BitmapDrawable) imageQrCode.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();

            File file = new File(cachePath, "upi_qr.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            return FileProvider.getUriForFile(
                    this,
                    "com.example.upiapp.fileprovider",
                    file
            );

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
