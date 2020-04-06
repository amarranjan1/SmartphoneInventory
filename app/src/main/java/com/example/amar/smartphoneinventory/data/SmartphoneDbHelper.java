package com.example.amar.smartphoneinventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Database helper for Smartphone app. Manages database creation and version management.
public class SmartphoneDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";

    //Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public SmartphoneDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //This is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_SMARTPHONE_TABLE = "CREATE TABLE " + SmartphoneContract.SmartphoneEntry.TABLE_NAME + " ("
                + SmartphoneContract.SmartphoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME + " TEXT NOT NULL, "
                + SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE + " INTEGER NOT NULL, "
                + SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY + " INTEGER NOT NULL, "
                + SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME + " TEXT,"
                + SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_SMARTPHONE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}


