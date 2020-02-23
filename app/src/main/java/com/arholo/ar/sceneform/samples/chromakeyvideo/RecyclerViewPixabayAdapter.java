package com.arholo.ar.sceneform.samples.chromakeyvideo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewPixabayAdapter extends RecyclerView.Adapter<RecyclerViewPixabayAdapter.ViewHolder> {

    private Context mContext;
    private OnArObjectListener mOnArObjectListener;
    private String mTag;
    private int selectedPos = RecyclerView.NO_POSITION;

    private final String THUMBNAIL_URL = "https://i.vimeocdn.com/video/";
    private final String THUMBNAIL_SIZE = "_295x166.jpg";
    private List<PixabayVideoInfo> pixabayVideoInfo;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View layout;
        public ImageView gridItemImage;
        public OnArObjectListener onArObjectListener;
        public ViewHolder(View itemView, OnArObjectListener onArObjectListener) {
            super(itemView);
            layout = itemView;
            gridItemImage = (ImageView) itemView.findViewById(R.id.grid_item_image);
            this.onArObjectListener = onArObjectListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            notifyItemChanged(selectedPos);
            selectedPos = getAdapterPosition();
            notifyItemChanged(selectedPos);
            onArObjectListener.onArObjectClick(getAdapterPosition(), mTag);
        }
    }


    public RecyclerViewPixabayAdapter(Context mContext, List<PixabayVideoInfo> pixabayVideoInfo, OnArObjectListener mOnArObjectListener, String tag) {
        this.mContext = mContext;
        this.pixabayVideoInfo = pixabayVideoInfo;
        this.mOnArObjectListener = mOnArObjectListener;
        this.mTag = tag;
    }

    //    The adapter prepares the layout of the items by inflating the correct layout for the
//    individual data elements. This work is done in the onCreateViewHolder method.
//    It returns an object of type ViewHolder per visual entry in the recycler view.
    @NonNull
    @Override
    public RecyclerViewPixabayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.pixabay_layout, parent, false);
        ViewHolder vh = new ViewHolder(v, mOnArObjectListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String url = THUMBNAIL_URL + pixabayVideoInfo.get(position).getPictureId() + THUMBNAIL_SIZE;
        Picasso.with(mContext).load(url).into(holder.gridItemImage);
        holder.itemView.setBackgroundColor(selectedPos == position ? Color.BLUE : Color.TRANSPARENT);

    }


    //  return the total number of items
    @Override
    public int getItemCount() {
        return pixabayVideoInfo.size();
    }


    public interface OnArObjectListener{
        void onArObjectClick(int position, String tag);
    }

    public PixabayVideoInfo getItem(int position) {
        return pixabayVideoInfo.get(position);
    }

    public void clearHighlight() {
        notifyItemChanged(selectedPos);
        selectedPos = RecyclerView.NO_POSITION;
        notifyItemChanged(selectedPos);
    }

}
