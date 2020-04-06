package com.example.amar.smartphoneinventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class SmartphoneContract {

    public static final String CONTENT_AUTHORITY = "com.example.amar.smartphoneinventory";

    // A convenient string to use for the content authority is the package name for the app, which is unique on the device
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SMARTPHONE = "smartphone";

    // To prevent someone from accidentally instantiating the contract class empty constructor must be private
    private SmartphoneContract() {
    }

    //Inner class that defines constant values for the Smartphone database table.
    public static final class SmartphoneEntry implements BaseColumns {

        //The content URI to access the smartphone data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SMARTPHONE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SMARTPHONE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SMARTPHONE;

        public final static String TABLE_NAME = "smartphone";

        //Unique ID number for the smartphone (only for use in the database table). Type: integer
        public final static String _ID = BaseColumns._ID;

        //Name of the smartphone. Type: TEXT
        public final static String COLUMN_SMARTPHONE_NAME = "name";

        // Price of the SMARTPHONE. Type: integer
        public final static String COLUMN_SMARTPHONE_PRICE = "price";

        //Quantity of the smartphone. Type: integer
        public final static String COLUMN_SMARTPHONE_QUANTITY = "quantity";

        //Supplier name of the smartphone. Type: TEXT
        public final static String COLUMN_SMARTPHONE_SUPPLIER_NAME = "supplierName";

        //Supplier`s phone number. Type: TEXT
        public final static String COLUMN_SMARTPHONE_SUPPLIER_PHONE = "supplierPhone";
    }
}






