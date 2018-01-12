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
 * This is the "landing" fragment that is displayed on application start. It shows two scrollable
 * lists of (currently all) recipes for the user to browse. The lists use the small version of a
 * recipes image.
 */


public class HomeFragment extends Fragment {
    RecipesDataSource datasource;
    ListView leftView, rightView;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        List<Bitmap> images = datasource.getRecipeImgSmall();
        List<String> titles = datasource.getRecipeTitles();
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), titles, images);
        //TODO a separate adapter for rightView

        leftView = (getView().findViewById(R.id.lv_leftlist));
        rightView = (getView().findViewById(R.id.lv_rightlist));
        leftView.setAdapter(adapter);
        rightView.setAdapter(adapter);

        leftView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), RecipeInfoActivity.class);
                String recipe = String.valueOf(adapterView.getItemAtPosition(position));
                intent.putExtra("recipe", recipe);
                startActivity(intent);
            }
        });
        rightView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
