package ua.opu.herhel_ai183_lab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 9999;
    private static final int REQUEST_IMAGE_CAPTURE = 1000;

    private Uri imageUri;
    private ImageView mImageView;
    private TextView mEmailField;
    private TextView mSubjectField;
    private TextView mMessageField;

    public static final String DEFAULT_EMAIL = "dmitrogergel2000@gmail.com";
    public static final String DEFAULT_SUBJECT = "*ПМП АІ-183 Гергель*";
    public static final String DEFAULT_MESSAGE = "Лаборатона робота №2\nКльове селфі:";

    private String mEmailStr = DEFAULT_EMAIL;
    private String mSubjectStr = DEFAULT_SUBJECT;
    private String mMessageStr = DEFAULT_MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.camera_image);
        mEmailField = findViewById(R.id.textview_to);
        mSubjectField = findViewById(R.id.textview_subject);
        mMessageField = findViewById(R.id.textview_message);

        updateDetails();

        findViewById(R.id.button_details).setOnClickListener(this::buttonDetails_onClick);
        findViewById(R.id.button_camera).setOnClickListener(this::buttonCamera_onClick);
        findViewById(R.id.button_send).setOnClickListener(this::buttonSend_onClick);
    }

    protected void buttonDetails_onClick(View v) {
        Intent i = new Intent(this, SecondActivity.class);
        startActivityForResult(i, SECOND_ACTIVITY_REQUEST_CODE);
    }

    protected void buttonCamera_onClick(View v){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error while trying to open camera app", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    protected void buttonSend_onClick(View v){
        try {
            Intent mail = new Intent(Intent.ACTION_SENDTO);
            mail.setData(Uri.parse("mailto:"));
            mail.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmailStr});
            mail.putExtra(Intent.EXTRA_SUBJECT, mSubjectStr);
            mail.putExtra(Intent.EXTRA_TEXT, mMessageStr);
            mail.putExtra(Intent.EXTRA_STREAM, imageUri);

            startActivity(Intent.createChooser(mail, "Choose email client"));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "There are no email clients installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    mEmailStr = data.getStringExtra(Intent.EXTRA_EMAIL);
                    mSubjectStr = data.getStringExtra(Intent.EXTRA_SUBJECT);
                    mMessageStr = data.getStringExtra(Intent.EXTRA_TEXT);

                    updateDetails();
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action cancelled!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);

            try {
                File extDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File outputFile = File.createTempFile("img-capture-", ".png", extDir);
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                imageUri = Uri.fromFile(outputFile);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateDetails() {
        mEmailField.setText(getString(R.string.textview_to_text, mEmailStr));
        mSubjectField.setText(getString(R.string.textview_subject_text, mSubjectStr));
        mMessageField.setText(getString(R.string.textview_message_text, mMessageStr));
    }
}