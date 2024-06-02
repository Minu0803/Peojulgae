package com.example.peojulgae;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BaloonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_display);


        CardView cardView = findViewById(R.id.CardView01); // 카드뷰 찾기


        cardView.setOnClickListener(new View.OnClickListener() {  // 카드뷰 클릭 리스너 설정
            @Override
            public void onClick(View v) {
                // FoodListActivity 시작
                Intent intent = new Intent(BaloonActivity.this, StoretoFood.class);
                startActivity(intent);
            }
        });
    }
}

