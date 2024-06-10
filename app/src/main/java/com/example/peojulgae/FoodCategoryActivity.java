package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class FoodCategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_category);

        ImageView peachImageView = findViewById(R.id.peachimage);
        ImageView appleImageView = findViewById(R.id.honeyapple);
        ImageView watermelonImageView = findViewById(R.id.watermelonimage);
        ImageView kiwiImageView = findViewById(R.id.kiwiimage);
        ImageView shinemuscatImageView = findViewById(R.id.shinemuscatimage);
        ImageView mangoImageView = findViewById(R.id.mangoimage);
        // 더 많은 ImageView 변수가 필요한 경우 추가

        peachImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodCategoryActivity.this, MartSpecific.class);
                intent.putExtra("image_resource", R.drawable.peach);
                intent.putExtra("item_name", "납작 복숭아");
                intent.putExtra("item_price", "34,120원");
                startActivity(intent);
            }
        });

        appleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodCategoryActivity.this, MartSpecific.class);
                intent.putExtra("image_resource", R.drawable.apple);
                intent.putExtra("item_name", "꿀 사과");
                intent.putExtra("item_price", "24,320원");
                startActivity(intent);
            }
        });

        watermelonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodCategoryActivity.this, MartSpecific.class);
                intent.putExtra("image_resource", R.drawable.watermelon);
                intent.putExtra("item_name", "수박");
                intent.putExtra("item_price", "40,00원");
                startActivity(intent);
            }
        });
        kiwiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodCategoryActivity.this, MartSpecific.class);
                intent.putExtra("image_resource", R.drawable.kiwi);
                intent.putExtra("item_name", "키위");
                intent.putExtra("item_price", "25,00원");
                startActivity(intent);
            }
        });

        shinemuscatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodCategoryActivity.this, MartSpecific.class);
                intent.putExtra("image_resource", R.drawable.shine_muscat);
                intent.putExtra("item_name", "샤인머스캣");
                intent.putExtra("item_price", "40,00원");
                startActivity(intent);
            }
        });

        mangoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodCategoryActivity.this, MartSpecific.class);
                intent.putExtra("image_resource", R.drawable.mango);
                intent.putExtra("item_name", "망고");
                intent.putExtra("item_price", "24,320원");
                startActivity(intent);
            }
        });

        // 더 많은 onClickListeners를 추가
    }
}
