package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MartSpecific extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.martspecific);

        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.food_ltext01);
        TextView priceView = findViewById(R.id.food_ltext04);

        // 인텐트에서 이미지 리소스 ID, 텍스트, 가격을 가져옴
        Intent intent = getIntent();
        int imageResource = intent.getIntExtra("image_resource", R.drawable.peach);
        String itemName = intent.getStringExtra("item_name");
        String itemPrice = intent.getStringExtra("item_price");

        // ImageView, TextView, PriceView에 설정
        imageView.setImageResource(imageResource);
        textView.setText(itemName);
        priceView.setText(itemPrice);
    }
}
