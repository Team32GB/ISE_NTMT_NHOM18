package com.example.team32gb.jobit.Model.UnActiveUser;

import static com.example.team32gb.jobit.Utility.Config.IS_JOB_SEEKER;

public class UnactiveUserModel {
    String name;
    String id;
    String dateUnactive;
    int typeUser;
    //    type user: Congig.java
//    public static final int IS_JOB_SEEKER = 1;
//    public static final int IS_RECRUITER = 2;
//    public static final int IS_ADMIN = 3;

    public UnactiveUserModel() {
        typeUser = IS_JOB_SEEKER;
    }

    public UnactiveUserModel(String name, String id, String dateUnactive, int typeUser) {
        this.name = name;
        this.id = id;
        this.dateUnactive = dateUnactive;
        this.typeUser = typeUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateUnactive() {
        return dateUnactive;
    }

    public void setDateUnactive(String dateUnactive) {
        this.dateUnactive = dateUnactive;
    }

    public int getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(int typeUser) {
        this.typeUser = typeUser;
    }


}
