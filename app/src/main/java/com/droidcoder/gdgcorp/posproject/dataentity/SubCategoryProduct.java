package com.droidcoder.gdgcorp.posproject.dataentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by DanLuciano on 2/12/2017.
 */

@Entity
public class SubCategoryProduct {

    @Id(autoincrement = true) Long id;
    long productId;
    long subCategoryId;
    @Generated(hash = 1377813804)
    public SubCategoryProduct(Long id, long productId, long subCategoryId) {
        this.id = id;
        this.productId = productId;
        this.subCategoryId = subCategoryId;
    }
    @Generated(hash = 402547894)
    public SubCategoryProduct() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getProductId() {
        return this.productId;
    }
    public void setProductId(long productId) {
        this.productId = productId;
    }
    public long getSubCategoryId() {
        return this.subCategoryId;
    }
    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

}
