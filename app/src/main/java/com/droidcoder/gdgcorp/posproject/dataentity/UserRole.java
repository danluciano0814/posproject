package com.droidcoder.gdgcorp.posproject.dataentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by DanLuciano on 1/5/2017.
 */
@Entity
public class UserRole {

    @Id(autoincrement = true)
    private Long id;

    private Date created;

    private Date deleted;

    private String roleName;

    private boolean allowSales;

    private boolean allowInvoice;

    private boolean allowReport;

    private boolean allowInventory;

    private boolean allowCustomer;

    private boolean allowEmployee;

    private boolean allowData;

    private boolean allowStorage;

    private boolean allowSetting;

    @ToMany(referencedJoinProperty = "userRoleId")
    @OrderBy("created DESC")
    private List<User> users;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1114803083)
    private transient UserRoleDao myDao;

    @Generated(hash = 382648945)
    public UserRole(Long id, Date created, Date deleted, String roleName,
            boolean allowSales, boolean allowInvoice, boolean allowReport,
            boolean allowInventory, boolean allowCustomer, boolean allowEmployee,
            boolean allowData, boolean allowStorage, boolean allowSetting) {
        this.id = id;
        this.created = created;
        this.deleted = deleted;
        this.roleName = roleName;
        this.allowSales = allowSales;
        this.allowInvoice = allowInvoice;
        this.allowReport = allowReport;
        this.allowInventory = allowInventory;
        this.allowCustomer = allowCustomer;
        this.allowEmployee = allowEmployee;
        this.allowData = allowData;
        this.allowStorage = allowStorage;
        this.allowSetting = allowSetting;
    }

    @Generated(hash = 552541888)
    public UserRole() {
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

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean getAllowSales() {
        return this.allowSales;
    }

    public void setAllowSales(boolean allowSales) {
        this.allowSales = allowSales;
    }

    public boolean getAllowInvoice() {
        return this.allowInvoice;
    }

    public void setAllowInvoice(boolean allowInvoice) {
        this.allowInvoice = allowInvoice;
    }

    public boolean getAllowReport() {
        return this.allowReport;
    }

    public void setAllowReport(boolean allowReport) {
        this.allowReport = allowReport;
    }

    public boolean getAllowInventory() {
        return this.allowInventory;
    }

    public void setAllowInventory(boolean allowInventory) {
        this.allowInventory = allowInventory;
    }

    public boolean getAllowCustomer() {
        return this.allowCustomer;
    }

    public void setAllowCustomer(boolean allowCustomer) {
        this.allowCustomer = allowCustomer;
    }

    public boolean getAllowEmployee() {
        return this.allowEmployee;
    }

    public void setAllowEmployee(boolean allowEmployee) {
        this.allowEmployee = allowEmployee;
    }

    public boolean getAllowData() {
        return this.allowData;
    }

    public void setAllowData(boolean allowData) {
        this.allowData = allowData;
    }

    public boolean getAllowStorage() {
        return this.allowStorage;
    }

    public void setAllowStorage(boolean allowStorage) {
        this.allowStorage = allowStorage;
    }

    public boolean getAllowSetting() {
        return this.allowSetting;
    }

    public void setAllowSetting(boolean allowSetting) {
        this.allowSetting = allowSetting;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 98604851)
    public List<User> getUsers() {
        if (users == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            List<User> usersNew = targetDao._queryUserRole_Users(id);
            synchronized (this) {
                if (users == null) {
                    users = usersNew;
                }
            }
        }
        return users;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1027274768)
    public synchronized void resetUsers() {
        users = null;
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
    @Generated(hash = 1183361425)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserRoleDao() : null;
    }


}
