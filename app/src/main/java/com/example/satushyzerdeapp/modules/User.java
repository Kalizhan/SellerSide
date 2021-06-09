package com.example.satushyzerdeapp.modules;

public class User {
    String fullName, phone, email, password, ruqsat, imgUri;

    public User() {}

    public User(String fullName, String phone, String email, String password, String ruqsat, String imgUri) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.ruqsat = ruqsat;
        this.imgUri = imgUri;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuqsat() {
        return ruqsat;
    }

    public void setRuqsat(String ruqsat) {
        this.ruqsat = ruqsat;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
}
