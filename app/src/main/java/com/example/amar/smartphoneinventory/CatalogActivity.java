package com.example.amar.smartphoneinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.amar.smartphoneinventory.data.SmartphoneContract;

public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SMARTPHONE_LOADER = 0;

    // Adapter for the ListView
    SmartphoneCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the smartphone data
        ListView smartphoneListView = findViewById(R.id.smartphone_list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        smartphoneListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of smartphone data in the Cursor.
        // There is no smartphone data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new SmartphoneCursorAdapter(this, null);
        smartphoneListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        smartphoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, SmartphoneDetailsActivity.class);

                // Form the content URI that represents the specific smartphone that was clicked on
                Uri currentSmartphoneUri = ContentUris.withAppendedId(SmartphoneContract.SmartphoneEntry.CONTENT_URI, id);
                intent.setData(currentSmartphoneUri);
                // Launch smartphone details activity
                startActivity(intent);
                Log.v("CatalogActivity", "item click");
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(SMARTPHONE_LOADER, null, this);
    }

    //Helper method to insert hardcoded smartphone data into the database. For debugging purposes only.
    private void insertSmartphone() {
        // dummy data
        ContentValues values = new ContentValues();
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME, "Redmi Note 5 pro");
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE, 14999);
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY, 1);
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME, "Redmi Hub");
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE, "9417476801");

        // Insert a new row for data into the provider using the ContentResolver.
        Uri newUri = getContentResolver().insert(SmartphoneContract.SmartphoneEntry.CONTENT_URI, values);
    }

    private void deleteAllSmartphones() {
        int rowsDeleted = getContentResolver().delete(SmartphoneContract.SmartphoneEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from smartphone database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // This inflates menu items on the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        Log.v("CatalogActivity", "menu inflated ");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertSmartphone();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllSmartphones();
                Log.v("CatalogActivity", "option item selected ");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                SmartphoneContract.SmartphoneEntry._ID,
                SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME,
                SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY,
                SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE,};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                SmartphoneContract.SmartphoneEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update cursor adapter with this new cursor containing updated smartphone data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}




