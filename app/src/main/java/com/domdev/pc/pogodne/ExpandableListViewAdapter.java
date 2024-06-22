package com.domdev.pc.pogodne;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    String[] groupNames = new String[1];
    String[][] childNames = new String[1][1];
    ArrayList<String> list;
    int choosen = 0;

    ExpandableListView mExpandableList;

    Context context;

    public  ExpandableListViewAdapter(Context context, String name, ArrayList<String> list, ExpandableListView mExpandableListView)
    {
        this.context = context;
        setGroupNames(name);
        setChildren(list);
        mExpandableList = mExpandableListView;
        this.list = list;
    }

    void setGroupNames(String name)
    {
        groupNames[0] = name;
    }

    void setChildren(ArrayList<String> list) {
        childNames[0] = new String[list.size() - 1];
        int minus = 0;
        for (int i = 0; i < list.size(); ++i)
        {
            if (i == choosen)
                minus = 1;
            else
                childNames[0][i - minus] = list.get(i);
        }
    }

    @Override
    public int getGroupCount() {
        return groupNames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childNames[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupNames[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childNames[groupPosition][childPosition];
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        TextView textView = new TextView(context);
        textView.setText(groupNames[groupPosition]);
        textView.setPadding(80,0,0,0);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(19);

        textView.setBackground( context.getResources().getDrawable( R.drawable.list_border ) );

        return textView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final TextView textView = new TextView(context);
        textView.setText(childNames[groupPosition][childPosition]);
        textView.setPadding(100,0,0,0);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(19);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandableList.collapseGroup(0);
                String child = getChild(groupPosition, childPosition).toString();
                setGroupNames(child);
                choosen = list.indexOf(child);
                setChildren(list);
            }
        });

        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
