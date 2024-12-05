package com.example.proj;

import android.content.Intent;
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

import java.util.List;

public class OwnerViewBusDetailsActivity extends AppCompatActivity {

    private TextView busNumberTextView, startLocationTextView, endLocationTextView, routeTextView, driverTextView, seatsTextView;
    private LinearLayout seatingPlanContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_view_bus_details);

        // Initialize views
        busNumberTextView = findViewById(R.id.busNumberTextView);
//        startLocationTextView = findViewById(R.id.startLocationTextView);
//        endLocationTextView = findViewById(R.id.endLocationTextView);
//        routeTextView = findViewById(R.id.routeTextView);
        driverTextView = findViewById(R.id.driverTextView);
        seatsTextView = findViewById(R.id.seatsTextView);
        seatingPlanContainer = findViewById(R.id.seatingPlanContainer);

        // Set window insets to avoid overlapping system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                startActivity(new Intent(this, OwnerHomeActivity.class));
                return true;
            } else if (id == R.id.reserved) {
                startActivity(new Intent(this, OwnerViewBusDetailsActivity.class));
                return true;
            } else if (id == R.id.message) {
                startActivity(new Intent(this, OwnerMessageActivity.class));
                return true;
            } else if (id == R.id.profile) {
                startActivity(new Intent(this, OwnerProfileActivity.class));
                return true;
            }

            return false;
        });

        // Get the bus details passed from OwnerHomeActivity
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
//        startLocationTextView.setText(startLocation);
//        endLocationTextView.setText(endLocation);
//        routeTextView.setText(route);
        driverTextView.setText(driver);
        seatsTextView.setText(String.valueOf(seats));

        // Update the seating plan based on the bus type (seats)
        updateSeatingPlan(seats);
        // Convert the string bus number to an int
        updateSeatStatus(busId);

        // Handle button clicks
        Button buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(view -> {
            Intent editIntent = new Intent(OwnerViewBusDetailsActivity.this, OwnerSetupBusActivity.class);
            startActivity(editIntent);
        });

        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(view -> {
            Intent cancelIntent = new Intent(OwnerViewBusDetailsActivity.this, OwnerBusRemovedActivity.class);
            startActivity(cancelIntent);
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
