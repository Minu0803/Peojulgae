package com.example.peojulgae;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

    public class CategoryActivity extends AppCompatActivity {

        ExpandableListView expandableListView;
        ExpandableAdapter expandableAdapter;
        HashMap<String, List<String>> categoryItemMap;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            expandableListView = findViewById(R.id.eListView);
            categoryItemMap = new HashMap<>();

            initializeData();

            expandableAdapter = new ExpandableAdapter(categoryItemMap);
            expandableListView.setAdapter(expandableAdapter);
        }

        private void initializeData() {
            // Adding items for each category
            List<String> koreanItems = new ArrayList<>();
            koreanItems.add("본죽");
            koreanItems.add("김밥천국");
            koreanItems.add("오봉집");

            List<String> chineseItems = new ArrayList<>();
            chineseItems.add("홍콩반점");
            chineseItems.add("오한수우육면가");
            chineseItems.add("8월의 중식");
            chineseItems.add("춘리마라탕");

            List<String> westernItems = new ArrayList<>();
            westernItems.add("영냠취향");
            westernItems.add("양식구옥");
            westernItems.add("수지앤파스타");
            westernItems.add("프랭크버거");
            westernItems.add("피자스쿨");

            List<String> dessertItems = new ArrayList<>();
            dessertItems.add("그래그릭");
            dessertItems.add("베스킨라빈스");
            dessertItems.add("와플대학");

            List<String> drinksItems = new ArrayList<>();
            drinksItems.add("메가커피");
            drinksItems.add("이디야");
            drinksItems.add("투썸플레이스");
            drinksItems.add("스타벅스");

            // Mapping categories to their respective items
            categoryItemMap.put("양식", westernItems);
            categoryItemMap.put("음료", drinksItems);
            categoryItemMap.put("한식", koreanItems);
            categoryItemMap.put("중식", chineseItems);
            categoryItemMap.put("디저트", dessertItems);
        }

        // Adapter for ExpandableListView
        public class ExpandableAdapter extends BaseExpandableListAdapter {

            private final HashMap<String, List<String>> categoryItemMap;
            private final List<String> categories;

            public ExpandableAdapter(HashMap<String, List<String>> categoryItemMap) {
                this.categoryItemMap = categoryItemMap;
                this.categories = new ArrayList<>(categoryItemMap.keySet());
            }

            @Override
            public int getGroupCount() {
                return categories.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return categoryItemMap.get(categories.get(groupPosition)).size();
            }

            @Override
            public Object getGroup(int groupPosition) {
                return categories.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return categoryItemMap.get(categories.get(groupPosition)).get(childPosition);
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
                    LayoutInflater inflater = LayoutInflater.from(com.example.peojulgae.MainActivity.this);
                    convertView = inflater.inflate(R.layout.group_item, parent, false);
                }
                TextView textView = convertView.findViewById(R.id.groupTextView);
                textView.setText(categoryTitle);
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                String itemName = (String) getChild(groupPosition, childPosition);
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(com.example.peojulgae.CategoryActivity.this);
                    convertView = inflater.inflate(R.layout.child_list, parent, false);
                }
                TextView textView = convertView.findViewById(R.id.childTextView);
                textView.setText(itemName);
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        }
    }
}
