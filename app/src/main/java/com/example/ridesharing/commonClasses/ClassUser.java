package com.example.ridesharing.commonClasses;

public class ClassUser {
    private String user_id;
    private String avatar;
    private String name;
    private String phone;
    private String passport;
    private String email;

    public ClassUser(String user_id, String avatar, String name, String phone, String passport, String email) {
        this.user_id = user_id;
        this.avatar = avatar;
        this.name = name;
        this.phone = phone;
        this.passport = passport;
        this.email = email;
    }

    public ClassUser() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
