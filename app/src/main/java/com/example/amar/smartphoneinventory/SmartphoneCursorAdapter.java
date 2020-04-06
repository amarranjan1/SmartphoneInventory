package com.example.amar.smartphoneinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.amar.smartphoneinventory.data.SmartphoneContract;

public class SmartphoneCursorAdapter extends CursorAdapter {

    public SmartphoneCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    // Makes a new blank list item view. No data is set (or bound) to the views yet.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    //This method binds the smartphone data (in the current row pointed to by cursor) to the given list item layout.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = view.findViewById(R.id.name);
        final TextView quantityTextView = view.findViewById(R.id.quantity);
        TextView priceTextView = view.findViewById(R.id.price);
        Button saleButton = view.findViewById(R.id.sale);

        // Find the columns of smartphone attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY);
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY));
        int priceColumnIndex = cursor.getColumnIndex(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_PRICE);

        // Read the smartphone attributes from the Cursor for the current smartphone.
        final String smartphoneName = cursor.getString(nameColumnIndex);
        final String smartphoneQuantity = cursor.getString(quantityColumnIndex);
        final String smartphonePrice = cursor.getString(priceColumnIndex);

        final Uri uri = ContentUris.withAppendedId(SmartphoneContract.SmartphoneEntry.CONTENT_URI,
                cursor.getInt(cursor.getColumnIndexOrThrow(SmartphoneContract.SmartphoneEntry._ID)));

        //set data to views
        nameTextView.setText(smartphoneName);
        quantityTextView.setText(" " + smartphoneQuantity);
        priceTextView.setText(" " + smartphonePrice);

        //sale button on click listener
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quantity > 0) {
                    Integer afterSale = quantity - 1;

                    ContentValues values = new ContentValues();
                    values.put(SmartphoneContract.SmartphoneEntry.COLUMN_SMARTPHONE_QUANTITY, afterSale);
                    context.getContentResolver().update(uri, values, null, null);

                    quantityTextView.setText(afterSale.toString());
                }
            }
        });
    }
}










