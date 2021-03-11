package ua.opu.herhel_ai183_lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView _helloTextView;
    private EditText _nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _helloTextView = findViewById(R.id.textview_hello);
        _nameEditText = findViewById(R.id.edittext_name);
    }
    public void mainButton_OnClick(View view) {
        String enteredName = _nameEditText.getText().toString();
        _helloTextView.setText(String.format(getString(R.string.label_hello_name), enteredName));
    }
}