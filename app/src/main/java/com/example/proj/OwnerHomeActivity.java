package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class OwnerHomeActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_home);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the selected item (optional, useful for showing the selected item after navigating back)
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

        Button view_details_btn = findViewById(R.id.view_details_btn);
        view_details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwnerHomeActivity.this, OwnerViewBusDetailsActivity.class);
                startActivity(intent);
            }
        });

        Button view_details_btn2 = findViewById(R.id.add_bus_btn);
        view_details_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwnerHomeActivity.this, OwnerSetupBusActivity.class);
                startActivity(intent);
            }
        });

        // Reference to the spinner
        Spinner spinner = findViewById(R.id.owner_spinner);

        // Populate spinner with buses from the database
        populateBusSpinner(spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.dropdown_items,
//                android.R.layout.simple_spinner_item
//        );

        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);
    }

    private void populateBusSpinner(Spinner spinner) {
        // Retrieve bus numbers from the database
        List<String> busList = databaseHelper.getAllBuses();

        if (busList.isEmpty()) {
            Toast.makeText(this, "No buses available", Toast.LENGTH_SHORT).show();
        } else {
            // Create an ArrayAdapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    busList
            );

            // Set dropdown style
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Assign the adapter to the spinner
            spinner.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

}