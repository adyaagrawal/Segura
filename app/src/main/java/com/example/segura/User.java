package com.example.segura;

import java.util.HashMap;
import java.util.List;

public class User {
    String email;
    String name;
    String phone;
    HashMap<String,String> contact;

    public User() {
    }

    public User(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public HashMap<String, String> getContacts() {
        return contact;
    }

    public void setContacts(HashMap<String, String> contacts) {
        this.contact = contacts;
    }
}
