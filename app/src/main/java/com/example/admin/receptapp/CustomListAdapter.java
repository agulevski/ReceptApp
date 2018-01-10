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
 * Created by Alexanders on 2018-01-10.
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

        TextView txtTitle = (TextView) rowView.findViewById(R.id.itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemname.get(position));
        imageView.setImageBitmap(image.get(position));
        return rowView;

    }
}
