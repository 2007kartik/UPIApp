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
import com.example.upiapp.utils.LocalDataStore; // Import LocalDataStore

public class HomeFragment extends Fragment {

    private LocalDataStore dataStore; // Declare LocalDataStore

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize LocalDataStore
        dataStore = new LocalDataStore(getActivity());

        // Link the Send Money button
        Button btnSendMoney = view.findViewById(R.id.btn_send_money);
        Button btnReceiveMoney = view.findViewById(R.id.btn_receive_money);

        // 1. Handle Send Money click (Conditional Redirection)
        btnSendMoney.setOnClickListener(v -> {
            Intent intent;

            // CRITICAL LOGIC: Check Developer Mode status
            if (dataStore.isDeveloperModeEnabled()) {
                // Developer Mode ON: Redirect to the Fraud Engine testing screen
                intent = new Intent(getActivity(), DeveloperPaymentActivity.class);
                Toast.makeText(getActivity(), "Developer Mode: Redirecting to Fraud Check Test.", Toast.LENGTH_LONG).show();
            } else {
                // Developer Mode OFF: Redirect to the regular payment screen
                intent = new Intent(getActivity(), SendMoneyActivity.class);
            }

            startActivity(intent);
        });

        // 2. Handle Receive Money click (Placeholder for now)
        btnReceiveMoney.setOnClickListener(v -> {
            // Redirect to the new ReceiveMoneyActivity
            startActivity(new Intent(getActivity(), ReceiveMoneyActivity.class));
        });

        // Note: Logic for text_account_balance would also go here.
    }
}