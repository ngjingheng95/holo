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
    private final String[] thumbnailUrl;

    public PixabayAdapter(Context context, String[] thumbnailUrl) {
        this.mContext = context;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int getCount() {
        return thumbnailUrl.length;
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
        final String url = thumbnailUrl[i];
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.pixabay_layout, null);

            final ImageView gridItemImage = (ImageView)convertView.findViewById(R.id.grid_item_image);
            // To display top 3 tags
//            final TextView gridItemTitle = (TextView)convertView.findViewById(R.id.grid_item_title);

//            final ViewHolder viewHolder = new ViewHolder(gridItemImage, gridItemTitle);
            final ViewHolder viewHolder = new ViewHolder(gridItemImage);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
//        viewHolder.gridItemTitle.setText(mContext.getString(book.getName()));
        Picasso.with(mContext).load("https://i.vimeocdn.com/video/529927645_295x166.jpg").into(viewHolder.gridItemImage);

        return convertView;
    }

    // View Holder Pattern: implement a class to “hold” the subviews inside of your cells. This avoids memory-expensive calls to findViewById()
    private class ViewHolder {
        private final ImageView gridItemImage;

        public ViewHolder(ImageView gridItemImage) {
            this.gridItemImage = gridItemImage;
        }
    }

//    private class ViewHolder {
//        private final ImageView gridItemImage;
//        private final TextView gridItemTitle;
//
//        public ViewHolder(ImageView gridItemImage, TextView gridItemTitle) {
//            this.gridItemImage = gridItemImage;
//            this.gridItemTitle = gridItemTitle;
//        }
//    }


}
