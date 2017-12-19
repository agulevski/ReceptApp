package com.example.admin.receptapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

public class InputRecipesActivity extends ListActivity {
    private RecipesDataSource datasource;
    SearchView inputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_recipes);
        datasource = new RecipesDataSource(this);
        SQLiteDatabase db = datasource.open();
        ListView listView = (ListView)findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(this, RecipeInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    public void handleInput(View view){
        inputView = (findViewById(R.id.searchView));
        CharSequence inputStatement = inputView.getQuery();
        if(inputStatement!= "") {
            List<String> foundRecipes = datasource.getRecipeByIngredients(inputStatement);
            ArrayAdapter<String> adapter = new ArrayAdapter<String> (getListView().getContext(), android.R.layout.simple_list_item_1, foundRecipes);
            getListView().setAdapter(adapter);
        }else{
            Context context = getApplicationContext();
            CharSequence error = "Skriv in en ingrediens först";
            int duration = Toast.LENGTH_SHORT;
            Toast errorMessage = Toast.makeText(context, error, duration);
            errorMessage.show();

        }

    }


}
