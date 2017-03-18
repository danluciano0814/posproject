package com.droidcoder.gdgcorp.posproject.dataentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by DanLuciano on 1/5/2017.
 */
@Entity
public class Discount {

    @Id(autoincrement = true)
    private Long id;

    private Date created;

    private Date deleted;

    @Unique
    private String name;

    private String description;

    private boolean isPercentage;

    private double discountValue;

    @Generated(hash = 1466914342)
    public Discount(Long id, Date created, Date deleted, String name,
            String description, boolean isPercentage, double discountValue) {
        this.id = id;
        this.created = created;
        this.deleted = deleted;
        this.name = name;
        this.description = description;
        this.isPercentage = isPercentage;
        this.discountValue = discountValue;
    }

    @Generated(hash = 1777606421)
    public Discount() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsPercentage() {
        return this.isPercentage;
    }

    public void setIsPercentage(boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

    public double getDiscountValue() {
        return this.discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

}
