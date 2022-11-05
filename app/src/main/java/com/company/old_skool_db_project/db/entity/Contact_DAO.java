package com.company.old_skool_db_project.db.entity;

import androidx.room.*;

import java.util.List;

@Dao
public interface Contact_DAO {

    @Insert
    long addContact(Contact contact);

    @Update
    void updateContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);

    @Query("select * from contacts")
    List<Contact> getContacts();

    @Query("select * from contacts where contact_id ==:contactId")
    Contact getContact(long contactId);
}
