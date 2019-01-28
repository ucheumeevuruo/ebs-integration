/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.siebel.data.SiebelDataBean
 *  com.siebel.eai.SiebelBusinessServiceException
 */
package com.plexadasi.order;

import com.plexadasi.helper.DataConverter;
import com.plexadasi.build.EBSSqlData;
import com.plexadasi.ebs.SiebelApplication.bin.Order;
import com.plexadasi.ebs.SiebelApplication.bin.POInventory;
import com.plexadasi.ebs.SiebelApplication.bin.PurchaseOrderIndividual;
import com.plexadasi.ebs.SiebelApplication.bin.PurchaseOrderOrganization;
import com.plexadasi.ebs.SiebelApplication.objects.Impl.Account;
import com.siebel.data.SiebelDataBean;
import com.siebel.eai.SiebelBusinessServiceException;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;

public class PurchaseOrderInventory {
    private final StringWriter errors = new StringWriter();
    private static SiebelDataBean siebConn = null;
    private static EBSSqlData ebsData = null;
    private static Order order = null;
    private static Account acc = null;
    private String siebelOrderNumber;
    private String siebelOrderId;
    private String siebelAccountId;
    private String accountType;
    private String[] organization = new String[0];
    private Array inventoryItem;
    private Integer sourceId;
    private String returnString;

    public void setSiebelOrderId(String orderId) {
        this.siebelOrderId = orderId;
    }

    public void setSiebelAccountId(String accountId) {
        this.siebelAccountId = accountId;
    }

    public void setSourceId(int string) {
        this.sourceId = string;
    }

    public void setInventoryItem(Array inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public void setAccountType(String type) {
        this.accountType = type;
    }

    public String getSiebelAccountId() {
        return this.siebelAccountId;
    }

    public String getSiebelOrderId() {
        return this.siebelOrderId;
    }

    public String getOrderNumber() {
        return order.getProperty("Order Number");
    }

    public Array getInventoryItem() throws SiebelBusinessServiceException, SQLException {
        return this.inventoryItem;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public Integer getSourceId() {
        return this.sourceId;
    }

    public String getShipToLocation() {
        return order.getProperty("PLX Warehouse Id");
    }

    public String getPromiseDate() {
        return order.getProperty("Requested Ship Date");
    }

    public String getShipToCity() {
        return acc.getProperty("Primary Ship To City");
    }

    public String getShipToState() {
        return acc.getProperty("Primary Bill To State");
    }

    public String getShipToCountry() {
        return acc.getProperty("Primary Ship To Country");
    }

    public String getBillToLocation() {
        return acc.getProperty("Primary Bill To Street Address");
    }

    public String getBillToCity() {
        return acc.getProperty("Primary Bill To City");
    }

    public String getBillToState() {
        return acc.getProperty("Primary Bill To State");
    }

    public String getBillToCountry() {
        return acc.getProperty("Primary Bill To Country");
    }

    public String getCurrencyCode() {
        return acc.getProperty("Currency Code");
    }

    public Integer getAgentCode() {
        return DataConverter.toInt(order.getProperty("PLX Agent Id"));
    }

    public String getEbsId() {
        return acc.getProperty("EBS Id");
    }

    public String getOrganizationId() {
        return this.organization[0];
    }

    public String getOrganizationCode() {
        return this.organization[0];
    }

    public String getOrganizationName() {
        return this.organization[0];
    }

    public String getShipToAccount() throws SiebelBusinessServiceException {
        return ebsData.shipToAccount(DataConverter.toInt(this.getEbsId()));
    }

    public POInventory inventory(SiebelDataBean sb, Connection ebs) {
        return new POInventory(sb, ebs, this);
    }

    private void triggerOrder() throws SiebelBusinessServiceException {
        order = new Order(siebConn);
        order.setPropertySet("Order Number", "Order Number");
        order.setPropertySet("PLX Agent Id", "PLX Agent Id");
        order.setPropertySet("PLX Warehouse Id", "PLX Warehouse Id");
        order.setPropertySet("Requested Ship Date", "Requested Ship Date");
        order.setSiebelAccountId(this.siebelOrderId);
        order.doTrigger();
    }

    private void triggetOrderAccount() throws SiebelBusinessServiceException {
        if (this.accountType.equalsIgnoreCase("individual")) {
            acc = new PurchaseOrderIndividual(siebConn);
        } else if (this.accountType.equalsIgnoreCase("organization")) {
            acc = new PurchaseOrderOrganization(siebConn);
        }
        acc.setSiebelAccountId(this.siebelAccountId);
        acc.doTrigger();
    }

    public PurchaseOrderInventory triggers(SiebelDataBean sb, EBSSqlData ed) throws SiebelBusinessServiceException {
        siebConn = sb;
        ebsData = ed;
        this.triggetOrderAccount();
        this.triggerOrder();
        return this;
    }

    public String toString() {
        this.returnString = "";
        this.returnString = this.returnString + "\t\t[Siebel order number=" + this.siebelOrderNumber + "\n";
        this.returnString = this.returnString + "\t\t[Siebel order id=" + this.siebelOrderId + "\n";
        this.returnString = this.returnString + "\t\t[Siebel account id=" + this.siebelAccountId + "\n";
        this.returnString = this.returnString + "\t\t[Account type=" + this.accountType + "\n";
        this.returnString = this.returnString + "\t\t[Ebs id=" + this.getEbsId() + "\n";
        this.returnString = this.returnString + "\t\t[Source id=" + String.valueOf(this.sourceId) + "\n";
        return this.getClass().getSimpleName() + "\n[Details\n\t[\n" + this.returnString + "\t]\n";
    }
}

