package com.example.loginregister;

public class User {

    public String firstname, lastname, mobile, address, email;


    public User(){

    }

    public User (String firstname, String lastname,String mobile, String address,String email){
        this.firstname = firstname;
        this.lastname = lastname;
        this.mobile = mobile;
        this.address = address;
        this.email = email;

    }
}
