package com.example.admin.receptapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EmailActivity extends AppCompatActivity {
    EditText et_to, et_cc;
    Button bt_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        et_to = (findViewById(R.id.et_to));
        et_cc = (findViewById(R.id.et_cc));
        bt_send = (findViewById(R.id.bt_send));



        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

    }

    protected void sendEmail() {
        Log.i("Send email", "");


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = (String) bundle.get("title");
        String description = (String) bundle.get("description");
        String ingredients = (String) bundle.get("ingredients");
        String instructions = (String) bundle.get("instructions");


        String[] TO = {et_to.getText().toString()};
        String[] CC = {et_cc.getText().toString()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ämne");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Test!");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EmailActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
