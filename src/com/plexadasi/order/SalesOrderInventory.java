/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.data.SiebelException
 */
package com.plexadasi.order;

import com.plexadasi.ebs.SiebelApplication.bin.SOInventory;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import java.sql.Connection;
import java.sql.SQLException;

public class SalesOrderInventory {
    private Integer orderId;
    private Integer soldToOrgId;
    private String shipToOrgId;
    private Integer invoiceId;
    private Integer soldFromId;
    private Integer salesRepId;
    private String transactionCode;
    private String statusCode;
    private String purchaseOrderNumber;
    private Integer sourceId;
    private String siebelOrderId;
    private Integer billToId;
    private Integer shipToId;
    private String type;

    public void setSiebelOrderId(String orderId) {
        this.siebelOrderId = orderId;
    }

    public String getSiebelOrderId() {
        return this.siebelOrderId;
    }

    public Integer getSoldToOrgId() {
        return this.soldToOrgId;
    }

    public void setSoldToOrgId(Integer soldToOrgId) {
        this.soldToOrgId = soldToOrgId;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getShipToOrgId() {
        return this.shipToOrgId;
    }

    public void setShipToOrgId(Integer shipToOrgId) {
    }

    public Integer getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getSoldFromId() {
        return this.soldFromId;
    }

    public void setSoldFromId(Integer soldFromId) {
        this.soldFromId = soldFromId;
    }

    public Integer getSalesRepId() {
        return this.salesRepId;
    }

    public void setSalesRepId(Integer salesRepId) {
        this.salesRepId = salesRepId;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTransactionCode() {
        return this.transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getPurchaseOrderNumber() {
        return this.purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public Integer getSourceId() {
        return this.sourceId;
    }

    public SOInventory inventoryItems(SiebelDataBean sb, Connection ebsConn) {
        return new SOInventory(sb, ebsConn, this);
    }

    public com.plexadasi.ebs.SiebelApplication.bin.SalesOrderInventory inventory(SiebelDataBean sb, Connection ebsConn) throws SiebelException, SQLException {
        return new com.plexadasi.ebs.SiebelApplication.bin.SalesOrderInventory(sb, ebsConn, this);
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public String toString() {
        String output = "";
        output = output + "\t\t[Siebel order number=" + this.siebelOrderId + "]\n";
        output = output + "\t\t[Order type id=" + this.orderId + "]\n";
        output = output + "\t\t[Sold to org id=" + this.soldToOrgId + "]\n";
        output = output + "\t\t[Ship to org id=" + this.shipToId + "]\n";
        output = output + "\t\t[Invoice id=" + this.billToId + "]\n";
        output = output + "\t\t[Sold from id=" + this.soldFromId + "]\n";
        output = output + "\t\t[Sales rep id=" + this.salesRepId + "]\n";
        output = output + "\t\t[Transaction code=" + this.transactionCode + "]\n";
        output = output + "\t\t[Status code=" + this.statusCode + "]\n";
        output = output + "\t\t[Purchase order number=" + this.purchaseOrderNumber + "]\n";
        output = output + "\t\t[Source id=" + this.sourceId + "]\n";
        return this.getClass().getSimpleName() + "\n[Details\n\t[\n" + output + "\t]\n";
    }

    public int getBillToId() {
        return this.billToId;
    }

    public int getShipToId() {
        return this.shipToId;
    }

    public void setShipToId(Integer shipToId) {
        this.shipToId = shipToId;
    }

    public void setBillToId(Integer billToId) {
        this.billToId = billToId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

