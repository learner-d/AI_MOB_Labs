package ua.opu.herhel_ai183_lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mSubjectEditText;
    private EditText mMessageEditText;

    private Button mConfirmButton;
    private Button mCancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mEmailEditText = findViewById(R.id.email_et);
        mSubjectEditText = findViewById(R.id.subject_et);
        mMessageEditText = findViewById(R.id.message_et);


        mConfirmButton = findViewById(R.id.button_confirm);
        mCancelButton = findViewById(R.id.button_cancel);

        mSubjectEditText.setText(MainActivity.DEFAULT_SUBJECT);
        mMessageEditText.setText(MainActivity.DEFAULT_MESSAGE);

        mConfirmButton.setOnClickListener(this::buttonConfirm_onClick);
        mCancelButton.setOnClickListener(this::buttonCancel_onClick);
    }

    protected void buttonConfirm_onClick(View v){
        // 1. Отримуємо введені дані
        String email = mEmailEditText.getText().toString();
        String subject = mSubjectEditText.getText().toString();
        String message = mMessageEditText.getText().toString();

        // 2. Передаємо дані в Intent
        Intent i = new Intent();
        i.putExtra(Intent.EXTRA_EMAIL, email);
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, message);

        // 3. Встановлюємо результат роботи Activity
        setResult(RESULT_OK, i);

        // 4. Закриваємо Activity
        finish();
    }

    protected void buttonCancel_onClick(View v){
        // 1. Встановлюємо результат роботи Activity
        setResult(RESULT_CANCELED);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        buttonCancel_onClick(null);
    }
}
