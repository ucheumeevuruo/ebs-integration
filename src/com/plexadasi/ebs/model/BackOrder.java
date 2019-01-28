/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.model;

public class BackOrder {
    private String pickMeaning;
    private String itemStatus;
    private String releaseStatus;
    private Integer quantity;

    public String getPickMeaning() {
        return this.pickMeaning;
    }

    public void setPickMeaning(String pickMeaning) {
        this.pickMeaning = pickMeaning;
    }

    public String getItemStatus() {
        return this.itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getReleaseStatus() {
        return this.releaseStatus;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        String NEW_LINE = System.getProperty("line.separator");
        String print = "Pick Meaning:" + this.pickMeaning + NEW_LINE;
        print = print + "Item Status:" + this.itemStatus + NEW_LINE;
        print = print + "Release Status:" + this.releaseStatus + NEW_LINE;
        print = print + "Quantity:" + this.quantity + NEW_LINE;
        return print;
    }
}

