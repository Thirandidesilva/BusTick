package com.example.proj;
import com.example.proj.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private TextView dateTimeText;
    private Handler handler;
    private Runnable updateTimeRunnable;

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

        // Initialize date and time TextView
        dateTimeText = findViewById(R.id.date_time_text);

        // Set greeting text based on the time of day
        TextView greetingTextTime = findViewById(R.id.greetingTextTime);
        setGreetingText(greetingTextTime);


        // Setup Handler and Runnable for updating time
        handler = new Handler();
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                handler.postDelayed(this, 1000); // Update every second
            }
        };
        handler.post(updateTimeRunnable);
    }

    private void updateDateTime() {
        String currentDateTime = new SimpleDateFormat("EEEE, MMM d, yyyy hh:mm:ss a", Locale.getDefault())
                .format(new Date());
        dateTimeText.setText(currentDateTime);
    }

    private void setGreetingText(TextView greetingTextView) {
        int currentHour = new Date().getHours();

        if (currentHour >= 5 && currentHour < 12) {
            greetingTextView.setText("Good Morning,");
        } else if (currentHour >= 12 && currentHour < 17) {
            greetingTextView.setText("Good Afternoon,");
        } else if (currentHour >= 17 && currentHour < 21) {
            greetingTextView.setText("Good Evening,");
        } else {
            greetingTextView.setText("Good Night,");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(updateTimeRunnable); // Remove callbacks to avoid memory leaks
        }
    }
}
