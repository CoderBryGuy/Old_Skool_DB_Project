package com.company.old_skool_db_project.db.entity;

import android.util.Log;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "contacts")
public class Contact {

    private static final String TAG = "Contact";

    // constants for database
//    public static final String TABLE_NAME = "contacts";
//    public static final String COLUMN_ID = "contact_id";
//    public static final String COLUMN_EMAIL = "contact_email";
//    public static final String COLUMN_NAME = "contact_name";

    //variables
    @ColumnInfo(name = "contact_name")
    private String name;

    @ColumnInfo(name = "contact_email")
    private String email;

    @ColumnInfo(name = "contact_id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    //constructors
    @Ignore
    public Contact() {
        Log.d(TAG, "Contact: created");
    }

    public Contact( int id, String name, String email) {
        Log.d(TAG, "Contact: created");
        this.id = id;
        this.name = name;
        this.email = email;

    }

    //Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //SQL Query  : Creating the table
//    public static final String CREATE_TABLE =
//            "CREATE TABLE " + TABLE_NAME + "("
//            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//            + COLUMN_NAME + " TEXT ,"
//            + COLUMN_EMAIL + " DATETIME DEFAULT CURRENT_TIMESTAMP"
//            + ")";
}
