package com.example.satushyzerdeapp.modules;

public class Satushylar {
    String nameSatushy, surnameSatushy, emailSatushy, passwordSatushy, phoneNumberSatushy;

    public Satushylar(){};

    public Satushylar(String nameSatushy, String surnameSatushy, String emailSatushy, String passwordSatushy, String phoneNumberSatushy) {
        this.nameSatushy = nameSatushy;
        this.surnameSatushy = surnameSatushy;
        this.emailSatushy = emailSatushy;
        this.passwordSatushy = passwordSatushy;
        this.phoneNumberSatushy = phoneNumberSatushy;
    }

    public String getNameSatushy() {
        return nameSatushy;
    }

    public void setNameSatushy(String nameSatushy) {
        this.nameSatushy = nameSatushy;
    }

    public String getSurnameSatushy() {
        return surnameSatushy;
    }

    public void setSurnameSatushy(String surnameSatushy) {
        this.surnameSatushy = surnameSatushy;
    }

    public String getEmailSatushy() {
        return emailSatushy;
    }

    public void setEmailSatushy(String emailSatushy) {
        this.emailSatushy = emailSatushy;
    }

    public String getPasswordSatushy() {
        return passwordSatushy;
    }

    public void setPasswordSatushy(String passwordSatushy) {
        this.passwordSatushy = passwordSatushy;
    }

    public String getPhoneNumberSatushy() {
        return phoneNumberSatushy;
    }

    public void setPhoneNumberSatushy(String phoneNumberSatushy) {
        this.phoneNumberSatushy = phoneNumberSatushy;
    }
}
