package com.example.proj;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BusDriverRegister extends AppCompatActivity {

    private ImageView imageView;
    private FloatingActionButton button;
    private EditText driverUsername, driverEmail, driverMobile, driverPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bus_driver_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.image_view2);
        button =findViewById(R.id.floatingActionButton4);
        driverUsername = findViewById(R.id.editTextText2);
        driverEmail = findViewById(R.id.editTextTextEmailAddress);
        driverMobile = findViewById(R.id.editTextPhone2);
        driverPassword = findViewById(R.id.editTextTextPassword);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(BusDriverRegister.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });

        Button button1 = findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validate input fields
                String username = driverUsername.getText().toString().trim();
                String email = driverEmail.getText().toString().trim();
                String mobile = driverMobile.getText().toString().trim();
                String password = driverPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    driverUsername.setError("Username cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    driverEmail.setError("Email cannot be empty");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    driverEmail.setError("Invalid email format");
                    return;
                }
                if (TextUtils.isEmpty(mobile)) {
                    driverMobile.setError("Mobile number cannot be empty");
                    return;
                }
                if (mobile.length() != 10 || !mobile.matches("\\d+")) {
                    driverMobile.setError("Enter a valid 10-digit mobile number");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    driverPassword.setError("Password cannot be empty");
                    return;
                }
                if (password.length() < 6) {
                    driverPassword.setError("Password must be at least 6 characters");
                    return;
                }

                // If all validations pass, show a success message
                Toast.makeText(BusDriverRegister.this, "Registration Successful!", Toast.LENGTH_SHORT).show();



                Intent intent = new Intent(BusDriverRegister.this,OwnerLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        imageView.setImageURI(uri);
    }

}