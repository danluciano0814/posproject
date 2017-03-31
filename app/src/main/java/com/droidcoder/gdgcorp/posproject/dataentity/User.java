package com.droidcoder.gdgcorp.posproject.dataentity;

import com.google.firebase.database.IgnoreExtraProperties;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by DanLuciano on 1/10/2017.
 */
@Entity
public class User {

    @Id(autoincrement = true)
    private Long id;

    private String firebaseId;

    private Date created;

    private Date deleted;

    @Unique
    private String email;

    private String passwordCode;

    private String remarks;

    @Unique
    private String firstName;

    private String lastName;

    private byte[] image;

    private long userRoleId;


    @Generated(hash = 875656602)
    public User(Long id, String firebaseId, Date created, Date deleted,
            String email, String passwordCode, String remarks, String firstName,
            String lastName, byte[] image, long userRoleId) {
        this.id = id;
        this.firebaseId = firebaseId;
        this.created = created;
        this.deleted = deleted;
        this.email = email;
        this.passwordCode = passwordCode;
        this.remarks = remarks;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.userRoleId = userRoleId;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirebaseId() {
        return this.firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
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

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getUserRoleId() {
        return this.userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


}
