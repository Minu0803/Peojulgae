package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.peojulgae.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Frag1 extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefragment, container, false);

        // FirebaseAuth 인스턴스 가져오기
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // TextView 가져오기
        TextView welcomeTextView = view.findViewById(R.id.textView);

        // 로그인된 사용자 정보 가져와서 TextView 업데이트
        if (currentUser != null) {
            String userId = currentUser.getEmail();
            welcomeTextView.setText(userId+ "님 환영합니다");
        } else {
            welcomeTextView.setText("로그인된 사용자가 없습니다.");
        }

        return view;
    }
}
