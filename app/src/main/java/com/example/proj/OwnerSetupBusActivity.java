package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OwnerSetupBusActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_setup_bus);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Handle navigation item selection
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

        // Get references to input fields and buttons
        EditText editTextBusNumber = findViewById(R.id.editTextBus);
        EditText editTextStartLocation = findViewById(R.id.editTextStart);
        EditText editTextEndLocation = findViewById(R.id.editTextEnd);
        EditText editTextRoute = findViewById(R.id.editTextBusRoute);
        EditText editTextDriver = findViewById(R.id.editTextDriver);
        EditText editTextSeats = findViewById(R.id.editTextSeat);

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(view -> {
            String busNumber = editTextBusNumber.getText().toString();
            String startLocation = editTextStartLocation.getText().toString();
            String endLocation = editTextEndLocation.getText().toString();
            String route = editTextRoute.getText().toString();
            String driver = editTextDriver.getText().toString();
            String seatsStr = editTextSeats.getText().toString();

            if (busNumber.isEmpty() || startLocation.isEmpty() || endLocation.isEmpty() ||
                    route.isEmpty() || driver.isEmpty() || seatsStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int seats;
            try {
                seats = Integer.parseInt(seatsStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Seats must be a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert bus data into the database
            long result = databaseHelper.insertBus(busNumber, startLocation, endLocation, route, driver, seats);

            if (result != -1) {
                Toast.makeText(this, "Bus saved successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OwnerSetupBusActivity.this, OwnerBusSavedActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to save bus", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonRemove = findViewById(R.id.buttonRemove);
        buttonRemove.setOnClickListener(view -> {
            Intent intent = new Intent(OwnerSetupBusActivity.this, OwnerBusRemovedActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
