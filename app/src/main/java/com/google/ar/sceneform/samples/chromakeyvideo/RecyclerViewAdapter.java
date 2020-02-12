package com.google.ar.sceneform.samples.chromakeyvideo;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<File> files;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public ImageView itemImage;
        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }

    public void add(int position, File item) {
        files.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        files.remove(position);
        notifyItemRemoved(position);
    }

    public RecyclerViewAdapter(Context mContext, List<File> filesList) {
        this.mContext = mContext;
        this.files = filesList;

    }

    //    The adapter prepares the layout of the items by inflating the correct layout for the
//    individual data elements. This work is done in the onCreateViewHolder method.
//    It returns an object of type ViewHolder per visual entry in the recycler view.
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_media, parent, false);
//        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
//        layoutParams.height = (int) (parent.getHeight() * 0.3);
//        v.setLayoutParams(layoutParams);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final File file = files.get(position);
        Glide.with(mContext).load(file).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "gallery: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    //    Every visible entry in a recycler view is filled with the correct data model item by the
//    adapter. Once a data item becomes visible, the adapter assigns this data to the individual
//    widgets which he inflated earlier.


//  return the total number of items
    @Override
    public int getItemCount() {
        return files.size();
    }


}
