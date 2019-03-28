package com.example.nigirifallsapp;

public class Review {

    private String orderId;
    private String stars;
    private String comment;

    public Review(String reviewString) {
        String[] elementsInReview = reviewString.split("\\|");
        this.orderId = elementsInReview[0];
        this.stars = elementsInReview[1];
        this.comment = elementsInReview[2];
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getStars() {
        return this.stars;
    }

    public String getComment() {
        return this.comment;
    }
}