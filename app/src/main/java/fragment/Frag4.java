package fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.peojulgae.R;
import com.example.peojulgae.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class Frag4 extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private View view;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button loginButton, registerButton, logoutButton;
    private EditText emailEditText, passwordEditText;
    private ImageView profileImageView;
    private LinearLayout loginLayout, buyerLayout, sellerLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mypagefragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        profileImageView = view.findViewById(R.id.profile_ImageView);
        loginButton = view.findViewById(R.id.login_Button);
        registerButton = view.findViewById(R.id.register_Button);
        emailEditText = view.findViewById(R.id.email_edittext);
        passwordEditText = view.findViewById(R.id.password_edittext);
        logoutButton = view.findViewById(R.id.logout_Button);
        loginLayout = view.findViewById(R.id.login_layout);
        buyerLayout = view.findViewById(R.id.buyer_layout);
        sellerLayout = view.findViewById(R.id.seller_layout);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //사용자가 로그인이 안되어있을때 로그인 화면 보여줌
        if (currentUser == null) {
            showLoginLayout();
            //로그인이 되어있으면 역할에 맞게 레이아웃 보여주기
        } else {
            showUserLayout(currentUser);
        }

        return view;
    }

    private void showLoginLayout() {
        loginLayout.setVisibility(View.VISIBLE);
        profileImageView.setVisibility(View.GONE);
        buyerLayout.setVisibility(View.GONE);
        sellerLayout.setVisibility(View.GONE);
        logoutButton.setVisibility(View.GONE);

        loginButton.setOnClickListener(v -> loginUser());

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
    }

    private void showUserLayout(FirebaseUser currentUser) {
        loginLayout.setVisibility(View.GONE);
        logoutButton.setVisibility(View.VISIBLE);
        profileImageView.setVisibility(View.VISIBLE);

        checkUserRole(currentUser.getUid());

        profileImageView.setOnClickListener(v -> openFileChooser());

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            showLoginLayout();
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    showUserLayout(user);
                }
            } else {
                Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRole(String userId) {
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);
                    if ("buyer".equals(role)) {
                        showBuyerUI();
                    } else if ("seller".equals(role)) {
                        showSellerUI();
                    }
                } else {
                    Toast.makeText(getActivity(), "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "데이터베이스 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBuyerUI() {
        buyerLayout.setVisibility(View.VISIBLE);
        sellerLayout.setVisibility(View.GONE);
        // 추가적으로 구매자용 UI 초기화
        //ex) loadPurchasedItems();
        //ex) loadFavoriteItems();
    }

    private void showSellerUI() {
        buyerLayout.setVisibility(View.GONE);
        sellerLayout.setVisibility(View.VISIBLE);
        // 추가적으로 판매자용 UI 초기화
        //ex)loadSoldItems();
    }

    private void loadPurchasedItems() {
        // ex) Firebase에서 구매한 상품 목록 로드

    }

    private void loadFavoriteItems() {
        // ex) Firebase에서 즐겨찾기 목록 로드

    }

    private void loadSoldItems() {
        // ex) Firebase에서 판매한 상품 목록 로드

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
