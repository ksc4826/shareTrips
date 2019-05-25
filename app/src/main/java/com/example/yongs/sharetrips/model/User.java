package com.example.yongs.sharetrips.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class User implements Parcelable {

    private String id;
    private String password;
    private String username;
    private String email;
    private String theme;
    private String country;

    public User(){
        this.id = null;
        this.password = null;
        this.username = null;
        this.email = null;
        this.theme = null;
        this.country = null;
    }

    public User(String id, String password, String username, String email, String theme, String country){
        this.id = id;
        this.password = password;
        this.username = username;
        this.email = email;
        this.theme = theme;
        this.country = country;
    }

    public User(HashMap<String, Object> parameters){
        this.id = (String) parameters.get("id");
        this.password = (String) parameters.get("password");
        this.username = (String) parameters.get("username");
        this.email = (String) parameters.get("email");
        this.theme = (String) parameters.get("theme");
        this.country = (String) parameters.get("country");
    }

    protected User(Parcel in) {
        id = in.readString();
        password = in.readString();
        username = in.readString();
        email = in.readString();
        theme = in.readString();
        country = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) { this.theme = theme; }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean checkInput(){
        if(getId()==null
                ||getPassword()==null
                ||getUsername()==null
                ||getEmail()==null)
            return false;

        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(password);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(theme);
        dest.writeString(country);
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
}
