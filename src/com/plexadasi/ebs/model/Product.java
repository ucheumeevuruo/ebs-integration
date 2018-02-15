/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexadasi.ebs.model;

/**
 *
 * @author SAP Training
 */
public class Product {
    private Integer Id;
    private String partNumber;
    private String description;
    private Integer organizationId;
    private String organizationCode;
    
    /**
     * @return the Id
     */
    public Integer getId() {
        return Id;
    }

    /**
     * @return the partNumber
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * @return the organizationId
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(Integer Id) {
        this.Id = Id;
    }

    /**
     * @param partNumber the partNumber to set
     */
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the organizationCode
     */
    public String getOrganizationCode() {
        return organizationCode;
    }

    /**
     * @param organizationCode the organizationCode to set
     */
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }
}
