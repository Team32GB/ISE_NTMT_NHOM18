package com.example.team32gb.jobit.View.Evaluation;

import java.util.ArrayList;

public class RatingModel {
    String comment;
    String title;
    String dateTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    double ratingAverage;
    double rating1;
    double rating2;
    double rating3;
    double rating4;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public double getRating1() {
        return rating1;
    }

    public void setRating1(double rating1) {
        this.rating1 = rating1;
    }

    public double getRating2() {
        return rating2;
    }

    public void setRating2(double rating2) {
        this.rating2 = rating2;
    }

    public double getRating3() {
        return rating3;
    }

    public void setRating3(double rating3) {
        this.rating3 = rating3;
    }

    public double getRating4() {
        return rating4;
    }

    public void setRating4(double rating4) {
        this.rating4 = rating4;
    }
}
