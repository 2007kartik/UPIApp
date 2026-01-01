package com.example.upiapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.upiapp.ApiClient;
import com.example.upiapp.R;
import com.example.upiapp.adapters.TransactionAdapter;
import com.example.upiapp.models.Transaction;
import com.example.upiapp.models.WalletResponse;
import com.example.upiapp.service.ApiService;
import com.example.upiapp.models.TransactionHistoryResponse;
import com.example.upiapp.models.ProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoneyFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textAccountBalance;
    private TextView textEmptyHistory;
    private String myUpiId = ""; // To store the logged-in user's ID


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ESSENTIAL: This must be present to inflate the layout [cite: 1]
        return inflater.inflate(R.layout.fragment_money, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        recyclerView = view.findViewById(R.id.recycler_view_history);
        textAccountBalance = view.findViewById(R.id.text_account_balance);
        textEmptyHistory = view.findViewById(R.id.text_empty_history);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchProfileAndHistory();
        fetchWalletBalance();   // GET /wallet/balance [cite: 47]
//        fetchTransactionHistory(); // GET /transactions/history [cite: 76]
    }

    private void fetchProfileAndHistory() {
        ApiService apiService = ApiClient.getClient(getContext());

        // Step 1: Get Profile to find out "Who am I?"
        apiService.getProfile().enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    myUpiId = response.body().upiId; // Store our UPI ID [cite: 42]

                    // Step 2: Now that we know our ID, fetch history
                    fetchTransactionHistory();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("PROFILE", "Failed to fetch profile info");
            }
        });
    }

    private void fetchTransactionHistory() {
        ApiService apiService = ApiClient.getClient(getContext());
        apiService.getTransactionHistory().enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call, Response<TransactionHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Transaction> transactions = response.body().transactions;

                    // Pass our retrieved UPI ID to the adapter for coloring logic
                    TransactionAdapter adapter = new TransactionAdapter(transactions, myUpiId);
                    recyclerView.setAdapter(adapter);
                    showEmptyState(transactions.isEmpty());
                }
            }
            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) { /* handle error */ }
        });
    }

    private void fetchWalletBalance() {
        if (getContext() == null) return;

        ApiService apiService = ApiClient.getClient(getContext());
        apiService.getBalance().enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(@NonNull Call<WalletResponse> call, @NonNull Response<WalletResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int balance = response.body().balance;
                    textAccountBalance.setText("₹ " + balance);
                } else if (response.code() == 401) {
                    textAccountBalance.setText("₹ --");
                    Toast.makeText(getActivity(), "Session expired", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WalletResponse> call, @NonNull Throwable t) {
                textAccountBalance.setText("₹ Error");
            }
        });
    }

//    private void fetchTransactionHistory() {
//        if (getContext() == null) return;
//
//        ApiService apiService = ApiClient.getClient(getContext());
//        apiService.getTransactionHistory().enqueue(new Callback<TransactionHistoryResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<TransactionHistoryResponse> call, @NonNull Response<TransactionHistoryResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Transaction> transactions = response.body().transactions;
//
//                    if (transactions == null || transactions.isEmpty()) {
//                        showEmptyState(true);
//                    } else {
//                        showEmptyState(false);
//                        TransactionAdapter adapter = new TransactionAdapter(transactions, getContext());
//                        recyclerView.setAdapter(adapter);
//                    }
//                } else {
//                    showEmptyState(true);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<TransactionHistoryResponse> call, @NonNull Throwable t) {
//                showEmptyState(true);
//            }
//        });
//    }

    private void showEmptyState(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            if (textEmptyHistory != null) textEmptyHistory.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            if (textEmptyHistory != null) textEmptyHistory.setVisibility(View.GONE);
        }
    }
}