package ua.opu.herhel_ai183_lab3;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddContactActivity extends AppCompatActivity {
    private static final int IMAGE_CAPTURE_REQ_CODE = 5437;
    private ImageView _contactImage;
    private Uri _contactImageUri;

    private EditText _nameEditText;
    private EditText _emailEditText;
    private EditText _phoneEditText;

    private Button _addContactBtn;
    private Button _takePhotoBtn;
    private Button _cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        setWindow();

        _contactImage = findViewById(R.id.profile_image);
        _nameEditText = findViewById(R.id.name_et);
        _emailEditText = findViewById(R.id.email_et);
        _phoneEditText = findViewById(R.id.phone_et);

        _addContactBtn = findViewById(R.id.button_add);
        _takePhotoBtn = findViewById(R.id.button_camera);
        _cancelBtn = findViewById(R.id.button_cancel);

        _takePhotoBtn.setOnClickListener(v -> {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePhotoIntent, IMAGE_CAPTURE_REQ_CODE);
            }
            catch (ActivityNotFoundException e){
                Toast.makeText(this, "Camera app couldn't be opened", Toast.LENGTH_LONG).show();
            }
        });

        _addContactBtn.setOnClickListener(v ->{
            if (_contactImageUri == null) {
                Toast.makeText(this, "The contact has no image!", Toast.LENGTH_SHORT);
                return;
            }

            String name = _nameEditText.getText().toString();
            String email = _emailEditText.getText().toString();
            String phone = _phoneEditText.getText().toString();

            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_USER, name);
            i.putExtra(Intent.EXTRA_EMAIL, email);
            i.putExtra(Intent.EXTRA_PHONE_NUMBER, phone);
            i.putExtra(Intent.EXTRA_ORIGINATING_URI, _contactImageUri.toString());

            setResult(RESULT_OK, i);
            finish();
        });

        _cancelBtn.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CAPTURE_REQ_CODE && resultCode == RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            onContactPhotoCaptured((Bitmap) extras.get("data"));
        }
    }

    private void onContactPhotoCaptured(Bitmap imageBitmap){
        // Міняємо ImageView із зображенням контакту
        _contactImage.setImageBitmap(imageBitmap);

        String filename = "contact_" + System.currentTimeMillis() + ".png";

        File outputDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File outputFile = File.createTempFile("contact_avatar_", ".png", outputDir);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

            // Зберігаємо шлях до файлу в форматі Uri
            _contactImageUri = Uri.fromFile(outputFile);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        setResult(RESULT_CANCELED);
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
}
