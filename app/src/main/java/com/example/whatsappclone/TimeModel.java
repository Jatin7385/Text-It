package com.example.whatsappclone;

import java.util.Comparator;

public class TimeModel {
    String id,myId,friendId,time,date;

    public TimeModel()
    {
        id = "";
        myId = "default";
        friendId = "default";
        time = "";
        date = "";
    }

    public TimeModel(String id,String myId, String friendId, String time,String date,String option) {
        this.id = id;
        this.myId = myId;
        this.friendId = friendId;
        this.time = time;
        this.date = date;
    }

    public static Comparator<TimeModel> TimeModelComparator = new Comparator<TimeModel>() {
        @Override
        public int compare(TimeModel o1, TimeModel o2) {
            if(o1.getDate().equals(o2.getDate())) {
                return o1.getTime().compareTo(o2.getTime());
            }
            else
                {
                    return o1.getDate().compareTo(o2.getDate());
                }
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
