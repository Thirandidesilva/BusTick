package com.example.proj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "busapp.db";
    private static final int DATABASE_VERSION = 1;

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
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Fixed missing space before INTEGER
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_MOBILE + " TEXT, " +
                    COLUMN_ROLE + " TEXT" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        Log.d("SQLite", "Database and users table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
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
                        "Username=" + username + ", Email=" + email);
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
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

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

        if (cursor.getCount() > 0) {
            Log.d("SQLite", "Users with role " + role + ":");
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                Log.d("SQLite", " - " + username);
            }
        } else {
            Log.d("SQLite", "No users found with role: " + role);
        }

        return cursor;
    }
}
