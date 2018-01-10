package com.example.admin.receptapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;


public class SearchFragment extends Fragment {
    private RecipesDataSource datasource;
    SearchView inputView;
    ListView listView;
    ImageButton imageButton;


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datasource = new RecipesDataSource(getActivity());
        SQLiteDatabase db = datasource.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        imageButton = (getView().findViewById(R.id.imageButton));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleInput(view);
            }
        });
        listView = getView().findViewById(R.id.list);
        //Handle output when user clicks item in list (start RecipeInfoActivity)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                String recipe = String.valueOf(adapterView.getItemAtPosition(position));
                intent.putExtra("recipe", recipe);
                startActivity(intent);
            }
        });

    }
    public void handleInput(View view){
        inputView = (getView().findViewById(R.id.searchView));
        CharSequence inputStatement = inputView.getQuery();
        //if(inputStatement!= "") {
            List<String> foundRecipes = datasource.getRecipeByIngredients(inputStatement);
            ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(), android.R.layout.simple_list_item_1, foundRecipes);
            listView.setAdapter(adapter);
        /*}else{
            Context context = getActivity();
            CharSequence error = "Skriv in en ingrediens först";
            int duration = Toast.LENGTH_SHORT;
            Toast errorMessage = Toast.makeText(context, error, duration);
            errorMessage.show();

        }*/

    }
}


