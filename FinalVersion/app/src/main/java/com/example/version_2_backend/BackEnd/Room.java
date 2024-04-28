package com.example.version_2_backend.BackEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room {




    @Override
    public String toString() {
        return "Room{" +
                "ID='" + ID + '\'' +
                ", roomName='" + roomName + '\'' +
                ", password='" + password + '\'' +
                ", trackCount=" + trackCount +
                ", users=" + users +
                ", sound=" + sound +
                '}';
    }

    String ID;
    String roomName ;
    String password;
    int trackCount;
    public ArrayList<User> users;
    int sound = 100;

    public Room(String ID, String roomName, String password, int trackCount, ArrayList<User> users, int sound) {
        this.ID = ID;
        this.roomName = roomName;
        this.password = password;
        this.users = users;
        this.sound = sound;
        this.trackCount = trackCount;
    }


    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public int getTrackCount() {
        return trackCount;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }



    public void setID(String ID) {
        this.ID = ID;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }


    public Room() {
        users = new ArrayList<User>();
    }
    public Room(String ID, String password) {
        users = new ArrayList<User>();
        this.ID = ID;
        this.password = password;
    }

    public Room(String ID, String roomName, String password) {
        users = new ArrayList<User>();
        this.ID = ID;
        this.roomName = roomName;
        this.password = password;
    }

    public Room(String ID, String password, String roomName, ArrayList<User> users, int sound) {
        users = new ArrayList<User>();
        this.ID = ID;
        this.password = password;
        this.roomName = roomName;
        this.users = users;
        this.sound = sound;
    }

    public String getID() {
        return ID;
    }

    public String getRoomName() {
        return roomName;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public int getSound() {
        return sound;
    }

    public void addUser(User user){
        users.add(user);
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", ID);
        result.put("roomName", roomName);
        result.put("users", users);
        result.put("sound", sound);
        result.put("password", password);
        result.put("trackCount", trackCount);

        return result;
    }
}
