package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SelectBusActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Retrieve intent data passed from HomeActivity
        Intent intent = getIntent();
        int busId = intent.getIntExtra("BUS_ID", 0);
        String busNumber = intent.getStringExtra("BUS_NUMBER");
        String startLocation = intent.getStringExtra("START_LOCATION");
        String endLocation = intent.getStringExtra("END_LOCATION");
        String route = intent.getStringExtra("ROUTE");
        String driver = intent.getStringExtra("DRIVER");
        int seats = intent.getIntExtra("SEATS", 0);

        // Display retrieved data
        Button busNumberTextView = findViewById(R.id.pick_bus);
        TextView startLocationTextView = findViewById(R.id.textStart);
        TextView endLocationTextView = findViewById(R.id.textEnd);

        if (busNumber != null) {
            busNumberTextView.setText(busNumber);
        } else {
            busNumberTextView.setText("Bus number unavailable");
        }

        if (startLocation != null) {
            startLocationTextView.setText(startLocation);
        } else {
            startLocationTextView.setText("Start location unavailable");
        }

        if (endLocation != null) {
            endLocationTextView.setText(endLocation);
        } else {
            endLocationTextView.setText("End location unavailable");
        }

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the selected item (optional, useful for showing the selected item after navigating back)
        bottomNavigationView.setSelectedItemId(R.id.home);

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

        busNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(busNumber.isEmpty()) {
                    Toast.makeText(SelectBusActivity.this, "Please select a bus", Toast.LENGTH_SHORT).show();
                } else {
                    // Retrieve the bus details from the database
                    Bus bus = databaseHelper.getBusDetails(busNumber);

                    if (bus != null) {
                        // Pass the bus details to the next activity
                        Intent intent = new Intent(SelectBusActivity.this, SeatBookActivity.class);
                        intent.putExtra("BUS_ID", bus.getBusId());
                        intent.putExtra("BUS_NUMBER", bus.getBusNumber());
                        intent.putExtra("START_LOCATION", bus.getStartLocation());
                        intent.putExtra("END_LOCATION", bus.getEndLocation());
                        intent.putExtra("ROUTE", bus.getRoute());
                        intent.putExtra("DRIVER", bus.getDriver());
                        intent.putExtra("SEATS", bus.getSeats());
                        startActivity(intent);
                    } else {
                        Toast.makeText(SelectBusActivity.this, "Bus details not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
