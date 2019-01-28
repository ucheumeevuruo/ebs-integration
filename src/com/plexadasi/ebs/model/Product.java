/*
 * Decompiled with CFR 0_123.
 */
package com.plexadasi.ebs.model;

public class Product {
    private Integer Id;
    private String partNumber;
    private String description;
    private Integer organizationId;
    private String organizationCode;

    public Integer getId() {
        return this.Id;
    }

    public String getPartNumber() {
        return this.partNumber;
    }

    public Integer getOrganizationId() {
        return this.organizationId;
    }

    public void setId(Integer Id2) {
        this.Id = Id2;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganizationCode() {
        return this.organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }
}

