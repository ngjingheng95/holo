package com.arholo.ar.sceneform.samples.chromakeyvideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.arholo.ar.sceneform.samples.chromakeyvideo.R;

import org.w3c.dom.Text;

import java.util.List;

public class PixabayCategoryAdapter extends BaseAdapter {

    private List<String> pixabayCategoryList;
    private final Context mContext;

    public PixabayCategoryAdapter(Context mContext, List<String> pixabayCategoryList) {
        this.mContext = mContext;
        this.pixabayCategoryList = pixabayCategoryList;
    }

    @Override
    public int getCount() {
        return pixabayCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String categoryName = pixabayCategoryList.get(position).toUpperCase();

        // view holder pattern
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_pixabay_category, null);

            final TextView pixabayCategoryButton = (TextView) convertView.findViewById(R.id.pixabay_category_button);

            final ViewHolder viewHolder = new ViewHolder(pixabayCategoryButton);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.pixabayCategoryButton.setText(categoryName);
        return convertView;
    }

    private class ViewHolder {
        private final TextView pixabayCategoryButton;

        public ViewHolder(TextView pixabayCategoryButton) {
            this.pixabayCategoryButton = pixabayCategoryButton;
        }
    }
}
