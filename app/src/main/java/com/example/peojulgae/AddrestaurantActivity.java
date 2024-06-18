package com.example.peojulgae;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddrestaurantActivity extends AppCompatActivity {

    private static final int SELECT_PHOTOS_REQUEST_CODE = 1;

    private EditText ownerName, businessName, restaurantAddress, restaurantDescription, phoneNumber;
    private TextView selectedPhotosText, openingHoursText;
    private Button selectPhotosButton, registerRestaurantButton, selectOpeningHoursButton;

    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private List<Uri> photoUris;
    private String openingTime, closingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrestaurant);

        ownerName = findViewById(R.id.ownerName);
        businessName = findViewById(R.id.businessName);
        restaurantAddress = findViewById(R.id.restaurantAddress);
        restaurantDescription = findViewById(R.id.restaurantDescription);
        phoneNumber = findViewById(R.id.phoneNumber);
        selectedPhotosText = findViewById(R.id.selectedPhotosText);
        openingHoursText = findViewById(R.id.openingHoursText);
        selectPhotosButton = findViewById(R.id.selectPhotosButton);
        registerRestaurantButton = findViewById(R.id.registerRestaurantButton);
        selectOpeningHoursButton = findViewById(R.id.selectOpeningHoursButton);

        dbRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        photoUris = new ArrayList<>();

        checkExistingRestaurant();

        selectPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotos();
            }
        });

        selectOpeningHoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOpeningHours();
            }
        });

        registerRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerRestaurant();
            }
        });
    }

    private void checkExistingRestaurant() {
        String userId = auth.getCurrentUser().getUid();
        dbRef.child("Users").child(userId).child("restaurantId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(AddrestaurantActivity.this, "이미 등록된 음식점이 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddrestaurantActivity.this, "데이터베이스 오류: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectPhotos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "사진 선택"), SELECT_PHOTOS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTOS_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
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

    private void selectOpeningHours() {
        TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                openingTime = String.format("%02d:%02d", hourOfDay, minute);
                selectClosingTime();
            }
        };

        TimePickerDialog startTimePickerDialog = new TimePickerDialog(this, startTimeSetListener, 9, 0, true);
        startTimePickerDialog.setTitle("운영 시작 시간 선택");
        startTimePickerDialog.show();
    }

    private void selectClosingTime() {
        TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                closingTime = String.format("%02d:%02d", hourOfDay, minute);
                openingHoursText.setText("운영시간: " + openingTime + " - " + closingTime);
            }
        };

        TimePickerDialog endTimePickerDialog = new TimePickerDialog(this, endTimeSetListener, 21, 0, true);
        endTimePickerDialog.setTitle("운영 종료 시간 선택");
        endTimePickerDialog.show();
    }

    private void registerRestaurant() {
        String owner = ownerName.getText().toString().trim();
        String business = businessName.getText().toString().trim();
        String address = restaurantAddress.getText().toString().trim();
        String description = restaurantDescription.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(owner) || TextUtils.isEmpty(business) || TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(description) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(openingTime) || TextUtils.isEmpty(closingTime)) {
            Toast.makeText(this, "모든 필드가 필요합니다", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        final String restaurantId = dbRef.child("Restaurants").push().getKey();

        final Map<String, Object> restaurant = new HashMap<>();
        restaurant.put("ownerName", owner);
        restaurant.put("businessName", business);
        restaurant.put("address", address);
        restaurant.put("description", description);
        restaurant.put("phoneNumber", phone);
        restaurant.put("openingHours", openingTime + " - " + closingTime);

        if (photoUris.isEmpty()) {
            // 사진이 없는 경우 바로 데이터베이스에 저장
            dbRef.child("Restaurants").child(restaurantId).setValue(restaurant)
                    .addOnSuccessListener(aVoid -> {
                        // 사용자 정보에 음식점 ID 저장
                        dbRef.child("Users").child(userId).child("restaurantId").setValue(restaurantId);
                        Toast.makeText(AddrestaurantActivity.this, "음식점 등록 완료", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddrestaurantActivity.this, "오류: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            // 사진이 있는 경우 업로드 후 데이터베이스에 저장
            final List<String> photoUrls = new ArrayList<>();
            StorageReference storageRef = storage.getReference().child("restaurant_photos").child(restaurantId);

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
                                            restaurant.put("photoUrls", photoUrls);
                                            dbRef.child("Restaurants").child(restaurantId).setValue(restaurant)
                                                    .addOnSuccessListener(aVoid -> {
                                                        // 사용자 정보에 음식점 ID 저장
                                                        dbRef.child("Users").child(userId).child("restaurantId").setValue(restaurantId);
                                                        Toast.makeText(AddrestaurantActivity.this, "음식점 등록 완료", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> Toast.makeText(AddrestaurantActivity.this, "오류: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                        }
                                    } else {
                                        Toast.makeText(AddrestaurantActivity.this, "사진 URL 가져오기 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AddrestaurantActivity.this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
