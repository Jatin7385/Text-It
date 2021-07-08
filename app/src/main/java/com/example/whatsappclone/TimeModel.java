package com.example.whatsappclone;

public class TimeModel {
    String myId,friendId,time,date,option;

    public TimeModel()
    {
        myId = "default";
        friendId = "default";
        time = "";
        date = "";
        option = "default";
    }

    public TimeModel(String myId, String friendId, String time,String date,String option) {
        this.myId = myId;
        this.friendId = friendId;
        this.time = time;
        this.date = date;
        this.option = option;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {return date;}

    public void setDate(String date) {this.date = date;}

    public String getOption() {return option;}

    public void setOption(String option) {this.option = option;}
}
