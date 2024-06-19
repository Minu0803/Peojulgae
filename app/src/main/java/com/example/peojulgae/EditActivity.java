package com.example.peojulgae;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity {

    private EditText editFoodName, editFoodDescription, editFoodPrice, editFoodDiscount, editFoodQuantity;
    private ImageView editFoodImageUrl;
    private Button saveButton;
    private DatabaseReference dbRef;
    private String foodId;
    private Food food; // 클래스 변수로 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editFoodName = findViewById(R.id.edit_food_name);
        editFoodDescription = findViewById(R.id.edit_food_description);
        editFoodPrice = findViewById(R.id.edit_food_price);
        editFoodDiscount = findViewById(R.id.edit_food_discount);
        editFoodQuantity = findViewById(R.id.edit_food_quantity);
        editFoodImageUrl = findViewById(R.id.edit_food_image);
        saveButton = findViewById(R.id.btn_save);

        foodId = getIntent().getStringExtra("foodId");

        if (foodId == null || foodId.isEmpty()) {
            Toast.makeText(this, "음식 ID가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Foods").child(foodId);

        loadFoodDetails();

        saveButton.setOnClickListener(v -> saveFoodDetails());
    }

    private void loadFoodDetails() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                food = snapshot.getValue(Food.class); // 클래스 변수에 저장
                if (food != null) {
                    editFoodName.setText(food.getName());
                    editFoodDescription.setText(food.getDescription());
                    editFoodPrice.setText(food.getPrice());
                    editFoodDiscount.setText(food.getDiscount());
                    editFoodQuantity.setText(String.valueOf(food.getQuantity()));
                    Glide.with(EditActivity.this).load(food.getImageUrl()).into(editFoodImageUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditActivity.this, "데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFoodDetails() {
        String name = editFoodName.getText().toString().trim();
        String description = editFoodDescription.getText().toString().trim();
        String price = editFoodPrice.getText().toString().trim();
        String discount = editFoodDiscount.getText().toString().trim();
        String quantity = editFoodQuantity.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price) ||
                TextUtils.isEmpty(discount) || TextUtils.isEmpty(quantity)) {
            Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Food updatedFood = new Food(foodId, name, description, price, discount, Integer.parseInt(quantity), food.getImageUrl(), food.getCategories());

        dbRef.setValue(updatedFood).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "음식 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // Result OK to indicate the change
                finish(); // 액티비티 종료하여 이전 화면으로 돌아감
            } else {
                Toast.makeText(this, "음식 정보 수정 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
