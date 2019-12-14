package com.google.ar.sceneform.samples.chromakeyvideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PixabayAdapter extends BaseAdapter {

    private final Context mContext;
    //TODO: Change to Pixabay API query results
    private final Book[] books;

    //TODO: Change Book to appropriate var name
    public PixabayAdapter(Context context, Book[] books) {
        this.mContext = context;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.length;
    }

    // Return 0
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // Return null
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_book, null);

            final ImageView gridItemImage = (ImageView)convertView.findViewById(R.id.grid_item_image);
            final TextView gridItemTitle = (TextView)convertView.findViewById(R.id.grid_item_title);

            final ViewHolder viewHolder = new ViewHolder(gridItemImage, gridItemTitle);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        // TODO: Make changes here
        viewHolder.nameTextView.setText(mContext.getString(book.getName()));
        Picasso.with(mContext).load(book.getImageUrl()).into(viewHolder.imageViewCoverArt);

        return convertView;
    }

    // View Holder Pattern: implement a class to “hold” the subviews inside of your cells. This avoids memory-expensive calls to findViewById()
    private class ViewHolder {
        private final ImageView gridItemImage;
        private final TextView gridItemTitle;

        public ViewHolder(ImageView gridItemImage, TextView gridItemTitle) {
            this.gridItemImage = gridItemImage;
            this.gridItemTitle = gridItemTitle;
        }
    }


}
