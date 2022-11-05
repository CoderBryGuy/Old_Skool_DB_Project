package com.company.old_skool_db_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.company.old_skool_db_project.R;
import com.company.old_skool_db_project.db.entity.Contact;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    //1. variables

    private Context mContext;
    private ArrayList<Contact> mContacts;
//    private MainActivity mMainActivity;

    public interface AlterContacts{
        void addAndEditContact(boolean b,Contact contact, int position);
    }

    public AlterContacts mAlterContacts;


    //instead of calling directly mainActivity I implemented interface as is good practice
    public ContactsAdapter(Context context, ArrayList<Contact> contacts, AlterContacts alterContacts) {
        mContext = context;
        mContacts = contacts;
        mAlterContacts = alterContacts;
    }


//    public ContactsAdapter(Context context, ArrayList<Contact> contacts, MainActivity activity) {
//        mContext = context;
//        mContacts = contacts;
//        mMainActivity = activity;
//    }

    @NonNull
    @NotNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ContactsAdapter.MyViewHolder holder, int positions) {
        final Contact contact = mContacts.get(positions);
        holder.nameTxtVw.setText(contact.getName());
        holder.emailTxtVw.setText(contact.getEmail());


        final int position = positions;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mAlterContacts.addAndEditContact(true, contact, position);
            }
        });

    }


//    @Override
//    public void onBindViewHolder(@NonNull @NotNull ContactsAdapter.MyViewHolder holder, int position) {
//                    final Contact contact = mContacts.get(position);
//                    holder.nameTxtVw.setText(contact.getName());
//                    holder.emailTxtVw.setText(contact.getEmail());
//
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            mMainActivity.addAndEditContact(true, contact, position);
//                        }
//                    });
//
//    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameTxtVw, emailTxtVw;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nameTxtVw = itemView.findViewById(R.id.name_contact_list_item);
            emailTxtVw = itemView.findViewById(R.id.email_contact_list_item);

        }


//        public MyViewHolder(@NonNull @NotNull View itemView, TextView nameTxtVw, TextView emailTxtVw) {
//            super(itemView);
//            this.nameTxtVw = nameTxtVw;
//            this.emailTxtVw = emailTxtVw;
//        }
    }
}
