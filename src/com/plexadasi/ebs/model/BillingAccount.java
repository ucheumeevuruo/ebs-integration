/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.model;

public class BillingAccount {
    private int id;
    private Integer siteUseId;
    private String billingType;
    private String address;

    public int getId() {
        return this.id;
    }

    public Integer getSiteUseId() {
        return this.siteUseId;
    }

    public String getBillingType() {
        return this.billingType;
    }

    public String getAddress() {
        return this.address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSiteUseId(Integer siteUseId) {
        this.siteUseId = siteUseId;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

