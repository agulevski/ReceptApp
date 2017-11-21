package com.example.admin.receptapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecipesDataSource datasource;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        datasource = new RecipesDataSource(this);
        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            datasource.insertFromFile(this, R.raw.init);
        } catch (IOException e) {
            e.printStackTrace();
        }

        textView = textView.findViewById(R.id.text);

        List<Recipe> values = datasource.getAllRecipes();
        StringBuilder builder = new StringBuilder();
        for (Recipe recipes : values) {
            builder.append((recipes + "\n"));
        }
        textView.setText(builder.toString());

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
      /*  ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);*/
    }


    // Will be called via the onClick attribute
    // of the buttons in main.xml
   /* public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Recipe> adapter = (ArrayAdapter<Recipe>) getListAdapter();
        Recipe recipe = null;
        switch (view.getId()) {
            case R.id.add:
                String[] recipes = new String[] { "Cool", "Very nice", "Hate it" };
                int nextInt = new Random().nextInt(3);
                // save the new comment to the database
                recipe = datasource.createRecipe(recipes[nextInt]);
                adapter.add(recipe);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    recipe = (Recipe) getListAdapter().getItem(0);
                    datasource.deleteRecipe(recipe);
                    adapter.remove(recipe);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }*/

    @Override
    protected void onResume() {
        try {
            datasource.open();
        } catch (SQLException e) {
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
