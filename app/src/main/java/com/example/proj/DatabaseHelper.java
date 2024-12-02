package com.example.proj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Database name and version
    private static final String DATABASE_NAME = "busapp.db";
    private static final int DATABASE_VERSION = 1;

    //Table name and columns
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
                    COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USERNAME + " TEXT," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_MOBILE + " TEXT," +
                    COLUMN_ROLE + " TEXT" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
     onCreate(db);
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
        } catch (Exception e) {
            e.printStackTrace(); // Log any errors
        } finally {
            if (db != null) {
                db.close(); // Close the database connection
            }
        }

        return result; // Returns the row ID or -1 if an error occurred
    }

    // Authenticate user
    public Cursor authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        return db.rawQuery(query, new String[]{username, password});
    }

    // Retrieve user by role
    public Cursor getUsersByRole(String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ROLE + " = ?";
        return db.rawQuery(query, new String[]{role});
    }
}
