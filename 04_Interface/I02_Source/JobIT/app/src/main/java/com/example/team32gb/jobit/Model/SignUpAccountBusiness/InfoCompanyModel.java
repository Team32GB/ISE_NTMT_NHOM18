package com.example.team32gb.jobit.Model.SignUpAccountBusiness;

import android.os.Parcel;
import android.os.Parcelable;

import static com.example.team32gb.jobit.Utility.Config.NOT_APPROVAL;

public class InfoCompanyModel implements Parcelable {
    String name;
    String type;
    String size;
    String avatar;
    Long approvalMode;

    protected InfoCompanyModel(Parcel in) {
        name = in.readString();
        type = in.readString();
        size = in.readString();
        avatar = in.readString();
        if (in.readByte() == 0) {
            approvalMode = null;
        } else {
            approvalMode = in.readLong();
        }
        address = in.readString();
        province = in.readString();
        introduce = in.readString();
        namePresenter = in.readString();
        phoneNumberPresenter = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(size);
        dest.writeString(avatar);
        if (approvalMode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(approvalMode);
        }
        dest.writeString(address);
        dest.writeString(province);
        dest.writeString(introduce);
        dest.writeString(namePresenter);
        dest.writeString(phoneNumberPresenter);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InfoCompanyModel> CREATOR = new Creator<InfoCompanyModel>() {
        @Override
        public InfoCompanyModel createFromParcel(Parcel in) {
            return new InfoCompanyModel(in);
        }

        @Override
        public InfoCompanyModel[] newArray(int size) {
            return new InfoCompanyModel[size];
        }
    };

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    String address;
    String province;
    String introduce;
    String namePresenter;
    String phoneNumberPresenter;

    public InfoCompanyModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getNamePresenter() {
        return namePresenter;
    }

    public void setNamePresenter(String namePresenter) {
        this.namePresenter = namePresenter;
    }

    public String getPhoneNumberPresenter() {
        return phoneNumberPresenter;
    }

    public void setPhoneNumberPresenter(String phoneNumberPresenter) {
        this.phoneNumberPresenter = phoneNumberPresenter;
    }

}
