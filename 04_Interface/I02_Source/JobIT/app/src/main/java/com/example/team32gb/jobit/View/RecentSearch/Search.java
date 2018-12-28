package com.example.team32gb.jobit.View.RecentSearch;

import java.io.Serializable;

public class Search implements Serializable {
    private String timkiem;
    private String diaDiem;

    public Search(String timkiem, String diaDiem){
        this.timkiem = timkiem;
        this.diaDiem = diaDiem;
    }

    public String getTimkiem() {
        return timkiem;
    }

    public void setTimkiem(String timkiem) {
        this.timkiem = timkiem;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }


}
