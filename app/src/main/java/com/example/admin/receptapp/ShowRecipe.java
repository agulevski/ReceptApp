package com.example.admin.receptapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ShowRecipe extends AppCompatActivity {
    private RecipesDataSource datasource;
    SearchView inputView;
    TextView displayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);
        datasource = new RecipesDataSource(this);
        SQLiteDatabase db = datasource.open();

    }
    public void startInputRecipesActivity(View view){
        Intent intent = new Intent(this, InputRecipesActivity.class);
        startActivity(intent);
    }
}