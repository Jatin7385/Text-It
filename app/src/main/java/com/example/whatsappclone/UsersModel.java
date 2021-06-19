package com.example.whatsappclone;

public class UsersModel {
    String name,uri,number,country_code,id;

    public UsersModel() {
        this.name = "";
        this.uri = "";
        this.number = "";
        this.country_code = "";
        this.id = "";
    }

    public UsersModel(String name, String uri, String number, String country_code, String id) {
        this.name = name;
        this.uri = uri;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
