package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SeatExchangeRequestActivity extends AppCompatActivity {
    private int selectedSeat;
    private int busId;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_exchange_request);

//        // Get data from the intent
//        Intent intent = getIntent();
//        selectedSeat = intent.getIntExtra("SELECTED_SEAT", -1);
//        busId = intent.getIntExtra("BUS_ID", 0);
//
//        TextView requestSeatTextView = findViewById(R.id.requestSeatTextView);
//        EditText swapSeatEditText = findViewById(R.id.swapSeatEditText);
//        Button sendRequestButton = findViewById(R.id.sendRequestButton);
//
//        // Display the selected seat
//        requestSeatTextView.setText("Seat (Request For): " + selectedSeat);
//
//        sendRequestButton.setOnClickListener(v -> {
//            String swapSeatInput = swapSeatEditText.getText().toString().trim();
//            if (!swapSeatInput.isEmpty()) {
//                int swapSeat = Integer.parseInt(swapSeatInput);
//
//                // Send the request to the database or API
//                databaseHelper = new DatabaseHelper(SeatExchangeRequestActivity.this);
//                boolean requestSent = databaseHelper.requestSeatSwap(busId, selectedSeat, swapSeat, getUserId());
//
//                if (requestSent) {
//                    Toast.makeText(SeatExchangeRequestActivity.this, "Swap request sent successfully.", Toast.LENGTH_SHORT).show();
//                    finish(); // Close the activity
//                } else {
//                    Toast.makeText(SeatExchangeRequestActivity.this, "Failed to send swap request.", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(SeatExchangeRequestActivity.this, "Please enter a seat number to swap.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private int getUserId() {
//        return 1; // Static value for testing purposes
//    }

    }
}