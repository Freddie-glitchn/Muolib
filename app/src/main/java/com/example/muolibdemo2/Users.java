package com.example.muolibdemo2;

public class Users {
    public String name, email, admnNo, password;
    public Users(){

    }



    public Users(String name, String email, String admnNo, String password){
        this.name = name;
        this.email = email;
        this.admnNo = admnNo;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public String getAdmnNo() {
        return admnNo;
    }


}
