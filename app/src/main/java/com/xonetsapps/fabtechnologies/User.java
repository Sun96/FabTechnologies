package com.xonetsapps.fabtechnologies;

import java.util.Date;

/**
 * Created by Abhi on 20 Jan 2018 020.
 */

public class User {
    String name,email,mobile,company;
    String address,id,token;
    Date sessionExpiryDate;

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getMobile() {
        return mobile;
    }
    public String getAddress() {
        return address;
    }
    public String getToken() {
        return token;
    }
    public String getCompany() {
        return company;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
