package com.example.admin.receptapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class InputRecipesActivity extends AppCompatActivity {
    private RecipesDataSource datasource;
    SearchView inputView;
    TextView displayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_recipes);
        datasource = new RecipesDataSource(this);
        SQLiteDatabase db = datasource.open();

    }

    public void handleInput(View view){
        inputView = (findViewById(R.id.searchView));
        CharSequence inputStatement = inputView.getQuery();
        if(inputStatement!= null) {
            List<Recipe> foundRecipes = datasource.getRecipeByIngredients(inputStatement);
            displayText = (findViewById(R.id.textView));
            displayText.setText(foundRecipes.toString());
        }else{
            Context context = getApplicationContext();
            CharSequence error = "Skriv in en ingrediens först";
            int duration = Toast.LENGTH_SHORT;
            Toast errorMessage = Toast.makeText(context, error, duration);
            errorMessage.show();

        }

    }
}
