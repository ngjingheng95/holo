package com.google.ar.sceneform.samples.chromakeyvideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class HomepageMyHolosAdapter extends BaseAdapter {

    private List<File> files;

    private Context context;

    public HomepageMyHolosAdapter(Context c, List<File> files) {
        context = c;
        this.files = files;
    }

    public int getCount() {
        return files.size();
    }

    public File getItem(int position) {
        return files.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_media, parent, false);
        }
        imageView = convertView.findViewById(R.id.item_image);
        File file = getItem(position);
        Glide.with(context).load(file).into(imageView);
        return convertView;
    }
}
