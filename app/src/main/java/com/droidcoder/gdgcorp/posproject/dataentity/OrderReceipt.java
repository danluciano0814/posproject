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
 * Created by DanLuciano on 3/4/2017.
 */

@Entity
public class OrderReceipt {

    @Id(autoincrement = true)
    private Long id;

    private long receiptId;

    private String receiptIdentification;

    private Date created;

    private Date deleted;

    private Date paidDate;

    private String voidBy;

    private double totalCostPrice;

    private double totalSellPrice;

    private double totalDeductedPrice;

    private double totalDiscount;

    private double totalTaxExempt;

    private double totalVatSales;

    private double totalNonVatSales;

    private double totalVat;

    private double taxValue;

    private double serviceChargeValue;

    private double serviceChargeTotal;

    private boolean onHold;

    private boolean isPaid;

    private long customerId;

    private String paymentType;

    private double cashTender;

    private long userId;

    @ToMany(referencedJoinProperty = "orderReceiptId")
    @OrderBy("created DESC")
    private List<OrderProduct> orderProducts;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 424433010)
    private transient OrderReceiptDao myDao;

    @Generated(hash = 489551901)
    public OrderReceipt(Long id, long receiptId, String receiptIdentification, Date created,
            Date deleted, Date paidDate, String voidBy, double totalCostPrice, double totalSellPrice,
            double totalDeductedPrice, double totalDiscount, double totalTaxExempt,
            double totalVatSales, double totalNonVatSales, double totalVat, double taxValue,
            double serviceChargeValue, double serviceChargeTotal, boolean onHold, boolean isPaid,
            long customerId, String paymentType, double cashTender, long userId) {
        this.id = id;
        this.receiptId = receiptId;
        this.receiptIdentification = receiptIdentification;
        this.created = created;
        this.deleted = deleted;
        this.paidDate = paidDate;
        this.voidBy = voidBy;
        this.totalCostPrice = totalCostPrice;
        this.totalSellPrice = totalSellPrice;
        this.totalDeductedPrice = totalDeductedPrice;
        this.totalDiscount = totalDiscount;
        this.totalTaxExempt = totalTaxExempt;
        this.totalVatSales = totalVatSales;
        this.totalNonVatSales = totalNonVatSales;
        this.totalVat = totalVat;
        this.taxValue = taxValue;
        this.serviceChargeValue = serviceChargeValue;
        this.serviceChargeTotal = serviceChargeTotal;
        this.onHold = onHold;
        this.isPaid = isPaid;
        this.customerId = customerId;
        this.paymentType = paymentType;
        this.cashTender = cashTender;
        this.userId = userId;
    }

    @Generated(hash = 55664174)
    public OrderReceipt() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getReceiptId() {
        return this.receiptId;
    }

    public void setReceiptId(long receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptIdentification() {
        return this.receiptIdentification;
    }

    public void setReceiptIdentification(String receiptIdentification) {
        this.receiptIdentification = receiptIdentification;
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

    public String getVoidBy() {
        return this.voidBy;
    }

    public void setVoidBy(String voidBy) {
        this.voidBy = voidBy;
    }

    public double getTotalCostPrice() {
        return this.totalCostPrice;
    }

    public void setTotalCostPrice(double totalCostPrice) {
        this.totalCostPrice = totalCostPrice;
    }

    public double getTotalSellPrice() {
        return this.totalSellPrice;
    }

    public void setTotalSellPrice(double totalSellPrice) {
        this.totalSellPrice = totalSellPrice;
    }

    public double getTotalDeductedPrice() {
        return this.totalDeductedPrice;
    }

    public void setTotalDeductedPrice(double totalDeductedPrice) {
        this.totalDeductedPrice = totalDeductedPrice;
    }

    public double getTotalDiscount() {
        return this.totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalTaxExempt() {
        return this.totalTaxExempt;
    }

    public void setTotalTaxExempt(double totalTaxExempt) {
        this.totalTaxExempt = totalTaxExempt;
    }

    public double getTotalVatSales() {
        return this.totalVatSales;
    }

    public void setTotalVatSales(double totalVatSales) {
        this.totalVatSales = totalVatSales;
    }

    public double getTotalNonVatSales() {
        return this.totalNonVatSales;
    }

    public void setTotalNonVatSales(double totalNonVatSales) {
        this.totalNonVatSales = totalNonVatSales;
    }

    public double getTotalVat() {
        return this.totalVat;
    }

    public void setTotalVat(double totalVat) {
        this.totalVat = totalVat;
    }

    public double getTaxValue() {
        return this.taxValue;
    }

    public void setTaxValue(double taxValue) {
        this.taxValue = taxValue;
    }

    public double getServiceChargeValue() {
        return this.serviceChargeValue;
    }

    public void setServiceChargeValue(double serviceChargeValue) {
        this.serviceChargeValue = serviceChargeValue;
    }

    public boolean getOnHold() {
        return this.onHold;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }

    public boolean getIsPaid() {
        return this.isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getCashTender() {
        return this.cashTender;
    }

    public void setCashTender(double cashTender) {
        this.cashTender = cashTender;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 922282589)
    public List<OrderProduct> getOrderProducts() {
        if (orderProducts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderProductDao targetDao = daoSession.getOrderProductDao();
            List<OrderProduct> orderProductsNew = targetDao
                    ._queryOrderReceipt_OrderProducts(id);
            synchronized (this) {
                if (orderProducts == null) {
                    orderProducts = orderProductsNew;
                }
            }
        }
        return orderProducts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 797759809)
    public synchronized void resetOrderProducts() {
        orderProducts = null;
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

    public double getServiceChargeTotal() {
        return this.serviceChargeTotal;
    }

    public void setServiceChargeTotal(double serviceChargeTotal) {
        this.serviceChargeTotal = serviceChargeTotal;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getPaidDate() {
        return this.paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1858427806)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderReceiptDao() : null;
    }

    

}
