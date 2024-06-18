package com.example.peojulgae;

public class Food {
    private String name;
    private String description;
    private String price;
    private String discount;
    private String imageUrl;
    private int quantity;
    private String mainImageUrl;

    public Food() {
        // Firebase를 위한 기본 생성자
    }

    public Food(String name, String description, String price, String discount, String imageUrl, int quantity, String mainImageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.mainImageUrl = mainImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }
}
