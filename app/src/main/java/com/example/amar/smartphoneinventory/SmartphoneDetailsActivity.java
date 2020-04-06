package com.example.amar.smartphoneinventory;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amar.smartphoneinventory.data.SmartphoneContract;

public class SmartphoneDetailsActivity extends AppCompatActivity {

    public String supplierPhone;
    Integer quantity;
    TextView nameTextView;
    TextView priceTextView;
    TextView quantityTextView;
    TextView supplierNameTextView;
    TextView supplierPhoneTextView;
    Uri mCurrentUri;
    Uri mNewUri;
    Button editButton;
    Button minusButton;
    Button plusButton;
    Button deleteButton;
    Button contactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartphone_details);

        //get the intent to open details page
        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        Intent i = getIntent();
        mNewUri = i.getData();
        nameTextView = findViewById(R.id.smartphone_name);
        priceTextView = findViewById(R.id.smartphone_price);
        quantityTextView = findViewById(R.id.smartphone_quantity);
        supplierNameTextView = findViewById(R.id.smartphone_supplier_name);
        supplierPhoneTextView = findViewById(R.id.smartphone_supplier_phone);
        editButton = findViewById(R.id.edit_button);
        minusButton = findViewById(R.id.minus_button);
        plusButton = findViewById(R.id.plus_button);
        deleteButton = findViewById(R.id.delete_button);
        contactButton = findViewById(R.id.contact_supplier_button);

        Cursor c = managedQuery(mCurrentUri, null, null, null, "name");

        if (c.moveToFirst()) {
            do {
                String smartphoneName = c.getString(c.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME));
                String smartphonePrice = c.getString(c.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE));
                String smartphoneQuantity = c.getString(c.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY));
                String supplierName = c.getString(c.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME));
                supplierPhone = c.getString(c.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE));
                quantity = c.getInt(c.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY));

                nameTextView.setText(" " + smartphoneName);
                priceTextView.setText(" " + smartphonePrice);
                quantityTextView.setText(" " + smartphoneQuantity.toString());
                supplierNameTextView.setText(" " + supplierName);
                supplierPhoneTextView.setText(" " + supplierPhone);

            } while (c.moveToNext());
        }

        Cursor cNew = managedQuery(mNewUri, null, null, null, "name");

        if (cNew.moveToFirst()) {
            do {
                String smartphoneName = cNew.getString(cNew.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME));
                String smartphonePrice = cNew.getString(cNew.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE));
                String smartphoneQuantity = cNew.getString(cNew.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY));
                String supplierName = cNew.getString(cNew.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_NAME));
                String supplierPhone = cNew.getString(cNew.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_SUPPLIER_PHONE));
                quantity = cNew.getInt(cNew.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY));

                nameTextView.setText(" " + smartphoneName);
                priceTextView.setText(" " + smartphonePrice);
                quantityTextView.setText(" " + smartphoneQuantity.toString());
                supplierNameTextView.setText(" " + supplierName);
                supplierPhoneTextView.setText(" " + supplierPhone);

            } while (cNew.moveToNext());
        }

        editButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(SmartphoneDetailsActivity.this, EditorActivity.class);

                intent.setData(mCurrentUri);
                startActivity(intent);
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (quantity > 0) {

                    quantity = quantity - 1;

                    ContentValues values = new ContentValues();
                    values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY, quantity);
                    getContentResolver().update(mCurrentUri, values, null, null);

                    quantityTextView.setText(quantity.toString());
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantity = quantity + 1;

                ContentValues values = new ContentValues();
                values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY, quantity);
                getContentResolver().update(mCurrentUri, values, null, null);
                quantityTextView.setText(quantity.toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDeleteConfirmationDialog();
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + supplierPhone));
                startActivity(intent);
            }
        });
    }

    //opens up when delete button clicked
    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deleteSmartphone();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteSmartphone() {
        if (mCurrentUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowsDeleted == 0) {

                Toast.makeText(this, getString(R.string.editor_delete_smartphone_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_smartphone_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}
