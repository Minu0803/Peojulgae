package com.example.peojulgae;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddproductActivity extends AppCompatActivity {

    private static final int SELECT_PHOTOS_REQUEST_CODE = 1;
    private static final String TAG = "AddproductActivity";

    private EditText foodName, foodDescription, foodPrice;
    private TextView selectedPhotosText, discountText, quantityText;
    private Button selectPhotosButton, registerFoodButton, selectMainImageButton;
    private SeekBar discountSeekBar, quantitySeekBar;
    private CheckBox categoryPizza, categoryChicken, categoryDessert, categoryDrink, categorySnack, categoryJapanese, categoryChinese, categoryKorean;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private List<Uri> photoUris;
    private int discount = 0;
    private int quantity = 1;
    private Uri mainImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        foodName = findViewById(R.id.foodName);
        foodDescription = findViewById(R.id.foodDescription);
        foodPrice = findViewById(R.id.foodPrice);
        selectedPhotosText = findViewById(R.id.selectedFoodPhotosText);
        discountText = findViewById(R.id.discountText);
        quantityText = findViewById(R.id.quantityText);
        selectPhotosButton = findViewById(R.id.selectFoodPhotosButton);
        registerFoodButton = findViewById(R.id.registerFoodButton);
        selectMainImageButton = findViewById(R.id.selectMainImageButton);
        discountSeekBar = findViewById(R.id.discountSeekBar);
        quantitySeekBar = findViewById(R.id.quantitySeekBar);

        categoryPizza = findViewById(R.id.categoryPizza);
        categoryChicken = findViewById(R.id.categoryChicken);
        categoryDessert = findViewById(R.id.categoryDessert);
        categoryDrink = findViewById(R.id.categoryDrink);
        categorySnack = findViewById(R.id.categorySnack);
        categoryJapanese = findViewById(R.id.categoryJapanese);
        categoryChinese = findViewById(R.id.categoryChinese);
        categoryKorean = findViewById(R.id.categoryKorean);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        photoUris = new ArrayList<>();

        selectPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotos();
            }
        });

        selectMainImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMainImage();
            }
        });

        registerFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFood();
            }
        });

        discountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                discount = progress;
                discountText.setText("할인율: " + discount + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        quantitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quantity = progress + 1;
                quantityText.setText("수량: " + quantity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void selectPhotos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "사진 선택"), SELECT_PHOTOS_REQUEST_CODE);
    }

    private void selectMainImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "메인 이미지 선택"), SELECT_PHOTOS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTOS_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    photoUris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                photoUris.add(data.getData());
            }
            selectedPhotosText.setText(photoUris.size() + "장의 사진 선택됨");
        }
    }

    private void registerFood() {
        Log.d(TAG, "registerFood: Started");
        String name = foodName.getText().toString().trim();
        String description = foodDescription.getText().toString().trim();
        String price = foodPrice.getText().toString().trim();
        String discountString = String.valueOf(discount);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price)) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        final DocumentReference foodRef = db.collection("Foods").document();

        final Map<String, Object> food = new HashMap<>();
        food.put("name", name);
        food.put("description", description);
        food.put("price", price);
        food.put("discount", discountString);
        food.put("quantity", quantity);
        food.put("userId", userId);

        List<String> categories = new ArrayList<>();
        if (categoryPizza.isChecked()) categories.add("피자");
        if (categoryChicken.isChecked()) categories.add("치킨");
        if (categoryDessert.isChecked()) categories.add("디저트");
        if (categoryDrink.isChecked()) categories.add("음료");
        if (categorySnack.isChecked()) categories.add("분식");
        if (categoryJapanese.isChecked()) categories.add("일식");
        if (categoryChinese.isChecked()) categories.add("중식");
        if (categoryKorean.isChecked()) categories.add("한식");
        food.put("categories", categories);

        if (!photoUris.isEmpty()) {
            final List<String> photoUrls = new ArrayList<>();
            StorageReference storageRef = storage.getReference().child("food_photos").child(foodRef.getId());

            for (Uri uri : photoUris) {
                final StorageReference photoRef = storageRef.child(uri.getLastPathSegment());
                photoRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            photoRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        photoUrls.add(task.getResult().toString());
                                        if (photoUrls.size() == photoUris.size()) {
                                            food.put("photoUrls", photoUrls);
                                            if (mainImageUri != null) {
                                                final StorageReference mainImageRef = storageRef.child("main_image");
                                                mainImageRef.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            mainImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Uri> task) {
                                                                    if (task.isSuccessful()) {
                                                                        food.put("mainImageUrl", task.getResult().toString());
                                                                        saveFoodToDatabase(foodRef, food);
                                                                    } else {
                                                                        Toast.makeText(AddproductActivity.this, "메인 이미지 URL 가져오기 실패", Toast.LENGTH_SHORT).show();
                                                                        Log.e(TAG, "메인 이미지 URL 가져오기 실패: " + task.getException().getMessage());
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            Toast.makeText(AddproductActivity.this, "메인 이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                                                            Log.e(TAG, "메인 이미지 업로드 실패: " + task.getException().getMessage());
                                                        }
                                                    }
                                                });
                                            } else {
                                                saveFoodToDatabase(foodRef, food);
                                            }
                                        }
                                    } else {
                                        Toast.makeText(AddproductActivity.this, "사진 URL 가져오기 실패", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "사진 URL 가져오기 실패: " + task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AddproductActivity.this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "사진 업로드 실패: " + task.getException().getMessage());
                        }
                    }
                });
            }
        } else {
            if (mainImageUri != null) {
                StorageReference storageRef = storage.getReference().child("food_photos").child(foodRef.getId());
                final StorageReference mainImageRef = storageRef.child("main_image");
                mainImageRef.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            mainImageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        food.put("mainImageUrl", task.getResult().toString());
                                        saveFoodToDatabase(foodRef, food);
                                    } else {
                                        Toast.makeText(AddproductActivity.this, "메인 이미지 URL 가져오기 실패", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "메인 이미지 URL 가져오기 실패: " + task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AddproductActivity.this, "메인 이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "메인 이미지 업로드 실패: " + task.getException().getMessage());
                        }
                    }
                });
            } else {
                saveFoodToDatabase(foodRef, food);
            }
        }
    }

    private void saveFoodToDatabase(DocumentReference foodRef, Map<String, Object> food) {
        foodRef.set(food)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddproductActivity.this, "음식 등록 완료", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "음식 등록 완료: " + foodRef.getId());
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddproductActivity.this, "오류: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "데이터베이스 저장 실패: " + e.getMessage());
                });
    }
}
