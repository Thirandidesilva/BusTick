package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DriverHomeActivity extends AppCompatActivity {

    private TextView dateTimeText;
    private Handler handler;
    private Runnable updateTimeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_home);

        // Adjust window insets
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
                startActivity(new Intent(this, DriverHomeActivity.class));
                return true;
            } else if (id == R.id.reserved) {
                startActivity(new Intent(this, DriverViewBusDetailsActivity.class));
                return true;
            } else if (id == R.id.message) {
                startActivity(new Intent(this, DriverMessageActivity.class));
                return true;
            } else if (id == R.id.profile) {
                startActivity(new Intent(this, DriverProfileActivity.class));
                return true;
            }
            return false;
        });

        // Initialize and set onClick listener for the button
        findViewById(R.id.view_details_btn).setOnClickListener(v -> {
            Intent intent = new Intent(DriverHomeActivity.this, DriverViewBusDetailsActivity.class);
            startActivity(intent);
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
