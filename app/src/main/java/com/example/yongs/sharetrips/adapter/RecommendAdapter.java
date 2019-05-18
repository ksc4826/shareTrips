package com.example.yongs.sharetrips.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.model.Report;

import java.util.ArrayList;
import java.util.HashMap;

public class RecommendAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> parentList;
    private ArrayList<Report> childList;
    private ReportHolder childHolder;
    private HashMap<String, ArrayList<Report>> childHashMap;

    public RecommendAdapter(Context context, ArrayList<String> parentList, HashMap<String, ArrayList<Report>> childHashMap){
        this.context = context;
        this.parentList = parentList;
        this.childHashMap = childHashMap;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childHashMap.get(this.parentList.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Report getChild(int groupPosition, int childPosition) {
        return this.childHashMap.get(this.parentList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater groupInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = groupInflater.inflate(R.layout.fragment_recommend_parent, parent, false);
        }

        TextView parentText = (TextView)convertView.findViewById(R.id.parenttext);
        parentText.setText(getGroup(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Report report = (Report)getChild(groupPosition, childPosition);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.trip_report_item,null);

            childHolder = new ReportHolder();
            childHolder.username = (TextView) convertView.findViewById(R.id.username);
            childHolder.date = (TextView) convertView.findViewById(R.id.user_date);
            childHolder.image = (ImageView) convertView.findViewById(R.id.image);
            childHolder.content = (TextView) convertView.findViewById(R.id.content);
            childHolder.location = (TextView) convertView.findViewById(R.id.location_name);
            childHolder.view = (TextView) convertView.findViewById(R.id.view);

            convertView.setTag(childHolder);
        }else{
            childHolder = (ReportHolder) convertView.getTag();
        }

        childHolder.username.setText(String.valueOf(getChild(groupPosition, childPosition).getUsername()));
        childHolder.date.setText(String.valueOf(getChild(groupPosition, childPosition).getDate()));
        childHolder.image.setImageBitmap(getChild(groupPosition, childPosition).getBitmap());
        childHolder.content.setText(String.valueOf(getChild(groupPosition, childPosition).getContent()));
        childHolder.location.setText(String.valueOf(getChild(groupPosition, childPosition).getLocation()));
        childHolder.date.setText(String.valueOf(getChild(groupPosition, childPosition).getDate()));
        childHolder.view.setText(String.valueOf(getChild(groupPosition, childPosition).getView()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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
