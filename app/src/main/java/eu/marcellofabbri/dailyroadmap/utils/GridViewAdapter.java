package eu.marcellofabbri.dailyroadmap.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import eu.marcellofabbri.dailyroadmap.R;

public class GridViewAdapter extends BaseAdapter {
    private List<String> listIconNames;
    private LayoutInflater layoutInflater;
    private Context context;

    public GridViewAdapter(List<String> listData, Context context) {
        this.listIconNames = listData;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listIconNames.size();
    }

    @Override
    public Object getItem(int position) {
        return listIconNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View grid, ViewGroup parent) {
        ImageView imageView;

        if (grid == null) {
            grid = layoutInflater.inflate(R.layout.individual_icon_imageview, null);
            imageView = (ImageView) grid.findViewById(R.id.individual_icon_imageview);
            grid.setTag(imageView);
        } else {
            imageView = (ImageView) grid.getTag();
        }

//        int resourceId = context.getResources().getIdentifier(listIconNames.get(position), "drawable", context.getPackageName());
//        Drawable drawable = context.getDrawable(resourceId);

        Drawable drawable = extractDrawableFromArray(position);
        System.out.println(extractResourceIdFromArray(62));

        imageView.setImageDrawable(drawable);
        return grid;
    }

    public Drawable extractDrawableFromArray(int position) {
        int resourceId = extractResourceIdFromArray(position);
        return context.getDrawable(resourceId);
    }

    public int extractResourceIdFromArray(int position) {
        return context.getResources().getIdentifier(listIconNames.get(position), "drawable", context.getPackageName());
    }
}
