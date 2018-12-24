package com.example.team32gb.jobit.Model.PostJob;

import android.os.Parcel;
import android.os.Parcelable;

public class DataPostJob implements Parcelable {
    String nameJob;
    String typeJob;
    String each;
    String maxSalary;
    String minSalary;
    String numberEmployer;
    String description;
    String qualification;
    String time;
    String idJob;
    String idCompany;

    protected DataPostJob(Parcel in) {
        nameJob = in.readString();
        typeJob = in.readString();
        each = in.readString();
        maxSalary = in.readString();
        minSalary = in.readString();
        numberEmployer = in.readString();
        description = in.readString();
        qualification = in.readString();
        time = in.readString();
        idJob = in.readString();
        idCompany = in.readString();
        nameCompany = in.readString();
        province = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameJob);
        dest.writeString(typeJob);
        dest.writeString(each);
        dest.writeString(maxSalary);
        dest.writeString(minSalary);
        dest.writeString(numberEmployer);
        dest.writeString(description);
        dest.writeString(qualification);
        dest.writeString(time);
        dest.writeString(idJob);
        dest.writeString(idCompany);
        dest.writeString(nameCompany);
        dest.writeString(province);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataPostJob> CREATOR = new Creator<DataPostJob>() {
        @Override
        public DataPostJob createFromParcel(Parcel in) {
            return new DataPostJob(in);
        }

        @Override
        public DataPostJob[] newArray(int size) {
            return new DataPostJob[size];
        }
    };

    public String getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    String nameCompany;
    String province;

    public DataPostJob() {
    }

    public String getEach() {
        return each;
    }

    public void setEach(String each) {
        this.each = each;
    }

    public DataPostJob(String nameCompany, String nameJob, String typeJob, String each, String maxSalary, String minSalary, String numberEmployer, String description, String qualification, String time) {
        this.nameJob = nameJob;
        this.typeJob = typeJob;
        this.each = each;
        this.maxSalary = maxSalary;
        this.minSalary = minSalary;
        this.numberEmployer = numberEmployer;
        this.description = description;
        this.qualification = qualification;
        this.time = time;
    }

    public String getNameJob() {
        return nameJob;
    }

    public void setNameJob(String nameJob) {
        this.nameJob = nameJob;
    }

    public String getTypeJob() {
        return typeJob;
    }

    public void setTypeJob(String typeJob) {
        this.typeJob = typeJob;
    }

    public String getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(String maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(String minSalary) {
        this.minSalary = minSalary;
    }

    public String getNumberEmployer() {
        return numberEmployer;
    }

    public void setNumberEmployer(String numberEmployer) {
        this.numberEmployer = numberEmployer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

