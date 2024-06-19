package com.example.peojulgae;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditproductActivity extends AppCompatActivity {

    private LinearLayout productListLayout;
    private List<Food> productList;
    private DatabaseReference dbRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editproduct);

        productListLayout = findViewById(R.id.productListLayout);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        productList = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference("Foods");
        loadProducts();
    }

    private void loadProducts() {
        dbRef.orderByChild("userId").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                productListLayout.removeAllViews();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Food food = productSnapshot.getValue(Food.class);
                    if (food != null) {
                        productList.add(food);
                        addProductView(food, productSnapshot.getKey()); // pass the foodId
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditproductActivity.this, "데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProductView(Food food, String foodId) {
        View productView = LayoutInflater.from(this).inflate(R.layout.item_food_edit, productListLayout, false);

        TextView foodName = productView.findViewById(R.id.food_name);
        TextView foodDescription = productView.findViewById(R.id.food_description);
        TextView foodPrice = productView.findViewById(R.id.food_price);
        TextView foodDiscount = productView.findViewById(R.id.food_discount);
        TextView foodQuantity = productView.findViewById(R.id.food_quantity);
        ImageView foodImage = productView.findViewById(R.id.food_image);
        Button editButton = productView.findViewById(R.id.edit_button);
        Button deleteButton = productView.findViewById(R.id.delete_button);

        foodName.setText(food.getName());
        foodDescription.setText(food.getDescription());
        foodPrice.setText("가격: " + food.getPrice());
        foodDiscount.setText("할인율: " + food.getDiscount() + "%");
        foodQuantity.setText("수량: " + food.getQuantity());

        Glide.with(this).load(food.getImageUrl()).into(foodImage);

        editButton.setOnClickListener(v -> openEditActivity(foodId));
        deleteButton.setOnClickListener(v -> deleteFood(foodId));

        productListLayout.addView(productView);
    }

    private void openEditActivity(String foodId) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("foodId", foodId);
        startActivity(intent);
    }

    private void deleteFood(String foodId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Foods").child(foodId);
        dbRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "음식이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                loadProducts(); // 삭제 후 목록 갱신
            } else {
                Toast.makeText(this, "음식 삭제 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
