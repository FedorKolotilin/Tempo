package com.example.version_2_backend.BackEnd;

import java.util.Objects;

public class User {

    private String firstname;
    private String password;
    private String email;
    private String lastname;
    private int avatar;


    public User(String firstname,  String lastname, String password, String email) {
        this.firstname = firstname;
        this.password = password;
        this.email = email;
        this.lastname = lastname;
    }

    public User(){

    }
    public User(String name){
        this.firstname = name;
    }


    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String name) {
        this.firstname = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastname;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getPassword() {
        return password;
    }

    public int getAvatar() {
        return avatar;
    }

}
