package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.oauth.view.NidOAuthLoginButton;

import fragment.Frag1;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, signupEditText, forgotpasswordEditText;
    private ImageView loginclick;
    private CheckBox autoLoginCheckBox;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private NidOAuthLoginButton naverLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emailEditText = findViewById(R.id.email_text);
        passwordEditText = findViewById(R.id.password_text);
        loginclick = findViewById(R.id.login_click);
        signupEditText = findViewById(R.id.sign_up);
        autoLoginCheckBox = findViewById(R.id.auto_login);
        naverLoginButton = findViewById(R.id.naver_login_button);

        // 네이버 로그인 SDK 초기화
        NaverIdLoginSDK.INSTANCE.initialize(this, getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret), getString(R.string.app_name));

        loginclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signupEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        naverLoginButton.setOAuthLogin(new OAuthLoginCallback() {
            @Override
            public void onSuccess() {
                // 로그인 성공 시 액세스 토큰 가져오기
                String accessToken = NaverIdLoginSDK.INSTANCE.getAccessToken();
                Log.d("NaverLogin", "AccessToken: " + accessToken);
                Toast.makeText(LoginActivity.this, "네이버 로그인 성공", Toast.LENGTH_SHORT).show();
                // 추가 작업: 액세스 토큰을 사용해 사용자 정보 가져오기
            }

            @Override
            public void onFailure(int httpStatus, @NonNull String message) {
                Log.e("NaverLogin", "onFailure: httpStatus - " + httpStatus + " / message - " + message);
            }

            @Override
            public void onError(int errorCode, @NonNull String message) {
                Log.e("NaverLogin", "onError: errorCode - " + errorCode + " / message - " + message);
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                fetchUserName(user.getUid());
                            } else {
                                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchUserName(String userId) {
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    Intent intent = new Intent(LoginActivity.this, Frag1.class);
                    intent.putExtra("userID", name);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
