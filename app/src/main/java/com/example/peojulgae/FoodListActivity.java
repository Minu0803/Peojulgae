package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FoodListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);

        // 인텐트에서 이미지 리소스 ID와 음식 이름을 가져옴
        Intent intent = getIntent();
        int imageResource = intent.getIntExtra("image_resource", R.drawable.store_gana);
        String foodName = intent.getStringExtra("food_name");

        // ImageView 설정
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(imageResource);

        // TextView 설정
        TextView foodNameTextView = findViewById(R.id.food_ltext01);
        foodNameTextView.setText(foodName);

        // food_lbutton04 버튼 찾기
        Button TakePayButton = findViewById(R.id.food_lbutton04);
        Button HavePayButton = findViewById(R.id.food_lbutton05);

        // food_lbutton04 버튼 클릭 리스너 설정
        TakePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TakePayActivity 시작
                Intent intent1 = new Intent(FoodListActivity.this, TakePayActivity.class);
                startActivity(intent1);
            }
        });

        HavePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HavePayActivity 시작
                Intent intent2 = new Intent(FoodListActivity.this, HavePayActivity.class);
                startActivity(intent2);
            }
        });
    }
}
