package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SelectBusActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
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

        // Initialize the MapView
        mapView = findViewById(R.id.map_view);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this); // Set up the callback for when the map is ready

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

        busNumberTextView.setOnClickListener(view -> {
            if (busNumber.isEmpty()) {
                Toast.makeText(SelectBusActivity.this, "Please select a bus", Toast.LENGTH_SHORT).show();
            } else {
                // Retrieve the bus details from the database
                Bus bus = databaseHelper.getBusDetails(busNumber);

                if (bus != null) {
                    // Pass the bus details to the next activity
                    Intent seatBookIntent = new Intent(SelectBusActivity.this, SeatBookActivity.class);
                    seatBookIntent.putExtra("BUS_ID", bus.getBusId());
                    seatBookIntent.putExtra("BUS_NUMBER", bus.getBusNumber());
                    seatBookIntent.putExtra("START_LOCATION", bus.getStartLocation());
                    seatBookIntent.putExtra("END_LOCATION", bus.getEndLocation());
                    seatBookIntent.putExtra("ROUTE", bus.getRoute());
                    seatBookIntent.putExtra("DRIVER", bus.getDriver());
                    seatBookIntent.putExtra("SEATS", bus.getSeats());
                    startActivity(seatBookIntent);
                } else {
                    Toast.makeText(SelectBusActivity.this, "Bus details not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set map type and enable UI features
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Add a marker at a specific location
        LatLng defaultLocation = new LatLng(-33.852, 151.211); // Sydney, Australia
        googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
}
