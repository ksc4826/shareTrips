package com.example.yongs.sharetrips.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.model.Report;


import java.util.ArrayList;

public class ReportAdapter extends BaseAdapter {
    private Context context;
    public static ArrayList<Report> reportArrayList;

    private ReportHolder holder;

    public ReportAdapter(Context context, ArrayList<Report> reportArrayList){
        this.context = context;
        this.reportArrayList = reportArrayList;
    }

    @Override
    public int getCount() {
        return reportArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return reportArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.trip_report_item,null);

            holder = new ReportHolder();
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.date = (TextView) convertView.findViewById(R.id.user_date);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.location = (TextView) convertView.findViewById(R.id.location_name);
            holder.view = (TextView) convertView.findViewById(R.id.view);

            convertView.setTag(holder);
        }else{
            holder = (ReportHolder) convertView.getTag();
        }

        holder.username.setText(String.valueOf(reportArrayList.get(position).getUsername()));
        holder.date.setText(String.valueOf(reportArrayList.get(position).getDate()));
        holder.image.setImageBitmap(reportArrayList.get(position).getBitmap());
        String str = reportArrayList.get(position).getContent();
        str = str.replace("\\r\\n", "\r\n");
        str = str.replace("\\n", "\n");
        str = str.replace("\\r", "\r");
        reportArrayList.get(position).setContent(str);
        holder.content.setText(String.valueOf(reportArrayList.get(position).getContent()));
        holder.location.setText(String.valueOf(reportArrayList.get(position).getLocation()));
        holder.date.setText(String.valueOf(reportArrayList.get(position).getDate()));
        holder.view.setText(String.valueOf(reportArrayList.get(position).getView()));

        return convertView;
    }

    private class ReportHolder{
        private TextView username;
        private TextView date;
        private ImageView image;
        private TextView content;
        private TextView location;
        private TextView view;
    }
}
