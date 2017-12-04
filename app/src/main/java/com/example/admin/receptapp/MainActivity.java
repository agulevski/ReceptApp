package com.example.admin.receptapp;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecipesDataSource datasource;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (findViewById(R.id.text));

        datasource = new RecipesDataSource(this);
        try {
            datasource.open();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        try {
            datasource.insertFromFile(this, R.raw.init);
        } catch (IOException e) {
            e.printStackTrace();
        }
        displayRecipes();

    }

    public int displayRecipes(){
        int countRecipes = 0;

        //Get all recipes
        List<Recipe> values = datasource.getAllRecipes();
        StringBuilder builder = new StringBuilder();

        //Loop through arraylist
        for (Recipe recipes : values) {
            builder.append((recipes + "\n"));
            countRecipes++;
        }
        //Display all content in textview
        textView.setText(builder.toString());

        //Return number of recipes displayed
        return countRecipes;
    }

    @Override
    protected void onResume() {
        try {
            datasource.open();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}
