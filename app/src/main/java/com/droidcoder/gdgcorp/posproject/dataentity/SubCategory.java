package com.droidcoder.gdgcorp.posproject.dataentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by DanLuciano on 2/4/2017.
 */

@Entity
public class SubCategory {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String name;

    private Date created;

    private Date deleted;

    private long categoryId;

    @Generated(hash = 1747161477)
    public SubCategory(Long id, String name, Date created, Date deleted,
            long categoryId) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.deleted = deleted;
        this.categoryId = categoryId;
    }

    @Generated(hash = 1008922175)
    public SubCategory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

}
