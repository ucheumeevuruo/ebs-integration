/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  org.plexada.si.siebel.annotations.BusinessField
 *  org.plexada.si.siebel.annotations.Entity
 */
package com.plexadasi.siebel.model;

import com.plexadasi.siebel.IOBusinessComponent;
import oracle.xdb.XMLType;
import org.plexada.si.siebel.annotations.BusinessField;
import org.plexada.si.siebel.annotations.Entity;

@IOBusinessComponent(businessComponent = "ATP Header")
@Entity(businessComponent = "Order Entry - Orders", businessObject = "Order Entry")
public class OrderEntry {
    @BusinessField("Id")
    private String orderId;
    @BusinessField("Order Number")
    private String orderNumber;
    @BusinessField(value="ebs_id")
    private Integer integrationId;
    @BusinessField(value="warehouse_id")
    private Integer warehouseId;
    @BusinessField(value="PLX Agent Id")
    private Integer agentId;
    @BusinessField(value="Requested Ship Date")
    private String dueDate;
    @BusinessField(value="Back Office Order Number")
    private Integer backOfficeOrderNumber;
    @BusinessField(value="PLX Vendor EBS Id")
    private Integer vendorId;
    @BusinessField(value="Currency Code")
    private String currencyCode;
    @BusinessField(value="PLX Ref Number")
    private String referenceNumber;
    @BusinessField(value="PLX Primary Sales Rep Number")
    private Integer salesPerson;
    @BusinessField(value="PLX Transaction Type")
    private String transactionType;
    private Integer transactionId;
    private Float deliveryCharges;
    @BusinessField(value="Freight")
    private Float freight;
    private Float computerProgramming;
    private Float sundries;
    private Float tax;
    @BusinessField(value="bill_to")
    private Integer billingId;
    @BusinessField(value="ship_to")
    private Integer shippingId;
    @BusinessField(value="PLX Payment Mode")
    private String paymentMode;
    @BusinessField(value="PLX Proforma Invoice Number")
    private String invoiceNumber;
    @BusinessField(value="SiebelMessage")
    private XMLType siebelMessage;
    @BusinessField(value="Carrier Priority")
    private String shippingMethod;

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getIntegrationId() {
        return this.integrationId;
    }

    public void setIntegrationId(Integer integrationId) {
        this.integrationId = integrationId;
    }

    public Integer getWarehouseId() {
        return this.warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getAgentId() {
        return this.agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getVendorId() {
        return this.vendorId;
    }

    /**
     * @return the backOfficeOrderNumber
     */
    public Integer getBackOfficeOrderNumber() {
        return backOfficeOrderNumber;
    }

    /**
     * @param backOfficeOrderNumber the backOfficeOrderNumber to set
     */
    public void setBackOfficeOrderNumber(Integer backOfficeOrderNumber) {
        this.backOfficeOrderNumber = backOfficeOrderNumber;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public Integer getShippingId() {
        return this.shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    public String getReferenceNumber() {
        return this.referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPaymentMode() {
        return this.paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * @return the siebelMessage
     */
    public XMLType getSiebelMessage() {
        return siebelMessage;
    }

    /**
     * @param siebelMessage the siebelMessage to set
     */
    public void setSiebelMessage(XMLType siebelMessage) {
        this.siebelMessage = siebelMessage;
    }

    /**
     * @return the shippingMethod
     */
    public String getShippingMethod() {
        return shippingMethod;
    }

    /**
     * @param shippingMethod the shippingMethod to set
     */
    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    @Override
    public String toString() {
        String NEW_LINE = System.getProperty("line.separator");
        String print = "\t\t[Order Number]:=" + this.orderNumber + NEW_LINE;
        print = print + "\t\t[Id]:=" + this.orderId + NEW_LINE;
        print = print + "\t\t[Back Office Order Number]:=" + this.backOfficeOrderNumber + NEW_LINE;
        print = print + "\t\t[Integration Id]:=" + this.integrationId + NEW_LINE;
        print = print + "\t\t[Sales Person]:=" + this.salesPerson + NEW_LINE;
        print = print + "\t\t[Transaction Id]:=" + this.transactionId + NEW_LINE;
        print = print + "\t\t[Delivery Charges]:=" + this.deliveryCharges + NEW_LINE;
        print = print + "\t\t[Freight]:=" + this.freight + NEW_LINE;
        print = print + "\t\t[Computer Programming]:=" + this.computerProgramming + NEW_LINE;
        print = print + "\t\t[Sundries]:=" + this.sundries + NEW_LINE;
        print = print + "\t\t[Tax]:=" + this.tax + NEW_LINE;
        print = print + "\t\t[Billing Id]:=" + this.billingId + NEW_LINE;
        print = print + "\t\t[Reference Number]:=" + this.referenceNumber + NEW_LINE;
        print = print + "\t\t[Currency Code]:=" + this.currencyCode + NEW_LINE;
        print = print + "\t\t[Payment Mode]:=" + this.paymentMode + NEW_LINE;
        print = print + "\t\t[Invoice Number]:=" + this.invoiceNumber + NEW_LINE;
        print = print + "\t\t[SiebelMessage]:=" + this.siebelMessage + NEW_LINE;
        return this.getClass().getSimpleName() + "\n[Details\n\t[\n" + print + "\t]\n";
    }
}

