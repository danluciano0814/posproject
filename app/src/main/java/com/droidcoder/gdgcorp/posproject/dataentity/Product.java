package com.droidcoder.gdgcorp.posproject.dataentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by DanLuciano on 1/5/2017.
 */

@Entity
public class Product {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    Date created;

    Date deleted;

    @Unique
    String name;

    String description;

    @NotNull
    double costPrice;

    @NotNull
    double sellPrice;

    long promoSaleId;

    byte[] image;

    double stocks;

    @Generated(hash = 2064516014)
    public Product(Long id, @NotNull Date created, Date deleted, String name,
            String description, double costPrice, double sellPrice,
            long promoSaleId, byte[] image, double stocks) {
        this.id = id;
        this.created = created;
        this.deleted = deleted;
        this.name = name;
        this.description = description;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.promoSaleId = promoSaleId;
        this.image = image;
        this.stocks = stocks;
    }

    @Generated(hash = 1890278724)
    public Product() {
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

    public double getCostPrice() {
        return this.costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSellPrice() {
        return this.sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public long getPromoSaleId() {
        return this.promoSaleId;
    }

    public void setPromoSaleId(long promoSaleId) {
        this.promoSaleId = promoSaleId;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getStocks() {
        return this.stocks;
    }

    public void setStocks(double stocks) {
        this.stocks = stocks;
    }


}
