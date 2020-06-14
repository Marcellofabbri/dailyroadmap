package eu.marcellofabbri.dailyroadmap.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView;
        }

        Resources.Theme theme = context.getTheme();
        Drawable drawable = context.getResources().getDrawable(R.drawable.d0001, theme);

        imageView.setImageDrawable(drawable);
        return imageView;
    }
}
