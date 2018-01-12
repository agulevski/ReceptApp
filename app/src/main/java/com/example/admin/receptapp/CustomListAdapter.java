package com.example.admin.receptapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * CustomListAdapter containing an imageview and a textview in each row. Used in HomeFragment and
 * FavoritesFragment. Uses xml file listview.xml.
 */

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> itemname;
    private final List<Bitmap> image;

    public CustomListAdapter(Activity context, List<String> itemname, List<Bitmap> image){
        super(context, R.layout.listview, itemname);

        this.context = context;
        this.itemname = itemname;
        this.image = image;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview, null,true);

        ImageView imageView = rowView.findViewById(R.id.iv_icon);
        TextView txtTitle = rowView.findViewById(R.id.tv_itemname);

        txtTitle.setText(itemname.get(position));
        imageView.setImageBitmap(image.get(position));
        return rowView;

    }
}
