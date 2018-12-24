package com.example.team32gb.jobit.View.Evaluation;

public class Comment {
    private String title;
    private float star;
    private String date;
    private String comment;

    public Comment(String title, float star, String date, String comment) {
        this.title = title;
        this.star = star;
        this.date = date;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
