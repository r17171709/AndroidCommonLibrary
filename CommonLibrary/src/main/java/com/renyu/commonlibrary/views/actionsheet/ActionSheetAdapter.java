package com.renyu.commonlibrary.views.actionsheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.renyu.commonlibrary.R;

/**
 * Created by Clevo on 2016/6/7.
 */
public class ActionSheetAdapter extends BaseAdapter {

    Context context;
    String title[];

    public ActionSheetAdapter(Context context, String[] title) {
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
        if (convertView==null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_pop, parent, false);
            holder=new ViewHolder();
            holder.pop_desp= (TextView) convertView.findViewById(R.id.pop_desp);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.pop_desp.setText(title[position]);
        return convertView;
    }

    public static class ViewHolder {
        TextView pop_desp;
    }
}
