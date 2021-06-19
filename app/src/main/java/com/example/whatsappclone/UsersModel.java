package com.example.whatsappclone;

public class UsersModel {
    String name,imageURL,number,country_code,id;

    public UsersModel() {
        this.name = "";
        this.imageURL = "";
        this.number = "";
        this.country_code = "";
        this.id = "";
    }

    public UsersModel(String name, String imageURL, String number, String country_code, String id) {
        this.name = name;
        this.imageURL = imageURL;
        this.number = number;
        this.country_code = country_code;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
