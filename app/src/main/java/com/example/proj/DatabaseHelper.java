package com.example.proj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "busapp.db";
    private static final int DATABASE_VERSION = 2;

    // Table name and columns
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_ROLE = "role";

    // Create table SQL
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_MOBILE + " TEXT NOT NULL, " +
                    COLUMN_ROLE + " TEXT NOT NULL" +
                    ")";

    // Add a new table for buses
    public static final String TABLE_BUSES = "buses";
    public static final String COLUMN_BUS_ID = "bus_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_BUS_NUMBER = "bus_number";
    public static final String COLUMN_START_LOCATION = "start_location";
    public static final String COLUMN_END_LOCATION = "end_location";
    public static final String COLUMN_ROUTE = "route";
    public static final String COLUMN_DRIVER = "driver";
    public static final String COLUMN_SEATS = "seats";

    private static final String CREATE_TABLE_BUSES =
            "CREATE TABLE " + TABLE_BUSES + " (" +
                    COLUMN_BUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BUS_NUMBER + " TEXT NOT NULL, " +
                    COLUMN_START_LOCATION + " TEXT NOT NULL, " +
                    COLUMN_END_LOCATION + " TEXT NOT NULL, " +
                    COLUMN_ROUTE + " TEXT NOT NULL, " +
                    COLUMN_DRIVER + " TEXT NOT NULL, " +
                    COLUMN_SEATS + " INTEGER NOT NULL" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        Log.d("SQLite", "Table " + TABLE_USERS + " created successfully.");
        db.execSQL(CREATE_TABLE_BUSES);
        Log.d("SQLite", "Table " + TABLE_BUSES + " created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("SQLite", "Upgrading database from version " + oldVersion + " to " + newVersion);

        // Drop existing tables and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSES);
        onCreate(db);
        Log.d("SQLite", "Database upgraded to version " + newVersion);
    }

    // Insert a new user
    public long insertUser(String username, String email, String password, String mobile, String role) {
        SQLiteDatabase db = null;
        long result = -1;

        try {
            // Open the database in write mode
            db = this.getWritableDatabase();

            // Create content values for the user
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_MOBILE, mobile);
            values.put(COLUMN_ROLE, role);

            // Insert the values into the table
            result = db.insert(TABLE_USERS, null, values);

            if (result != -1) {
                Log.d("SQLite", "User inserted successfully: " +
                        "Username=" + username + ", Email=" + email + ", Password" + password);
            } else {
                Log.e("SQLite", "Failed to insert user");
            }
        } catch (Exception e) {
            Log.e("SQLite", "Error during user insertion", e);
        } finally {
            if (db != null) {
                db.close(); // Close the database connection
            }
        }

        return result; // Returns the row ID or -1 if an error occurred
    }

    // Authenticate user
    public Cursor authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("SQLite", "Authenticating user with Email: " + email + ", Password: " + password);
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        Log.d("SQLite", "Query returned " + cursor.getCount() + " rows.");

        if (cursor.getCount() > 0) {
            Log.d("SQLite", "Authentication successful for user: " + email);
        } else {
            Log.d("SQLite", "Authentication failed for user: " + email);
        }

        return cursor;
    }

    // Retrieve users by role
    public Cursor getUsersByRole(String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ROLE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{role});

        if (cursor != null && cursor.getCount() > 0) {
            Log.d("SQLite", "Users with role " + role + ":");
            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);

            if (usernameIndex != -1) { // Ensure the column exists
                while (cursor.moveToNext()) {
                    String username = cursor.getString(usernameIndex);
                    Log.d("SQLite", " - " + username);
                }
            } else {
                Log.e("SQLite", "COLUMN_USERNAME does not exist in the result set.");
            }
        } else {
            Log.d("SQLite", "No users found with role: " + role);
        }

        return cursor;
    }

    public long insertBus(String busNumber, String startLocation, String endLocation, String route, String driver, int seats) {
        SQLiteDatabase db = null;
        long result = -1;

        try{
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_BUS_NUMBER, busNumber);
            values.put(COLUMN_START_LOCATION, startLocation);
            values.put(COLUMN_END_LOCATION, endLocation);
            values.put(COLUMN_ROUTE, route);
            values.put(COLUMN_DRIVER, driver);
            values.put(COLUMN_SEATS, seats);

            result = db.insert(TABLE_BUSES, null, values);

            if (result != -1) {
                Log.d("SQLite", "Bus inserted successfully: " +
                        "Bus Number=" + busNumber + ", Start Location=" + startLocation + ", End Location" + endLocation);
            } else {
                Log.e("SQLite", "Failed to insert bus");
            }
        } catch (Exception e){
            Log.e("SQLite", "Error during bus insertion", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return result;
    }

    public List<String> getAllBuses() {
        List<String> busList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Correct SQL query with proper spaces between keywords and table/column names
        String query = "SELECT " + COLUMN_BUS_NUMBER + " FROM " + TABLE_BUSES;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Fetch the bus number from the cursor
                String busNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUS_NUMBER));
                busList.add(busNumber);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return busList;
    }

    // Method to get details of a bus by its bus number
    public Bus getBusDetails(String busNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Bus bus = null;

        // Query to fetch the bus details
        String query = "SELECT " +
                COLUMN_BUS_NUMBER + ", " +
                COLUMN_START_LOCATION + ", " +
                COLUMN_END_LOCATION + ", " +
                COLUMN_ROUTE + ", " +
                COLUMN_DRIVER + ", " +
                COLUMN_SEATS +
                " FROM " + TABLE_BUSES +
                " WHERE " + COLUMN_BUS_NUMBER + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{busNumber});

        if (cursor != null && cursor.moveToFirst()) {
            // Extract bus details from cursor
            String startLocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_LOCATION));
            String endLocation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_LOCATION));
            String route = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROUTE));
            String driver = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRIVER));
            int seats = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEATS));

            bus = new Bus(busNumber, startLocation, endLocation, route, driver, seats);
        }

        cursor.close();
        db.close();

        return bus;
    }
}