package com.example.peojulgae;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableAdpter {
    public class ExpandableAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> categoryList;
        private HashMap<String, List<String>> itemList;

        public ExpandableAdapter(HashMap<String, List<String>> itemList) {
            this.context = context;
            this.categoryList = categoryList;
            this.itemList = itemList;
        }

        @Override
        public int getGroupCount() {
            return this.categoryList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.itemList.get(this.categoryList.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.categoryList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.itemList.get(this.categoryList.get(groupPosition)).get(childPosition);
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
            String categoryTitle = (String) getGroup(groupPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.category_list, null);
            }

            TextView categoryTv = convertView.findViewById(R.id.tvCategory);
            categoryTv.setText(categoryTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String itemTitle = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_list, null);
            }

            TextView itemTv = convertView.findViewById(R.id.eListView);
            itemTv.setText(itemTitle);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
