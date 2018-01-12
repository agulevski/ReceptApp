package com.example.admin.receptapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * This fragment shows a list of all recipes that have been saved as favorite. To remove or add
 * a recipe as favorite the user must click the button in RecipeInfoActivity.
 */

public class FavoritesFragment extends Fragment {
    RecipesDataSource datasource;
    ListView listView;

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datasource = new RecipesDataSource(getActivity());
        datasource.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        List<Bitmap> images = datasource.getFavoriteRecipeImgs();
        List<String> titles = datasource.getFavoriteRecipeTitle();
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), titles, images);

        listView = (getView().findViewById(R.id.lv_favorites));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                String recipe = String.valueOf(adapterView.getItemAtPosition(position));
                intent.putExtra("recipe", recipe);
                startActivity(intent);
            }
        });

        datasource.close();

    }
}


