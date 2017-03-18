package com.droidcoder.gdgcorp.posproject.dataentity;

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
 * Created by DanLuciano on 1/5/2017.
 */
@Entity
public class Customer {

    @Id(autoincrement = true)
    private Long id;

    private Date created;

    private Date deleted;

    private String code;

    private String firstName;

    private String lastName;

    private String contact;

    private String email;

    private String address;

    private double points;

    private byte[] image;

    @ToMany(referencedJoinProperty = "customerId")
    @OrderBy("created DESC")
    private List<OrderReceipt> orderReceipts;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1697251196)
    private transient CustomerDao myDao;

    @Generated(hash = 1134026160)
    public Customer(Long id, Date created, Date deleted, String code,
            String firstName, String lastName, String contact, String email,
            String address, double points, byte[] image) {
        this.id = id;
        this.created = created;
        this.deleted = deleted;
        this.code = code;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.points = points;
        this.image = image;
    }

    @Generated(hash = 60841032)
    public Customer() {
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

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPoints() {
        return this.points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 764413491)
    public List<OrderReceipt> getOrderReceipts() {
        if (orderReceipts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderReceiptDao targetDao = daoSession.getOrderReceiptDao();
            List<OrderReceipt> orderReceiptsNew = targetDao
                    ._queryCustomer_OrderReceipts(id);
            synchronized (this) {
                if (orderReceipts == null) {
                    orderReceipts = orderReceiptsNew;
                }
            }
        }
        return orderReceipts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 524367221)
    public synchronized void resetOrderReceipts() {
        orderReceipts = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 462117449)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCustomerDao() : null;
    }

}
