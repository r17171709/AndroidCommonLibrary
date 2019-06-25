package com.renyu.commonlibrary.views.actionsheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.renyu.commonlibrary.views.wheelview.R;

/**
 * Created by Clevo on 2016/6/7.
 */
public class CenterListActionSheetAdapter extends BaseAdapter {
    private Context context;
    private String title[];

    public CenterListActionSheetAdapter(Context context, String[] title) {
        this.context = context;
        this.title = title;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_center, parent, false);
            holder = new ViewHolder();
            holder.pop_center_desp = convertView.findViewById(R.id.pop_center_desp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.pop_center_desp.setText(title[position]);
        return convertView;
    }

    public static class ViewHolder {
        TextView pop_center_desp;
    }
}
