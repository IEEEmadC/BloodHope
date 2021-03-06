package com.kantutapp.bloodhope.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    String key;
    String name;
    String photo;
    String email;
    String phone_number;
    String blood_type;
    int number_donations;


    public User() {
    }

    public User(String key, String name, String photo, String phone_number, String blood_type, int number_donations, String email) {
        this.key = key;
        this.name = name;
        this.photo = photo;
        this.phone_number = phone_number;
        this.blood_type = blood_type;
        this.number_donations = number_donations;
        this.email= email;
    }

    protected User(Parcel in) {
        key = in.readString();
        name = in.readString();
        photo = in.readString();
        phone_number = in.readString();
        blood_type = in.readString();
        number_donations= in.readInt();
        email= in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public int getNumber_donations() {
        return number_donations;
    }

    public void setNumber_donations(int number_donations) {
        this.number_donations = number_donations;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(phone_number);
        dest.writeString(blood_type);
        dest.writeInt(number_donations);
        dest.writeString(email);
    }
}
