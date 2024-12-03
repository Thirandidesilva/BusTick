package com.example.proj;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        databaseHelper = new DatabaseHelper(this);

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle login
                loginUser();
            }
        });

        TextView textView7 = findViewById(R.id.textView7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String username = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(username)) {
            etEmail.setError("Email cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password cannot be empty");
            return;
        }

        // Authenticate user
        Cursor cursor = databaseHelper.authenticateUser(username, password);
        if (cursor != null && cursor.moveToFirst()) {
            // Extract user role
            String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE));

            // Navigate to appropriate activity based on role
            switch (role) {
                case "Passenger":
                    Intent passengerIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(passengerIntent);
                    break;

                case "Bus Owner":
                    Intent ownerIntent = new Intent(LoginActivity.this, OwnerHomeActivity.class);
                    startActivity(ownerIntent);
                    break;

                case "Driver":
                    Intent driverIntent = new Intent(LoginActivity.this, DriverHomeActivity.class);
                    startActivity(driverIntent);
                    break;

                default:
                    Toast.makeText(LoginActivity.this, "Invalid role. Please contact support.", Toast.LENGTH_SHORT).show();
                    break;
            }

            // Close the cursor after use
            cursor.close();
        } else {
            Toast.makeText(LoginActivity.this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
        }
    }
}
