package com.arholo.ar.sceneform.samples.chromakeyvideo;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private int selectedPos = RecyclerView.NO_POSITION;
    private List<File> files;
    private Context mContext;
    private OnArObjectListener mOnArObjectListener;
    private String mTag;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View layout;
        public ImageView itemImage;
        public OnArObjectListener onArObjectListener;
        public ViewHolder(View itemView, OnArObjectListener onArObjectListener) {
            super(itemView);
            layout = itemView;
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            this.onArObjectListener = onArObjectListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating old as well as new positions
            notifyItemChanged(selectedPos);
            selectedPos = getAdapterPosition();
            notifyItemChanged(selectedPos);
            onArObjectListener.onArObjectClick(getAdapterPosition(), mTag);

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



    public RecyclerViewAdapter(Context mContext, List<File> filesList, OnArObjectListener mOnArObjectListener, String tag) {
        this.mContext = mContext;
        this.files = filesList;
        this.mOnArObjectListener = mOnArObjectListener;
        this.mTag = tag;
    }

    //    The adapter prepares the layout of the items by inflating the correct layout for the
//    individual data elements. This work is done in the onCreateViewHolder method.
//    It returns an object of type ViewHolder per visual entry in the recycler view.
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_media, parent, false);
        ViewHolder vh = new ViewHolder(v, mOnArObjectListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final File file = files.get(position);
        Glide.with(mContext).load(file).into(holder.itemImage);
        holder.itemView.setBackgroundColor(selectedPos == position ? Color.BLUE : Color.TRANSPARENT);
//        if(selectedPosition==position)
//            holder.itemView.setBackgroundColor(Color.parseColor("#000000"));
//        else
//            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
    }



//  return the total number of items
    @Override
    public int getItemCount() {
        return files.size();
    }


    public interface OnArObjectListener{
        void onArObjectClick(int position, String tag);
    }

    public File getItem(int position) {
        return files.get(position);
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public void clearHighlight() {
        notifyItemChanged(selectedPos);
        selectedPos = RecyclerView.NO_POSITION;
        notifyItemChanged(selectedPos);
    }
}
