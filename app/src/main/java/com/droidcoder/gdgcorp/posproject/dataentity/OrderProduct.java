package com.droidcoder.gdgcorp.posproject.dataentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by DanLuciano on 2/22/2017.
 */

@Entity
public class OrderProduct {

    @Id(autoincrement = true)
    private Long id;

    private Date created;

    private Date deleted;

    private String productName;

    private String productRemarks;

    private byte[] productImage;

    private long productId;

    private double productQuantity;

    private double productCostPrice;

    private double productSellPrice;

    private double productDeductedPrice;

    private boolean isDiscountPercent;

    private double discountValue;

    private double discountTotal;

    private boolean isTaxExempt;

    private double taxValue;

    private long orderReceiptId;

    private long discountId;

    private String note;

    @Generated(hash = 1250102356)
    public OrderProduct(Long id, Date created, Date deleted, String productName,
            String productRemarks, byte[] productImage, long productId,
            double productQuantity, double productCostPrice,
            double productSellPrice, double productDeductedPrice,
            boolean isDiscountPercent, double discountValue, double discountTotal,
            boolean isTaxExempt, double taxValue, long orderReceiptId,
            long discountId, String note) {
        this.id = id;
        this.created = created;
        this.deleted = deleted;
        this.productName = productName;
        this.productRemarks = productRemarks;
        this.productImage = productImage;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.productCostPrice = productCostPrice;
        this.productSellPrice = productSellPrice;
        this.productDeductedPrice = productDeductedPrice;
        this.isDiscountPercent = isDiscountPercent;
        this.discountValue = discountValue;
        this.discountTotal = discountTotal;
        this.isTaxExempt = isTaxExempt;
        this.taxValue = taxValue;
        this.orderReceiptId = orderReceiptId;
        this.discountId = discountId;
        this.note = note;
    }

    @Generated(hash = 1818552344)
    public OrderProduct() {
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

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductRemarks() {
        return this.productRemarks;
    }

    public void setProductRemarks(String productRemarks) {
        this.productRemarks = productRemarks;
    }

    public double getProductQuantity() {
        return this.productQuantity;
    }

    public void setProductQuantity(double productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductCostPrice() {
        return this.productCostPrice;
    }

    public void setProductCostPrice(double productCostPrice) {
        this.productCostPrice = productCostPrice;
    }

    public double getProductSellPrice() {
        return this.productSellPrice;
    }

    public void setProductSellPrice(double productSellPrice) {
        this.productSellPrice = productSellPrice;
    }

    public double getProductDeductedPrice() {
        return this.productDeductedPrice;
    }

    public void setProductDeductedPrice(double productDeductedPrice) {
        this.productDeductedPrice = productDeductedPrice;
    }

    public boolean getIsDiscountPercent() {
        return this.isDiscountPercent;
    }

    public void setIsDiscountPercent(boolean isDiscountPercent) {
        this.isDiscountPercent = isDiscountPercent;
    }

    public double getDiscountValue() {
        return this.discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getDiscountTotal() {
        return this.discountTotal;
    }

    public void setDiscountTotal(double discountTotal) {
        this.discountTotal = discountTotal;
    }

    public boolean getIsTaxExempt() {
        return this.isTaxExempt;
    }

    public void setIsTaxExempt(boolean isTaxExempt) {
        this.isTaxExempt = isTaxExempt;
    }

    public Double getTaxValue() {
        return this.taxValue;
    }

    public void setTaxValue(Double taxValue) {
        this.taxValue = taxValue;
    }

    public long getOrderReceiptId() {
        return this.orderReceiptId;
    }

    public void setOrderReceiptId(long orderReceiptId) {
        this.orderReceiptId = orderReceiptId;
    }

    public long getDiscountId() {
        return this.discountId;
    }

    public void setDiscountId(long discountId) {
        this.discountId = discountId;
    }

    public byte[] getProductImage() {
        return this.productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public void setTaxValue(double taxValue) {
        this.taxValue = taxValue;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getProductId() {
        return this.productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
