package com.example.yongs.sharetrips.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.yongs.sharetrips.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CountryListAdapter extends BaseExpandableListAdapter implements Filterable {
    private Context _context;
    private List<String> header;

    private List<String> unFilteredHeader;
    private HashMap<String, List<String>> unFilteredChild;

    private HashMap<String, List<String>> child;

    boolean isFilter = false;

    public CountryListAdapter(Context context, List<String> listDataHeader,
                              HashMap<String, List<String>> listChildData) {
        this._context = context;
        this.header = listDataHeader;
        this.child = listChildData;
        this.unFilteredChild = listChildData;
        this.unFilteredHeader = listDataHeader;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.child.get(this.header.get(groupPosition)).get(
                childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.childs, parent, false);
        }

        TextView child_text = convertView.findViewById(R.id.child);

        child_text.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.child.get(this.header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.header.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.header.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header, parent, false);
        }

        TextView header_text = convertView.findViewById(R.id.header);

        header_text.setText(headerTitle);

        if (isExpanded) {
            header_text.setTypeface(null, Typeface.BOLD);
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_up, 0);
        } else {
            header_text.setTypeface(null, Typeface.NORMAL);
            header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.ic_down, 0);
        }

        if (isFilter)
            ((ExpandableListView) parent).expandGroup(groupPosition, true);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                if (charString.isEmpty()) {
                    header = unFilteredHeader;
                    child = unFilteredChild;
                    isFilter = false;

                } else {
                    HashMap<String, List<String>> filteringList = new HashMap<String, List<String>>();
                    ArrayList<String> filteringHeader = new ArrayList<>();

                    for (String key : unFilteredChild.keySet()) {
                        ArrayList<String> filteringChildd = new ArrayList<>();
                        List<String> filteringChild = unFilteredChild.get(key);
                        for (Object object : filteringChild) {
                            String element = (String) object;
                            if (element.contains(charString)) {
                                if (!filteringHeader.contains(key)) {
                                    filteringHeader.add(key);
                                }
                                filteringChildd.add(element);
                            }
                        }

                        filteringList.put(key, filteringChildd);
                    }

                    header = filteringHeader;
                    child = filteringList;

                    isFilter = true;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = header;


                return filterResults;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                header = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }




}