package com.example.admin.receptapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class HomeFragment extends Fragment {
    TextView textView;
    RecipesDataSource datasource;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView = (getView().findViewById(R.id.text));
        //displayRecipes();


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
}
