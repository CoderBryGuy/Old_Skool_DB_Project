package com.company.old_skool_db_project.db.entity;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactsAppDatabase extends RoomDatabase {
    //linking the DAO with our database
    public abstract Contact_DAO getContactDAO();
}
