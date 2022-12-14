package com.company.old_skool_db_project;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.company.old_skool_db_project.adapter.ContactsAdapter;
import com.company.old_skool_db_project.db.entity.Contact;
import com.company.old_skool_db_project.db.entity.ContactsAppDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.AlterContacts {

    private static final String TAG = "MainActivity";
    //NO ROOM Database Project
    //Using SQLITE


    //variables
    private ContactsAdapter mContactsAdapter;
    private final ArrayList<Contact> mContactArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    //    private DataBaseHelper_OldSkool mDataBaseHelper;
    private ContactsAppDatabase mContactsAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Favorite Contacts");

        //RecyclerView
        mRecyclerView = findViewById(R.id.recyclerViewContacts);
//        mDataBaseHelper = new DataBaseHelper_OldSkool(this);

        //CallBacks
//        RoomDatabase.Callback myCallBack = new RoomDatabase.Callback() {
//            @Override
//            public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
//                super.onCreate(db);
//
//                //4 contacts already built into contacts
//                createContact("Bill Gates", "gman@microsoft.com");
//                createContact("Nicolas Tesla", "tesla@gmail.com");
//                createContact("Mark Zuckerberg", "zuck@facebook.com");
//                createContact("Elon Musk", "Musk@tesla.com");
//
//                Log.i(TAG, "onOpen: database created");
//
//            }
//
//            @Override
//            public void onOpen(@NonNull @NotNull SupportSQLiteDatabase db) {
//                super.onOpen(db);
//
//                Log.i(TAG, "onOpen: database opened");
//            }
//        };

        //Database
        mContactsAppDatabase = Room.databaseBuilder(getApplicationContext(),
                        ContactsAppDatabase.class, "ContactDB")
//                .addCallback(myCallBack)
                //thread run in background now
//                .allowMainThreadQueries()
                .build();

        //display all contacts list
        //replaced with to run on background thread
//        mContactArrayList.addAll(mContactsAppDatabase.getContactDAO().getContacts());

        displayAllContactsInBackground();

        mContactsAdapter = new ContactsAdapter(this, mContactArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mContactsAdapter);

        //fab
        FloatingActionButton mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: fab");
                addAndEditContact(false, null, -1);
            }
        });

    }


    //implementation *.adapter.ContactsAdapter.AlterContacts;
    @Override
    public void addAndEditContact(final boolean isUpdating, final Contact contact, final int position) {
        Log.d(TAG, "addAndEditContact: started");
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.add_contact, null);

        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alerDialogBuilder.setView(view);


        TextView contactTitle = view.findViewById(R.id.change_contact_title);
        final EditText contactName = view.findViewById(R.id.name_add_contact);
        final EditText contactEmail = view.findViewById(R.id.email_add_contact);
        contactTitle.setText(!isUpdating ? R.string.add_contact : R.string.update_contact);

        if (isUpdating && contact != null) {
            Log.d(TAG, "addAndEditContact: isupdated = true; editing contact");

            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());

        }

        alerDialogBuilder.setCancelable(false).setPositiveButton(isUpdating ? "update" : "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: alerdialogBuilder pos");
            }
        }).setNegativeButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: alerdialogBuilder neg");
                if (isUpdating) {
                    DeleteContact(contact, position);
                } else {
                    dialogInterface.cancel();
                }
            }
        });

        //.show();

        final AlertDialog alertDialog = alerDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: AlertDialog.BUTTON_POSITIVE");
                if (TextUtils.isEmpty(contactName.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter a name.", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                }

                if (isUpdating && contact != null) {
                    UpdateContact(contactName.getText().toString(), contactEmail.getText().toString(), position);

                } else {
                    createContact(contactName.getText().toString(), contactEmail.getText().toString());
                }
            }

        });
    }

    private void createContact(String contactName, String contactEmail) {
        Log.d(TAG, "createContact: started");

        //replaced with room addcontact()
//        long id = mDataBaseHelper.insertContact(contactName, contactEmail);
        //        Contact contact = mDataBaseHelper.getContact(id);


        long id = mContactsAppDatabase.getContactDAO().addContact(new Contact(0, contactName, contactEmail));
        Contact contact = mContactsAppDatabase.getContactDAO().getContact(id);
        if (contact != null) {
            mContactArrayList.add(0, contact);
            mContactsAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "createContact: contact created id = " + contact.getId() + " name = " + contact.getName() + " email = " + contact.getEmail());

    }

    private void UpdateContact(String contactName, String contactEmail, int position) {
        Log.d(TAG, "UpdateContact: started");
        Contact contact = mContactArrayList.get(position);
        contact.setName(contactName);
        contact.setEmail(contactEmail);
        //replaced with room update contact
//        mDataBaseHelper.updateContact(contact);
        mContactsAppDatabase.getContactDAO().updateContact(contact);
        mContactArrayList.set(position, contact);
        mContactsAdapter.notifyDataSetChanged();
        Log.d(TAG, "UpdateContact: contact updated id = " + contact.getId() + " name = " + contact.getName());

    }

    private void DeleteContact(Contact contact, int position) {
        Log.d(TAG, "DeleteContact: started");
        Contact contact1 = mContactArrayList.remove(position);
        //replaced with room deletecontact()
//        mDataBaseHelper.deleteContact(contact);
        mContactsAppDatabase.getContactDAO().deleteContact(contact);
        mContactsAdapter.notifyDataSetChanged();
        if (contact1 != null) {
            Log.d(TAG, "DeleteContact: contact deleted = " + contact1.getName());
        }

    }

    private void displayAllContactsInBackground() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        Log.d(TAG, "displayAllContactsInBackground: started");
        executor.execute(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "run: background thread running");

                //background work
                mContactArrayList.addAll(mContactsAppDatabase.getContactDAO().getContacts());

                //run after background work
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: background thread finished. Running handler.post new Runnable");
                        mContactsAdapter.notifyDataSetChanged();

                    }
                });
            }
        });
    }

    //Menu bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}