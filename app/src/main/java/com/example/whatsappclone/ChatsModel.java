package com.example.whatsappclone;

//ADDED TIME IN THIS

public class ChatsModel {
    String sender;
    String receiver;
    String imageUrl;
    String message;
    String time;

    public ChatsModel(String sender, String receiver, String imageUrl, String message, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.imageUrl = imageUrl;
        this.message = message;
        this.time = time;
    }

    public ChatsModel()
    {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
