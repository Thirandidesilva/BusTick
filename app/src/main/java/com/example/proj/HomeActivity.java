package com.example.proj;
import com.example.proj.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        // Add a click listener to the TextView with ID 'pick_location'
        TextView pickLocationTextView = findViewById(R.id.pick_location);
        pickLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BookingActivity
                Intent intent = new Intent(HomeActivity.this, SelectBusActivity.class);
                startActivity(intent);
            }
        });

        TextView greetingTextView = findViewById(R.id.textView2);
        setGreeting(greetingTextView);

    }

    // Method to determine and set the greeting text
    private void setGreeting(TextView greetingTextView) {
        // Get the current hour of the day
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Determine the appropriate greeting
        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening";
        } else {
            greeting = "Good Night";
        }

        // Set the greeting text in the TextView
        greetingTextView.setText(greeting);
    }



}
