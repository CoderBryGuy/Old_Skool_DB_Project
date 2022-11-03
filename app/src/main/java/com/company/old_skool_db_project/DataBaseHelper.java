package com.company.old_skool_db_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import androidx.annotation.Nullable;
import com.company.old_skool_db_project.db.entity.Contact;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contact_db";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DataBaseHelper: created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contact.CREATE_TABLE);
        Log.d(TAG, "onCreate: started");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: started");
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);
        onCreate(db);
    }

    //insert data into db
    public long insertContact(String name, String email) {
        Log.d(TAG, "insertContact: started");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, name);
        values.put(Contact.COLUMN_EMAIL, email);
        long id = db.insert(Contact.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    //getting contact from db
    public Contact getContact(long id) {
        Log.d(TAG, "getContact: started");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Contact.TABLE_NAME,
                new String[]{
                        Contact.COLUMN_ID,
                        Contact.COLUMN_NAME,
                        Contact.COLUMN_EMAIL},
                Contact.COLUMN_ID + "=?",
                new String[]{
                        String.valueOf(id)},
                null,
                null,
                null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Contact contact = new Contact(
                cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL))

        );

        cursor.close();
        return contact;
    }

    //get all contacts
    public ArrayList<Contact> getAllContacts() {
        Log.d(TAG, "getAllContacts: started");
        String selectQuery = "SELECT * FROM " + Contact.TABLE_NAME + " ORDER BY " + Contact.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<Contact> contactsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                       contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));
                       contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)));
                        contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL)));

                contactsList.add(contact);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return contactsList;
    }

    //update contact
    public long updateContact(Contact contact){
        Log.d(TAG, "updateContact: started");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_EMAIL, contact.getEmail());

        return db.update(Contact.TABLE_NAME, values, Contact.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});

    }

    //delete contact
    public void deleteContact(Contact contact){
        Log.d(TAG, "deleteContact: started");
        SQLiteDatabase db = this.getWritableDatabase();
        int id = db.delete(Contact.TABLE_NAME, Contact.COLUMN_ID + " = ? ", new String[]{String.valueOf(contact.getId())});
        Log.d(TAG, "deleteContact: deleted contact.id = " + id);
        db.close();
    }





}
