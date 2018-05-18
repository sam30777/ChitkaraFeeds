package com.example.android.feedbackchitkara.data;

import java.util.MissingFormatArgumentException;

public class MainData {
    private String title_name;
    private Integer image_id;
    private Integer number_of_reviews;
    public MainData(){}
    public MainData(String topic,Integer image_id,Integer number_of_reviews){
        this.title_name=topic;
        this.image_id=image_id;
        this.number_of_reviews=number_of_reviews;
    }
    public String getTitle_name() {
        return title_name;
    }

    public Integer getImage_id() {
        return image_id;
    }

    public Integer getNumber_of_reviews() {
        return number_of_reviews;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public void setImage_id(Integer image_id) {
        this.image_id = image_id;
    }

    public void setNumber_of_reviews(Integer number_of_reviews) {
        this.number_of_reviews = number_of_reviews;
    }
}
