package com.company.old_skool_db_project;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.company.old_skool_db_project.adapter.ContactsAdapter;
import com.company.old_skool_db_project.adapter.ContactsAdapter.AlterContacts;
import com.company.old_skool_db_project.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.AlterContacts {

    //NO ROOM Database Project
    //Using SQLITE


    //variables
    private ContactsAdapter mContactsAdapter;
    private ArrayList<Contact> mContactArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private DataBaseHelper mDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Favorite Contacts");

        //RecyclerView
        mRecyclerView = findViewById(R.id.recyclerViewContacts);
        mDataBaseHelper = new DataBaseHelper(this);

        //Contacts list
        mContactArrayList.addAll(mDataBaseHelper.getAllContacts());
        mContactsAdapter = new ContactsAdapter(this, mContactArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mContactsAdapter);

        //fab
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContact(false, null, -1);
            }
        });

    }


    //implementation *.adapter.ContactsAdapter.AlterContacts;
    @Override
    public void addAndEditContact(final boolean isUpdated, final Contact contact, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.add_contact, null);

        AlertDialog.Builder alerDialog = new AlertDialog.Builder(MainActivity.this);
        alerDialog.setView(view);


        TextView contactTitle = view.findViewById(R.id.change_contact_title);
        final EditText newContact = view.findViewById(R.id.name_addCon);
        final EditText newEmail = view.findViewById(R.id.email_addCon);
        contactTitle.setText(!isUpdated ? R.string.add_contact : R.string.update_contact);

        if(isUpdated && contact != null){

        }

    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//        super.onPointerCaptureChanged(hasCapture);
//    }
}