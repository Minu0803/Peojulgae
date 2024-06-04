package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.peojulgae.FoodCategoryActivity;
import com.example.peojulgae.R;

public class Frag3 extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_grocery_store, container, false);

        ImageView groceryStoreImg = view.findViewById(R.id.grocery_store_img);
        ImageView groceryStoreImg2 = view.findViewById(R.id.grocery_store_img02);
        ImageView groceryStoreImg3 = view.findViewById(R.id.grocery_store_img03);
        groceryStoreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodCategoryActivity.class);
                startActivity(intent);
            }
        });

        groceryStoreImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodCategoryActivity.class);
                startActivity(intent);
            }
        });

        groceryStoreImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodCategoryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
