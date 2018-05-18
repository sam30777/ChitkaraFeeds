package com.example.android.feedbackchitkara.data;

public class UserReview {
    private String user_review;
    private String user_name;
    private String user_image_url;

    public UserReview(){}

    public UserReview(String user_image_url,String user_name,String user_review){
        this.user_image_url=user_image_url;
        this.user_name=user_name;
        this.user_review=user_review;
    }

    public void setUser_review(String user_review) {
        this.user_review = user_review;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_image_url(String user_image_url) {
        this.user_image_url = user_image_url;
    }

    public String getUser_review() {
        return user_review;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_image_url() {
        return user_image_url;
    }
}
