package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.peojulgae.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Frag2 extends Fragment {

    private View view;
    private ExpandableListView expandableListView;
    private ExpandableAdapter expandableAdapter;
    private HashMap<String, List<String>> categoryItemMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_activity, container, false);

        expandableListView = view.findViewById(R.id.eListView);
        categoryItemMap = new HashMap<>();

        initializeData();

        expandableAdapter = new ExpandableAdapter(categoryItemMap);
        expandableListView.setAdapter(expandableAdapter);

        return view;
    }

    private void initializeData() {
        // Adding items for each category
        List<String> koreanItems = new ArrayList<>();
        koreanItems.add("본죽");
        koreanItems.add("김밥천국");

        List<String> chineseItems = new ArrayList<>();
        chineseItems.add("홍콩반점");
        chineseItems.add("오한수우육면가");
        chineseItems.add("8월의 중식");
        chineseItems.add("우동");
        chineseItems.add("춘리마라탕");

        List<String> westernItems = new ArrayList<>();
        westernItems.add("스테이크");

        List<String> dessertItems = new ArrayList<>();
        // Add dessert items if any

        List<String> drinksItems = new ArrayList<>();
        // Add drink items if any

        // Mapping categories to their respective items
        categoryItemMap.put("한식", koreanItems);
        categoryItemMap.put("중식", chineseItems);
        categoryItemMap.put("양식", westernItems);
        categoryItemMap.put("디저트", dessertItems);
        categoryItemMap.put("음료", drinksItems);
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
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(categoryTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String itemName = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(itemName);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
