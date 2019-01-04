package com.example.team32gb.jobit.Model.PostJob;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemPostJob implements Parcelable{
    String idJob;
    String idCompany;
    String nameCompany;
    String timeApplied;
    DataPostJob dataPostJob;
    boolean checked;
    boolean applied;
    boolean isEliminated;

    public boolean isEliminated() {
        return isEliminated;
    }

    public void setEliminated(boolean eliminated) {
        isEliminated = eliminated;
    }



    public ItemPostJob() {
        checked = false;
        applied = false;
        isEliminated = false;
    }


    protected ItemPostJob(Parcel in) {
        idJob = in.readString();
        idCompany = in.readString();
        nameCompany = in.readString();
        timeApplied = in.readString();
        dataPostJob = in.readParcelable(DataPostJob.class.getClassLoader());
        checked = in.readByte() != 0;
        applied = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idJob);
        dest.writeString(idCompany);
        dest.writeString(nameCompany);
        dest.writeString(timeApplied);
        dest.writeParcelable(dataPostJob, flags);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeByte((byte) (applied ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemPostJob> CREATOR = new Creator<ItemPostJob>() {
        @Override
        public ItemPostJob createFromParcel(Parcel in) {
            return new ItemPostJob(in);
        }

        @Override
        public ItemPostJob[] newArray(int size) {
            return new ItemPostJob[size];
        }
    };

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTimeApplied() {
        return timeApplied;
    }

    public void setTimeApplied(String timeApplied) {
        this.timeApplied = timeApplied;
    }


    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }

    public DataPostJob getDataPostJob() {
        return dataPostJob;
    }

    public void setDataPostJob(DataPostJob dataPostJob) {
        this.dataPostJob = dataPostJob;
    }


    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
    }
}
