package com.example.upiapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.upiapp.R;
import com.example.upiapp.ReceiveMoneyActivity;
import com.example.upiapp.SendMoneyActivity;
import com.example.upiapp.SendMoneyDeveloperModeActivity; // Import the New Dev Activity
import com.example.upiapp.utils.SecurePrefManager; // Use SecurePrefManager

public class HomeFragment extends Fragment {

    private SecurePrefManager prefManager; // Use SecurePrefManager instead of LocalDataStore

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize SecurePrefManager
        prefManager = new SecurePrefManager(getActivity());

        Button btnSendMoney = view.findViewById(R.id.btn_send_money);
        Button btnReceiveMoney = view.findViewById(R.id.btn_receive_money);

        // 1. Handle Send Money click (Dynamic Redirection)
        btnSendMoney.setOnClickListener(v -> {
            Intent intent;

            // Check if Developer Mode is toggled ON in Secure Preferences
            if (prefManager.isDeveloperModeEnabled()) {
                // Redirect to the RAW JSON Editor Activity
                intent = new Intent(getActivity(), SendMoneyDeveloperModeActivity.class);
                Toast.makeText(getActivity(), "Dev Mode: Direct JSON Access", Toast.LENGTH_SHORT).show();
            } else {
                // Redirect to the standard UPI payment flow
                intent = new Intent(getActivity(), SendMoneyActivity.class);
            }

            startActivity(intent);
        });

        // 2. Handle Receive Money click
        btnReceiveMoney.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ReceiveMoneyActivity.class));
        });
    }
}