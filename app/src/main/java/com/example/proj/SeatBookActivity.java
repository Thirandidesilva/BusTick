package com.example.proj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SeatBookActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView busNumberTextView, driverTextView, seatsTextView;
    private LinearLayout seatingPlanContainer;

    private int getUserId() {
        return 1; // Static value for testing purposes
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seat_book);

        // Initialize views
        busNumberTextView = findViewById(R.id.bus_number);
        driverTextView = findViewById(R.id.driver_name);
        seatsTextView = findViewById(R.id.seatsTextView);
        seatingPlanContainer = findViewById(R.id.seatingPlanContainer);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the selected item (optional, useful for showing the selected item after navigating back)
        bottomNavigationView.setSelectedItemId(R.id.reserved);

        // Handle navigation item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (id == R.id.reserved) {
                startActivity(new Intent(this, ReservedActivity.class));
                return true;
            } else if (id == R.id.message) {
                startActivity(new Intent(this, MessageActivity.class));
                return true;
            } else if (id == R.id.profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }

            return false;
        });

        // Get the bus details passed from SelectBusActivity
        Intent intent = getIntent();
        int busId = intent.getIntExtra("BUS_ID", 0);
        String busNumber = intent.getStringExtra("BUS_NUMBER");
        String startLocation = intent.getStringExtra("START_LOCATION");
        String endLocation = intent.getStringExtra("END_LOCATION");
        String route = intent.getStringExtra("ROUTE");
        String driver = intent.getStringExtra("DRIVER");
        int seats = intent.getIntExtra("SEATS", 0);

        // Populate the fields with the bus details
        busNumberTextView.setText(busNumber);
        driverTextView.setText(driver);
        seatsTextView.setText(String.valueOf(seats));

        // Update the seating plan based on the number of seats of the bus
        updateSeatingPlan(busId, seats);

        Button buttonBook = findViewById(R.id.buttonBook);
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeatBookActivity.this, CompletedActivity.class);
                startActivity(intent);
            }
        });
        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeatBookActivity.this, CancelledActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to update the seating plan dynamically based on the bus type (seats)
    private void updateSeatingPlan(int busId, int seats) {
        // Clear the seating plan container
        seatingPlanContainer.removeAllViews();

        // Get seating details from the database
        Cursor cursor = dbHelper.getSeatingDetails(busId);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < seats; i++) {
                // Create a row layout dynamically for every 4 seats (or seatsPerRow)
                LinearLayout rowLayout = new LinearLayout(this);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                for (int j = 0; j < 4 && !cursor.isAfterLast(); j++) {
                    int seatNumber = cursor.getInt(cursor.getColumnIndex(dbHelper.COLUMN_SEAT_NUMBER));
                    String status = cursor.getString(cursor.getColumnIndex(dbHelper.COLUMN_STATUS));

                    // Create a TextView for each seat
                    TextView seatView = new TextView(this);
                    seatView.setLayoutParams(new LinearLayout.LayoutParams(0,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    seatView.setText(String.valueOf(seatNumber));
                    seatView.setGravity(Gravity.CENTER);

                    // Set seat background color based on status
                    if ("booked".equals(status)) {
                        seatView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    } else {
                        seatView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                        seatView.setOnClickListener(v -> {
                            // Attempt to book the seat
                            if (dbHelper.bookSeat(busId, seatNumber, getUserId())) {
                                Toast.makeText(this, "Seat " + seatNumber + " booked successfully!", Toast.LENGTH_SHORT).show();
                                updateSeatingPlan(busId, seats); // Refresh layout
                            } else {
                                Toast.makeText(this, "Seat " + seatNumber + " is already booked.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    rowLayout.addView(seatView); // Add seat to the row
                    cursor.moveToNext(); // Move to next seat
                }

                seatingPlanContainer.addView(rowLayout); // Add row to container
            }
        }

        cursor.close();
        dbHelper.close();
    }


    // Helper method to create the seating plan dynamically
    private void createSeatingPlan(int rows, int seatsPerRow) {
        LinearLayout seatingPlanContainer = findViewById(R.id.seatingPlanContainer);

        for (int i = 0; i < rows; i++) {
            // Create a row layout dynamically
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            // Add seats to the row
            for (int j = 0; j < seatsPerRow; j++) {
                // Dynamically create a seat (TextView for simplicity)
                TextView seatView = new TextView(this);
                seatView.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                seatView.setText(String.valueOf((i * seatsPerRow) + j + 1));
                seatView.setGravity(Gravity.CENTER);// Set your custom background
                rowLayout.addView(seatView); // Add seat to row
            }

            seatingPlanContainer.addView(rowLayout); // Add row to seating plan
        }
    }
}