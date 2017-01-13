package com.droidcoder.gdgcorp.posproject.dataentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by DanLuciano on 1/11/2017.
 */

@Entity
public class FirebaseUser {

    @Unique
    private String email;

    private String passwordCode;

    @Unique
    private String firstName;

    private String lastName;

    @Generated(hash = 1553903994)
    public FirebaseUser(String email, String passwordCode, String firstName,
            String lastName) {
        this.email = email;
        this.passwordCode = passwordCode;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Generated(hash = 1942424435)
    public FirebaseUser() {
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordCode() {
        return this.passwordCode;
    }

    public void setPasswordCode(String passwordCode) {
        this.passwordCode = passwordCode;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
