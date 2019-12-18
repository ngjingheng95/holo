package com.google.ar.sceneform.samples.chromakeyvideo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PixabayAdapter extends BaseAdapter {
    private final Context mContext;
    String[] thumbnailUrl = {
            "https://i.vimeocdn.com/video/529927645_295x166.jpg",
            "https://i.vimeocdn.com/video/681648203_295x166.jpg",
            "https://i.vimeocdn.com/video/529927645_295x166.jpg",
            "https://i.vimeocdn.com/video/681648203_295x166.jpg",
            "https://i.vimeocdn.com/video/529927645_295x166.jpg",
            "https://i.vimeocdn.com/video/681648203_295x166.jpg",
            "https://i.vimeocdn.com/video/529927645_295x166.jpg",
            "https://i.vimeocdn.com/video/681648203_295x166.jpg",
            "https://i.vimeocdn.com/video/529927645_295x166.jpg",
            "https://i.vimeocdn.com/video/681648203_295x166.jpg"};


    public PixabayAdapter(Context context) {
        this.mContext = context;
        Log.d("MyApp", "PixabayAdapter Constructor");
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
    public View getView(int i, View convertView, ViewGroup parent) {
        Log.d("MyApp","In getView()");
        String url = thumbnailUrl[i];
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.pixabay_layout, null);

            final ImageView gridItemImage = (ImageView)convertView.findViewById(R.id.grid_item_image);
            if (gridItemImage != null){
                Log.d("MyApp","NON NULL");
            }
            else{
                Log.d("MyApp","NULL");
            }
            // To display top 3 tags
//            final TextView gridItemTitle = (TextView)convertView.findViewById(R.id.grid_item_title);

//            final ViewHolder viewHolder = new ViewHolder(gridItemImage, gridItemTitle);
            final ViewHolder viewHolder = new ViewHolder(gridItemImage);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
//        viewHolder.gridItemTitle.setText(mContext.getString(book.getName()));
        Picasso.with(mContext).load(url).into(viewHolder.gridItemImage);

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
