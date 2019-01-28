/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.plexada.si.siebel.annotations.BusinessField
 */
package com.plexadasi.siebel.model;

import org.plexada.si.siebel.annotations.BusinessField;

public class Invoice {
    private Integer Id;
    @BusinessField(value="PLX Primary Sales Rep Number")
    private Integer salesPerson;
    @BusinessField(value="PLX Transaction Type")
    private String transactionType;
    private Integer transactionId;
    private Float deliveryCharges;
    private Float freight;
    private Float computerProgramming;
    private Float sundries;
    private Float tax;
    @BusinessField(value="bill_to")
    private Integer billingId;
    @BusinessField(value="PLX Ref Number")
    private String referenceNumber;
    @BusinessField(value="Currency Code")
    private String currencyCode;

    public Integer getId() {
        return this.Id;
    }

    public void setId(Integer Id2) {
        this.Id = Id2;
    }

    public Integer getSalesPerson() {
        return this.salesPerson;
    }

    public Integer getTransactionId() {
        return this.transactionId;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Float getDeliveryCharges() {
        return this.deliveryCharges;
    }

    public Float getFreight() {
        return this.freight;
    }

    public Float getComputerProgramming() {
        return this.computerProgramming;
    }

    public Float getSundries() {
        return this.sundries;
    }

    public Float getTax() {
        return this.tax;
    }

    public void setSalesPerson(Integer salesPerson) {
        this.salesPerson = salesPerson;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public void setDeliveryCharges(Float deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public void setFreight(Float freight) {
        this.freight = freight;
    }

    public void setComputerProgramming(Float computerProgramming) {
        this.computerProgramming = computerProgramming;
    }

    public void setSundries(Float sundries) {
        this.sundries = sundries;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public Integer getBillingId() {
        return this.billingId;
    }

    public void setBillingId(Integer billingId) {
        this.billingId = billingId;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String toString() {
        String NEW_LINE = System.getProperty("line.separator");
        String print = "\t\t[Id]:=" + this.Id + NEW_LINE;
        print = print + "\t\t[Sales Person]:=" + this.salesPerson + NEW_LINE;
        print = print + "\t\t[Transaction Id]:=" + this.transactionId + NEW_LINE;
        print = print + "\t\t[Delivery Charges]:=" + this.deliveryCharges + NEW_LINE;
        print = print + "\t\t[Freight]:=" + this.freight + NEW_LINE;
        print = print + "\t\t[Computer Programming]:=" + this.computerProgramming + NEW_LINE;
        print = print + "\t\t[Sundries]:=" + this.sundries + NEW_LINE;
        print = print + "\t\t[Tax]:=" + this.tax + NEW_LINE;
        print = print + "\t\t[Billing Id]:=" + this.billingId + NEW_LINE;
        print = print + "\t\t[Reference Number]:=" + this.referenceNumber + NEW_LINE;
        return this.getClass().getSimpleName() + "\n[Details\n\t[\n" + print + "\t]\n";
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}

