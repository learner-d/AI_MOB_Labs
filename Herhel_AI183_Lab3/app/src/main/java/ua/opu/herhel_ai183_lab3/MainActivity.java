package ua.opu.herhel_ai183_lab3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.DeleteItemListener {
    private static final int ADD_CONTACT_REQUEST_CODE = 33399;

    private RecyclerView _recyclerView;
    private FloatingActionButton _addContactButton;

    private List<Contact> _contacts = new ArrayList<>();

    private ContactsAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWindow();

        _recyclerView = findViewById(R.id.list);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _adapter = new ContactsAdapter(getApplicationContext(), _contacts, this);
        _recyclerView.setAdapter(_adapter);

        _addContactButton = findViewById(R.id.fab);
        _addContactButton.setOnClickListener(v-> {
            Intent i = new Intent(this, AddContactActivity.class);
            startActivityForResult(i, ADD_CONTACT_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            onContactAdd(data);
        }
    }

    private void onContactAdd(Intent data){
        String name = data.getStringExtra(Intent.EXTRA_USER);
        String email = data.getStringExtra(Intent.EXTRA_EMAIL);
        String phone = data.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Uri uri = Uri.parse(data.getStringExtra(Intent.EXTRA_ORIGINATING_URI));

        _contacts.add(new Contact(name, email, phone, uri));
        _adapter.notifyDataSetChanged();
    }

    private void setWindow() {
        // Метод встановлює StatusBar в колір фону
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.activity_background));

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void verifyStoragePermissions() {
        // Перевіряємо наявність дозволу на запис у зовнішнє сховище
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // запитуємо дозвіл у користувача
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        verifyStoragePermissions();
    }

    @Override
    public void onDeleteItem(int position) {
        _contacts.remove(position);
        _adapter.notifyDataSetChanged();
    }
}