package com.example.amar.smartphoneinventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class SmartphoneProvider extends ContentProvider {

      //Tag for the log messages
    public static final String LOG_TAG = SmartphoneProvider.class.getSimpleName();

    //URI matcher code for the content URI for the smartphone table
    private static final int SMARTPHONES = 100;

    //URI matcher code for the content URI for a single smartphone in the smartphones table
    private static final int SMARTPHONE_ID = 101;

    //UriMatcher object to match a content URI to a corresponding code. It's common to use NO_MATCH as the input for this case.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

// Static initializer. This is run the first time anything is called from this class.
static {
        sUriMatcher.addURI(SmartphoneContract.CONTENT_AUTHORITY, SmartphoneContract.PATH_SMARTPHONE, SMARTPHONES);

        // This URI is used to provide access to ONE single row of the smartphone table.
        sUriMatcher.addURI(SmartphoneContract.CONTENT_AUTHORITY, SmartphoneContract.PATH_SMARTPHONE + "/#", SMARTPHONE_ID);
        }

//Database helper object
private SmartphoneDbHelper mDbHelper;

@Override
public boolean onCreate() {
        mDbHelper = new SmartphoneDbHelper(getContext());
        return true;
        }

@Override
public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
        case SMARTPHONES:
        // For the SMARTPHONESS code, query the smartphone table directly with the given projection, selection, selection arguments, and sort order.
        cursor = database.query(SmartphoneContract.SmartphoneEntry.TABLE_NAME, projection, selection, selectionArgs,
        null, null, sortOrder);
        break;
        case SMARTPHONE_ID:
        // For the SMARTPHONE_ID code, extract out the ID from the URI.
        selection = SmartphoneContract.SmartphoneEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

        cursor = database.query(SmartphoneContract.SmartphoneEntry.TABLE_NAME, projection, selection, selectionArgs,
        null, null, sortOrder);
        break;
default:
        throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
        }

@Override
public Uri insert(Uri uri, ContentValues contentValues) {
final int match = sUriMatcher.match(uri);
        switch (match) {
        case SMARTPHONES:
        return insertSmartphone(uri, contentValues);
default:
        throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
        }

//Insert a smartphone into the database with the given content values.
private Uri insertSmartphone(Uri uri, ContentValues values) {

        // Check that the name is not null
        String name = values.getAsString(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME);
        Log.v(LOG_TAG, "Smartphone name required ");
        if (name == null) {
        throw new IllegalArgumentException("Smartphone requires a name");
        }

        Integer quantity = values.getAsInteger(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY);
        if (quantity == null || quantity < 0) {
        Log.v(LOG_TAG, "Smartphone name required ");
        throw new IllegalArgumentException("Smartphone requires valid quantity");
        }

        Integer price = values.getAsInteger(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE);
        if (price == null || price < 0) {
        throw new IllegalArgumentException("Smartphone requires valid price");
        }

        if (values.containsKey(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME)) {
        String supplierName = values.getAsString(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME);
        if (supplierName == null) {
        throw new IllegalArgumentException("Smaerphone requires a supplier name");
        }
        }

        if (values.containsKey(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE)) {
        String supplierPhone = values.getAsString(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE);
        if (supplierPhone == null) {
        throw new IllegalArgumentException("Smartphone requires valid supplier phone");
        }
        }
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new smartphone with the given values
        long id = database.insert(SmartphoneContract.SmartphoneEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
        Log.e(LOG_TAG, "Failed to insert row for " + uri);
        return null;
        }

        // Notify all listeners that the data has changed for the smartphone content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
        }

@Override
public int update(Uri uri, ContentValues contentValues, String selection,
        String[] selectionArgs) {
final int match = sUriMatcher.match(uri);
        switch (match) {
        case SMARTPHONES:
        return updateSmartphone(uri, contentValues, selection, selectionArgs);
        case SMARTPHONE_ID:
        selection = SmartphoneContract.SmartphoneEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return updateSmartphone(uri, contentValues, selection, selectionArgs);
default:
        throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        }

//Update smartphone in the database with the given content values.
private int updateSmartphone(Uri uri, ContentValues values, String selection, String[]
        selectionArgs) {

        // check that the name value is not null.
        if (values.containsKey(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME)) {
        String name = values.getAsString(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME);
        if (name == null) {
        throw new IllegalArgumentException("Smartphone requires a name");
        }
        }

        if (values.containsKey(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY)) {
        Integer quantity = values.getAsInteger(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY);
        if (quantity == null || quantity < 0) {
        throw new IllegalArgumentException("Smartphone requires valid quantity");
        }
        }

        if (values.containsKey(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE)) {
        Integer price = values.getAsInteger(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE);
        if (price == null || price < 0) {
        throw new IllegalArgumentException("Smartphone requires valid price");
        }
        }

        if (values.containsKey(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME)) {
        String supplierName = values.getAsString(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME);
        if (supplierName == null) {
        throw new IllegalArgumentException("Smartphone requires a supplier name");
        }
        }

        if (values.containsKey(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE)) {
        String supplierPhone = values.getAsString(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE);
        if (supplierPhone == null) {
        throw new IllegalArgumentException("Smartphone requires valid supplier phone");
        }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
        return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(SmartphoneContract.SmartphoneEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
        getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
        }

@Override
public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

final int match = sUriMatcher.match(uri);
        switch (match) {
        case SMARTPHONES:
        // Delete all rows that match the selection and selection args
        rowsDeleted = database.delete(SmartphoneContract.SmartphoneEntry.TABLE_NAME, selection, selectionArgs);
        break;
        case SMARTPHONE_ID:
        // Delete a single row given by the ID in the URI
        selection = SmartphoneContract.SmartphoneEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        rowsDeleted = database.delete(SmartphoneContract.SmartphoneEntry.TABLE_NAME, selection, selectionArgs);
        break;
default:
        throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
        getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
        }

@Override
public String getType(Uri uri) {
final int match = sUriMatcher.match(uri);
        switch (match) {
        case SMARTPHONES:
        return SmartphoneContract.SmartphoneEntry.CONTENT_LIST_TYPE;
        case SMARTPHONE_ID:
        return SmartphoneContract.SmartphoneEntry.CONTENT_ITEM_TYPE;
default:
        throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
        }
        }




















