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


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = (String) bundle.get("title");
        String description = (String) bundle.get("description");
        String ingredients = (String) bundle.get("ingredients");
        String instructions = (String) bundle.get("instructions");
        String emailContent = title + "\n\n" + description + "\n\n" + ingredients + "\n\n" + instructions;


        String[] TO = {et_to.getText().toString()};
        String[] CC = {et_cc.getText().toString()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "*Person* vill dela ett recept med dig: " +title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);




        try {
            startActivity(Intent.createChooser(emailIntent, "Dela recept"));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EmailActivity.this,
                    "Kunde inte hitta en mailklient på enheten", Toast.LENGTH_SHORT).show();
        }
    }
}
