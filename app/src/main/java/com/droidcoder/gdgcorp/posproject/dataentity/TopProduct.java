package com.droidcoder.gdgcorp.posproject.dataentity;

/**
 * Created by DanLuciano on 4/17/2017.
 */

public class TopProduct {

    int rank;
    String productName;
    double totalSold;
    double totalSales;

    public TopProduct(int rank, String productName, double totalSold, double totalSales) {
        this.rank = rank;
        this.productName = productName;
        this.totalSold = totalSold;
        this.totalSales = totalSales;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(double totalSold) {
        this.totalSold = totalSold;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

}
