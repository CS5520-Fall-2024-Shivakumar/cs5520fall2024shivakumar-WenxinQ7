package com.example.numad24fa_wenxinqi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import java.util.List;
import android.app.AlertDialog;
import com.google.android.material.snackbar.Snackbar;
import android.widget.ImageButton;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private List<Contact> contacts;
    private Context context;

    public interface ContactActionListener {
        void onContactDeleted(Contact contact, int position);
        void onContactEdited(Contact contact, int position);
    }

    private ContactActionListener listener;

    public ContactsAdapter(List<Contact> contacts, ContactActionListener listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.nameText.setText(contact.getName());
        holder.phoneText.setText(contact.getPhoneNumber());

        holder.itemView.setOnClickListener(v -> {
            String phoneNumber = contact.getPhoneNumber();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            if (ActivityCompat.checkSelfPermission(context, 
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intent);
            }
        });

        holder.editButton.setOnClickListener(v -> {
            showEditDialog(contact, holder.getAdapterPosition());
        });

        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(contact, holder.getAdapterPosition());
        });
    }

    private void showEditDialog(Contact contact, int position) {
        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_add_contact, null);
        EditText nameInput = dialogView.findViewById(R.id.editTextName);
        EditText phoneInput = dialogView.findViewById(R.id.editTextPhone);

        nameInput.setText(contact.getName());
        phoneInput.setText(contact.getPhoneNumber());

        new AlertDialog.Builder(context)
                .setTitle("Edit Contact")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = nameInput.getText().toString();
                    String newPhone = phoneInput.getText().toString();
                    if (!newName.isEmpty() && !newPhone.isEmpty()) {
                        Contact editedContact = new Contact(newName, newPhone);
                        contacts.set(position, editedContact);
                        notifyItemChanged(position);
                        listener.onContactEdited(editedContact, position);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteConfirmationDialog(Contact contact, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    contacts.remove(position);
                    notifyItemRemoved(position);
                    listener.onContactDeleted(contact, position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView phoneText;
        ImageButton editButton;
        ImageButton deleteButton;

        ContactViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textName);
            phoneText = itemView.findViewById(R.id.textPhone);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
} 