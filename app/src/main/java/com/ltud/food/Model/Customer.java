package com.ltud.food.Model;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Customer implements Serializable {

    private String id;
    private String account;
    private String name;
    private String address;
    private String birthday;
    private String gender;
    private String avatar;

    public Customer() {
    }

    public Customer(String id, String account, String name, String address, String birthday, String gender, String avatar) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getAvatar() {
        return avatar;
    }
}
