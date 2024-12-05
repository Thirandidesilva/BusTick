package com.example.proj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

public class SeatBookActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView busNumberTextView, driverTextView, seatsTextView;
    private LinearLayout seatingPlanContainer;

    // Store the selected seat number
    private int selectedSeatNumber = -1; // -1 means no seat is selected initially

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
        updateSeatingPlan(seats);
        updateSeatStatus(busId);

        Button buttonBook = findViewById(R.id.buttonBook);
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if a seat has been selected
                if (selectedSeatNumber != -1) {
                    // Proceed with booking the selected seat
                    int busId = getIntent().getIntExtra("BUS_ID", 0);
                    int userId = getUserId();  // Use the dummy or real user ID

                    // Call the bookSeat method from the database helper
                    DatabaseHelper databaseHelper = new DatabaseHelper(SeatBookActivity.this);
                    boolean isBooked = databaseHelper.bookSeat(busId, selectedSeatNumber, userId);

                    if (isBooked) {
                        updateSeatStatus(busId); // Refresh the UI after successful booking
                        Toast.makeText(SeatBookActivity.this, "Seat " + selectedSeatNumber + " booked successfully!", Toast.LENGTH_SHORT).show();

                        // Navigate to the seat book confirmation page
                        Intent confirmationIntent = new Intent(SeatBookActivity.this, CompletedActivity.class);
                        confirmationIntent.putExtra("SELECTED_SEAT", selectedSeatNumber);
                        startActivity(confirmationIntent);
                    } else {
                        Toast.makeText(SeatBookActivity.this, "Failed to book seat " + selectedSeatNumber + ". Try another seat.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SeatBookActivity.this, "Please select a seat to book.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if a seat has been selected
                if (selectedSeatNumber != -1) {
                    int busId = getIntent().getIntExtra("BUS_ID", 0);
                    int userId = getUserId();  // Use the dummy or real user ID

                    // Call the cancelSeatBooking method from the database helper
                    DatabaseHelper databaseHelper = new DatabaseHelper(SeatBookActivity.this);
                    boolean isCanceled = databaseHelper.cancelSeatBooking(busId, selectedSeatNumber, userId);

                    if (isCanceled) {
                        // Successfully canceled the seat booking
                        updateSeatStatus(busId);  // Refresh the seat status UI
                        Toast.makeText(SeatBookActivity.this, "Booking for seat " + selectedSeatNumber + " canceled successfully.", Toast.LENGTH_SHORT).show();

                        // Navigate to the canceled confirmation page (Optional)
                        Intent canceledIntent = new Intent(SeatBookActivity.this, CancelledActivity.class);
                        canceledIntent.putExtra("SELECTED_SEAT", selectedSeatNumber);
                        startActivity(canceledIntent);
                    } else {
                        // Failed to cancel the booking (e.g., the seat might not be booked by the user)
                        Toast.makeText(SeatBookActivity.this, "Failed to cancel booking for seat " + selectedSeatNumber + ".", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // No seat selected for cancelation
                    Toast.makeText(SeatBookActivity.this, "Please select a seat to cancel the booking.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Method to update the seating plan dynamically based on the bus type (seats)
    private void updateSeatingPlan(int seats) {
        // Clear the seating plan container before updating
        seatingPlanContainer.removeAllViews();

        // Dynamically create seating plan based on the number of seats
        if (seats == 24) {
            // Create a seating plan for a 24-seater bus (e.g., 4 rows of 6 seats)
            createSeatingPlan(6, 4);
        } else if (seats == 48) {
            // Create a seating plan for a 48-seater bus (e.g., 6 rows of 8 seats)
            createSeatingPlan(12, 4);
        } else if (seats == 52) {
            // Create a seating plan for a 52-seater bus (e.g., 6 rows of 8 seats + 1 row of 4 seats)
            createSeatingPlan(13, 4); // Adjust the layout to have an extra row if needed
        } else {
            // Handle the case when the number of seats is unexpected
            Toast.makeText(this, "Invalid seating configuration", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to create the seating plan dynamically
    private void createSeatingPlan(int rows, int seatsPerRow) {
        seatingPlanContainer.removeAllViews(); // Ensure container is cleared.

        for (int i = 0; i < rows; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < seatsPerRow; j++) {
                TextView seatView = new TextView(this);
                int seatNumber = (i * seatsPerRow) + j + 1;
                seatView.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                seatView.setText(String.valueOf(seatNumber));
                seatView.setGravity(Gravity.CENTER);
                seatView.setBackgroundResource(R.drawable.textview_background); // Add a custom background.
                seatView.setPadding(16, 16, 16, 16);

                // Set the seat's click listener
                seatView.setOnClickListener(v -> {
                    selectedSeatNumber = seatNumber;  // Store the selected seat number
                    seatView.setBackgroundResource(R.drawable.selected_seat); // Highlight the selected seat
                });

                rowLayout.addView(seatView);
            }

            seatingPlanContainer.addView(rowLayout);
        }
    }

    private void updateSeatStatus(int busId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Seat> seatList = databaseHelper.getAllSeatsForBus(busId);

        for (Seat seat : seatList) {
            int seatNumber = seat.getSeatNumber();
            String status = seat.getStatus();

            // Update seat appearance based on its status
            TextView seatView = findSeatViewByNumber(seatNumber); // Custom helper method to locate the seat view
            if (seatView != null) {
                if ("booked".equalsIgnoreCase(status)) {
                    seatView.setBackgroundResource(R.drawable.seat_booked);
                } else if ("available".equalsIgnoreCase(status)) {
                    seatView.setBackgroundResource(R.drawable.seat_available);
                }
            }
        }
    }

    private TextView findSeatViewByNumber(int seatNumber) {
        // Traverse the seating container to locate the TextView for the seat number.
        for (int i = 0; i < seatingPlanContainer.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) seatingPlanContainer.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView seatView = (TextView) row.getChildAt(j);
                if (Integer.parseInt(seatView.getText().toString()) == seatNumber) {
                    return seatView;
                }
            }
        }
        return null;
    }
}
