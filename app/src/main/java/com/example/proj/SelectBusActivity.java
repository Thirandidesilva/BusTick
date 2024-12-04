package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SelectBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        // Retrieve intent data passed from HomeActivity
        Intent intent = getIntent();
        String busNumber = intent.getStringExtra("BUS_NUMBER");
        String startLocation = intent.getStringExtra("START_LOCATION");
        String endLocation = intent.getStringExtra("END_LOCATION");
        String route = intent.getStringExtra("ROUTE");
        String driver = intent.getStringExtra("DRIVER");
        int seats = intent.getIntExtra("SEATS", 0);

        // Display retrieved data
        TextView busNumberTextView = findViewById(R.id.pick_bus);
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
            public void onClick(View v) {
                // Start BookingActivity
                Intent intent = new Intent(SelectBusActivity.this, BusRouteViewActivity.class);
                startActivity(intent);
            }
        });

    }
}
