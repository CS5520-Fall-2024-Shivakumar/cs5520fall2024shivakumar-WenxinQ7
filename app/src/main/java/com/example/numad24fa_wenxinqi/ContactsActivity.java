package com.example.numad24fa_wenxinqi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import com.google.android.material.snackbar.Snackbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class ContactsActivity extends AppCompatActivity implements ContactsAdapter.ContactActionListener {
    private static final int PERMISSION_REQUEST_CALL_PHONE = 1;
    private List<Contact> contacts = new ArrayList<>();
    private ContactsAdapter adapter;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        checkCallPermission();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactsAdapter(contacts, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddContactDialog());
    }

    private void checkCallPermission() {
        if (ContextCompat.checkSelfPermission(this, 
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Call permission is required to make calls", 
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showAddContactDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);
        EditText nameInput = dialogView.findViewById(R.id.editTextName);
        EditText phoneInput = dialogView.findViewById(R.id.editTextPhone);

        new AlertDialog.Builder(this)
                .setTitle("Add New Contact")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameInput.getText().toString();
                    String phone = phoneInput.getText().toString();
                    if (!name.isEmpty() && !phone.isEmpty()) {
                        Contact newContact = new Contact(name, phone);
                        contacts.add(newContact);
                        adapter.notifyItemInserted(contacts.size() - 1);
                        showSuccessSnackbar(newContact);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSuccessSnackbar(Contact contact) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                "Contact added successfully", Snackbar.LENGTH_LONG);
        snackbar.setAction("CALL NOW", v -> {
            // Initiate call to newly added contact
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
            if (ActivityCompat.checkSelfPermission(this, 
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            }
        });
        snackbar.show();
    }

    @Override
    public void onContactDeleted(Contact contact, int position) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                "Contact deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", v -> {
            contacts.add(position, contact);
            adapter.notifyItemInserted(position);
        });
        snackbar.show();
    }

    @Override
    public void onContactEdited(Contact contact, int position) {
        Snackbar.make(coordinatorLayout,
                "Contact updated successfully", Snackbar.LENGTH_SHORT).show();
    }
}