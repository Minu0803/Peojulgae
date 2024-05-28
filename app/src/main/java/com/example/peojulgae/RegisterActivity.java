package com.example.peojulgae;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.peojulgae.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, userIdEditText, businessNumberEditText;
    private CheckBox businessCheckBox;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userIdEditText = findViewById(R.id.userIdEditText);
        businessNumberEditText = findViewById(R.id.businessNumberEditText);
        businessCheckBox = findViewById(R.id.businessCheckBox);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        businessCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                businessNumberEditText.setVisibility(View.VISIBLE);
            } else {
                businessNumberEditText.setVisibility(View.GONE);
            }
        });

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String userId = userIdEditText.getText().toString().trim();
        String businessNumber = businessNumberEditText.getText().toString().trim();
        String role = businessCheckBox.isChecked() ? "seller" : "buyer";

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (businessCheckBox.isChecked() && TextUtils.isEmpty(businessNumber)) {
            Toast.makeText(this, "사업자 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String userUid = user.getUid();
                    User newUser = new User(email, userUid, businessNumber, role);
                    mDatabase.child(userUid).setValue(newUser).addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "사용자 데이터 저장 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "사용자 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
