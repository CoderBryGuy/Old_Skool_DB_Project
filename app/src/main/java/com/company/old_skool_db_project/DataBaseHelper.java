package com.company.old_skool_db_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import androidx.annotation.Nullable;
import com.company.old_skool_db_project.db.entity.Contact;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contact_db";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contact.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);
        onCreate(db);
    }

    //insert data into db
    public long insertContact(String name, String email) {
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
                cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)),
                cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID))
        );

        cursor.close();
        return contact;
    }

    //get all contacts
    public ArrayList<Contact> getAllContacts() {

        String selectQuery = "SELECT * FROM " + Contact.TABLE_NAME + " ORDER BY " + Contact.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
//        Cursor cursor = db.query(Contact.TABLE_NAME,
//                new String[]{
//                        Contact.COLUMN_ID,
//                        Contact.COLUMN_NAME,
//                        Contact.COLUMN_EMAIL},
//                Contact.COLUMN_ID + "=?",
//                null,
//                null,
//                null,
//                null,
//                null);

//        if(cursor != null){
//            cursor.moveToFirst();
//        }

        ArrayList<Contact> contactsList = new ArrayList<>();
        if (cursor.moveToNext()) {
            do {
                Contact contact = new Contact();
                       contact.setId(cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID)));
                       contact.setName(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)));
                        contact.setEmail(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_ID)));

                contactsList.add(contact);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return contactsList;
    }

    //update contact
    public long updateContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_EMAIL, contact.getEmail());

        return db.update((Contact.TABLE_NAME, values, Contact.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});

    }

}
