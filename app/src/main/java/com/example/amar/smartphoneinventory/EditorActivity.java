package com.example.amar.smartphoneinventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amar.smartphoneinventory.data.SmartphoneContract;

// Allows user to create a new smartphone or edit an existing one
public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    //Identifier for the smartphone data loader
    private static final int EXISTING_SMARTPHONE_LOADER = 0;
    //string variables for the data values
    public static String nameString;
    public static String priceString;
    public static String quantityString;
    public static String supplierNameString;
    public static String supplierPhoneString;
    //EditText field to enter the smartphone's name
    private EditText mNameEditText;
    //EditText field to enter the smartphone's price
    private EditText mPriceEditText;
    //EditText field to enter the smartphone's quantity
    private EditText mQuantityEditText;
    //EditText field to enter the supplier's name
    private EditText mSupplierNameEditText;
    // EditText field to enter the supplier's phone
    private EditText mSupplierPhoneEditText;
    //ontent URI for the existing smartphone (null if it's a new smartphone)
    private Uri mCurrentSmartphoneUri;
    //Boolean flag that keeps track of whether the smartphone has been edited (true) or not (false)
    private boolean mSmartphoneHasChanged = false;

    //OnTouchListener that listens for any user touches on a View,
    // implying that they are modifying the view, and we change the mSmartphoneHasChanged boolean to true.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mSmartphoneHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity,
        Intent intent = getIntent();
        mCurrentSmartphoneUri = intent.getData();

        // If the intent DOES NOT contain a smartphone content URI, then we know that we are creating a new smartphone.
        if (mCurrentSmartphoneUri == null) {
            // This is a new smartphone, so change the app bar to say "Add a Smartphone"
            setTitle(getString(R.string.editor_activity_title_new_smartphone));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing smartphone, so change app bar to say "Edit Smartphone"
            setTitle(getString(R.string.editor_activity_title_edit_smartphone));

            // Initialize a loader to read the smartphone data from the database
            getLoaderManager().initLoader(EXISTING_SMARTPHONE_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_smartphone_name);
        mPriceEditText = findViewById(R.id.edit_smartphone_price);
        mQuantityEditText = findViewById(R.id.edit_smartphone_quantity);
        mSupplierNameEditText = findViewById(R.id.edit_smartphone_supplier_name);
        mSupplierPhoneEditText = findViewById(R.id.edit_smartphone_supplier_phone);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);
    }

    //get user input from editor and save new smartphone into database.
    private void saveSmartphone() {
        // Create a ContentValues object where column names are the keys,
        // and smartphone attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME, nameString);
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE, priceString);
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY, quantityString);
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME, supplierNameString);
        values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE, supplierPhoneString);
        // Determine if this is a new or existing smartphone by checking if mCurrentSmartUri is null or not
        if (mCurrentSmartphoneUri == null) {
            // This is a NEW smartphone, so insert a new smartphone into the provider,
            // returning the content URI for the new smartphone.
            Uri newUri = getContentResolver().insert(SmartphoneContract.SmartphoneEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_smartphone_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_smartphone_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING smartphone, so update the smartphone with content URI.
            int rowsAffected = getContentResolver().update(mCurrentSmartphoneUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_smartphone_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(EditorActivity.this, CatalogActivity.class);

                i.setData(mCurrentSmartphoneUri);

                startActivity(i);
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_smartphone_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    //This method is called after invalidateOptionsMenu(), so that the
    // menu can be updated (some menu items can be hidden or made visible).
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new smartphone, hide the "Delete" menu item.
        if (mCurrentSmartphoneUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                nameString = mNameEditText.getText().toString().trim();
                priceString = mPriceEditText.getText().toString().trim();
                quantityString = mQuantityEditText.getText().toString().trim();
                supplierNameString = mSupplierNameEditText.getText().toString().trim();
                supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(nameString) && !TextUtils.isEmpty(priceString) &&
                        !TextUtils.isEmpty(quantityString) && !TextUtils.isEmpty(supplierNameString) &&
                        !TextUtils.isEmpty(supplierPhoneString)) {

                    saveSmartphone();
                    finish();
                    return true;

                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_smartphone_failed),
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                if (!mSmartphoneHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //This method is called when the back button is pressed.
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mSmartphoneHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all smartphone attributes, define a projection that contains
        // all columns from the smartphone table
        String[] projection = {
                SmartphoneContract.SmartphoneEntry._ID,
                SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME,
                SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE,
                SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY,
                SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME,
                SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentSmartphoneUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of smartphone attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME);
            int priceColumnIndex = cursor.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneEditText.setText(supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
    }

    //Show a dialog that warns the user there are unsaved changes that will be lost
    // if they continue leaving the editor.

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the smartphone.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Prompt the user to confirm that they want to delete this smartphone.
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the smartphone.
                deleteSmartphone();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the smartphone.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteSmartphone() {
        // Only perform the delete if this is an existing smartphone.
        if (mCurrentSmartphoneUri != null) {
            // Call the ContentResolver to delete the smartphone at the given content URI.
            int rowsDeleted = getContentResolver().delete(mCurrentSmartphoneUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_smartphone_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_smartphone_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}