package com.example.android.feedbackchitkara.data;

public class Rating {
    private Integer user_count;
    private Float rating_sum;
   public Rating(){}
   public Rating(Integer a,Float b){
        this.user_count=a;
        this.rating_sum=b;
    }

    public Integer getUser_count() {
        return user_count;
    }

    public Float getRating_sum() {
        return rating_sum;
    }

    public void setUser_count(Integer user_count) {
        this.user_count = user_count;
    }

    public void setRating_sum(Float rating_sum) {
        this.rating_sum = rating_sum;
    }
}
