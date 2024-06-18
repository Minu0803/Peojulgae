package com.example.peojulgae;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CategoryItemsFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";

    private EditText searchEditText;
    private LinearLayout categoryLinearLayout;
    private RecyclerView categoryItemsRecyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private List<Food> filteredFoodList;
    private DatabaseReference dbRef;
    private String currentCategory;

    public CategoryItemsFragment() {
        // 기본 생성자 필요
    }

    public static CategoryItemsFragment newInstance(String category) {
        CategoryItemsFragment fragment = new CategoryItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentCategory = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_items, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        categoryLinearLayout = view.findViewById(R.id.categoryLinearLayout);
        categoryItemsRecyclerView = view.findViewById(R.id.categoryItemsRecyclerView);
        categoryItemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        foodList = new ArrayList<>();
        filteredFoodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(filteredFoodList);

        // 어댑터를 RecyclerView에 설정
        categoryItemsRecyclerView.setAdapter(foodAdapter);

        dbRef = FirebaseDatabase.getInstance().getReference("Categories").child(currentCategory);

        loadFoods();
        setupSearch();
        setupCategories();

        return view;
    }

    private void loadFoods() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    Food food = foodSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
                filteredFoodList.clear();
                filteredFoodList.addAll(foodList);
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void filter(String text) {
        filteredFoodList.clear();
        for (Food food : foodList) {
            if (food.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredFoodList.add(food);
            }
        }
        foodAdapter.notifyDataSetChanged();
    }

    private void setupCategories() {
        String[] categories = {"피자", "치킨", "디저트", "음료", "분식", "일식", "중식", "한식"};
        for (final String category : categories) {
            TextView textView = new TextView(getContext());
            textView.setText(category);
            textView.setPadding(16, 8, 16, 8);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openCategoryFragment(category);
                }
            });
            categoryLinearLayout.addView(textView);
        }
    }

    private void openCategoryFragment(String category) {
        Fragment fragment = CategoryItemsFragment.newInstance(category);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}
