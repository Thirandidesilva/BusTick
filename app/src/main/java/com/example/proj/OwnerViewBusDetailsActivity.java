package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OwnerViewBusDetailsActivity extends AppCompatActivity {

    private TextView busNumberTextView, startLocationTextView, endLocationTextView, routeTextView, driverTextView, seatsTextView;
    private View seatingPlanContainer;

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
        String busNumber = intent.getStringExtra("BUS_NUMBER");
        String startLocation = intent.getStringExtra("START_LOCATION");
        String endLocation = intent.getStringExtra("END_LOCATION");
        String route = intent.getStringExtra("ROUTE");
        String driver = intent.getStringExtra("DRIVER");
        int seats = intent.getIntExtra("SEATS", 0);

        // Populate the fields with the bus details
        busNumberTextView.setText(busNumber);
        startLocationTextView.setText(startLocation);
        endLocationTextView.setText(endLocation);
        routeTextView.setText(route);
        driverTextView.setText(driver);
        seatsTextView.setText(String.valueOf(seats));

        // Update the seating plan based on the bus type (seats)
        updateSeatingPlan(seats);

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

        // Dynamically create seating plan with 2 rows
        int columnsPerRow = seats / 2; // Divide seats equally between the 2 rows
        if (seats % 2 != 0) {
            columnsPerRow++; // If seats is odd, adjust columns for the first row
        }

        // Ensure the seat count is reasonable (e.g., 24, 48, 52)
        if (seats == 24 || seats == 48 || seats == 52) {
            // Create seating plan with 2 rows and dynamically calculated columns
            createSeatingPlan(2, columnsPerRow);
        } else {
            // Handle the case when the number of seats is unexpected
            Toast.makeText(this, "Invalid seating configuration", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to create the seating plan dynamically
    private void createSeatingPlan(int rows, int seatsPerRow) {
        for (int i = 0; i < rows; i++) {
            // Create a row layout dynamically
            View rowView = getLayoutInflater().inflate(R.layout.seating_row_layout, seatingPlanContainer, false);
            // Add seats to the row
            for (int j = 0; j < seatsPerRow; j++) {
                // Dynamically create and add seats to the row view
                View seatView = getLayoutInflater().inflate(R.layout.seat_layout, rowView, false);
                rowView.findViewById(R.id.seat_container).addView(seatView); // Add seat to row
            }
            seatingPlanContainer.addView(rowView); // Add row to seating plan
        }
    }
}
