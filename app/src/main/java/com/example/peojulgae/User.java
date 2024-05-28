package com.example.peojulgae;

public class User {
    public String email;
    public String userId;
    public String businessNumber;


    public String role;// 사용자 역할

    //기본 생성자(Firebase Realtime Database에서 데이터를 읽거나 쓸 때 필요)
    public User() {

    }



    //모든 필드 초기화하는 생성자
    public User(String email, String userId, String businessNumber,String role) {
        this.email = email;
        this.userId = userId;
        this.businessNumber = businessNumber;
        this.role = role;
    }
}
