package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.peojulgae.Category;
import com.example.peojulgae.CategoryAdapter;
import com.example.peojulgae.CategoryItemsFragment;
import com.example.peojulgae.ChickenFragment;
import com.example.peojulgae.ChineseFragment;
import com.example.peojulgae.DessertFragment;
import com.example.peojulgae.DrinkFragment;
import com.example.peojulgae.JapaneseFragment;
import com.example.peojulgae.KoreanFragment;
import com.example.peojulgae.PizzaFragment;
import com.example.peojulgae.R;
import com.example.peojulgae.SnackFragment;

import java.util.ArrayList;
import java.util.List;

public class Frag2 extends Fragment {

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categoryfragment, container, false);

        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryList = new ArrayList<>();
        categoryList.add(new Category("피자"));
        categoryList.add(new Category("치킨"));
        categoryList.add(new Category("디저트"));
        categoryList.add(new Category("음료"));
        categoryList.add(new Category("분식"));
        categoryList.add(new Category("일식"));
        categoryList.add(new Category("중식"));
        categoryList.add(new Category("한식"));

        categoryAdapter = new CategoryAdapter(categoryList, getContext(), category -> openCategoryFragment(category.getName()));
        categoryRecyclerView.setAdapter(categoryAdapter);

        return view;
    }

    private void openCategoryFragment(String categoryName) {
        Fragment fragment;
        switch (categoryName) {
            case "피자":
                fragment = new PizzaFragment();
                break;
            case "치킨":
                fragment = new ChickenFragment();
                break;
            case "디저트":
                fragment = new DessertFragment();
                break;
            case "음료":
                fragment = new DrinkFragment();
                break;
            case "분식":
                fragment = new SnackFragment();
                break;
            case "일식":
                fragment = new JapaneseFragment();
                break;
            case "중식":
                fragment = new ChineseFragment();
                break;
            case "한식":
                fragment = new KoreanFragment();
                break;
            default:
                fragment = new CategoryItemsFragment(); // 기본 프래그먼트 설정
                break;
        }
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}
