package com.renyu.commonlibrary.views.actionsheet;

import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.renyu.commonlibrary.views.wheelview.R;

/**
 * Created by Clevo on 2016/6/7.
 */
public class ActionSheetAdapter extends BaseAdapter {
    private Context context;
    private String title[];
    private String subTitle[];
    private int choiceIndex;

    ActionSheetAdapter(Context context, String[] title, String subTitle[], int choiceIndex) {
        this.context = context;
        this.title = title;
        this.subTitle = subTitle;
        this.choiceIndex = choiceIndex;
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
            holder.pop_desp= convertView.findViewById(R.id.pop_desp);
            holder.pop_sub_desp= convertView.findViewById(R.id.pop_sub_desp);
            holder.pop_checkcolor = convertView.findViewById(R.id.pop_checkcolor);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
        if (position == choiceIndex) {
            holder.pop_desp.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.pop_checkcolor.setVisibility(View.VISIBLE);
        }
        else {
            holder.pop_desp.setTextColor(Color.parseColor("#333333"));
            holder.pop_checkcolor.setVisibility(View.GONE);
        }
        holder.pop_desp.setText(title[position]);
        holder.pop_sub_desp.setText(subTitle[position]);
        if (TextUtils.isEmpty(subTitle[position])) {
            holder.pop_sub_desp.setVisibility(View.GONE);
        }
        else {
            holder.pop_sub_desp.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView pop_desp;
        TextView pop_sub_desp;
        ImageView pop_checkcolor;
    }

    void setChoiceIndex(int choiceIndex) {
        this.choiceIndex = choiceIndex;
        notifyDataSetChanged();
    }
}
